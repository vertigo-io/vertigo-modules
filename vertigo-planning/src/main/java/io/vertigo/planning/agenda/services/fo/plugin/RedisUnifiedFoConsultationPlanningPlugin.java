/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.planning.agenda.services.fo.plugin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import io.vertigo.commons.eventbus.EventBusSubscribed;
import io.vertigo.commons.transaction.Transactional;
import io.vertigo.connectors.redis.RedisConnector;
import io.vertigo.connectors.redis.RedisConnectorUtil;
import io.vertigo.core.analytics.trace.Trace;
import io.vertigo.core.daemon.DaemonScheduled;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Tuple;
import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.lang.json.CoreJsonAdapters;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.core.node.config.discovery.NotDiscoverable;
import io.vertigo.core.param.ParamValue;
import io.vertigo.datamodel.criteria.Criterions;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.DtListState;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.planning.agenda.dao.AgendaDAO;
import io.vertigo.planning.agenda.domain.Agenda;
import io.vertigo.planning.agenda.domain.CritereTrancheHoraire;
import io.vertigo.planning.agenda.domain.PublicationRange;
import io.vertigo.planning.agenda.domain.TrancheHoraire;
import io.vertigo.planning.agenda.services.TrancheHoraireEvent;
import io.vertigo.planning.agenda.services.TrancheHoraireEvent.HoraireImpacte;
import io.vertigo.stella.master.MasterManager;
import io.vertigo.stella.master.WorkResultHandler;
import redis.clients.jedis.args.ExpiryOption;
import redis.clients.jedis.args.ListDirection;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.util.KeyValue;

@NotDiscoverable
public class RedisUnifiedFoConsultationPlanningPlugin extends DbFoConsultationPlanningPlugin implements Activeable {

	private static final Gson V_CORE_GSON = CoreJsonAdapters.V_CORE_GSON;
	private static final Logger LOG = LogManager.getLogger(RedisUnifiedFoConsultationPlanningPlugin.class);
	private static final String PREFIX_REDIS_KEY = SynchroDbRedisCreneauHelper.PREFIX_REDIS_KEY;
	private static final int SYNCHRO_FREQUENCE_SECOND = 60;
	private static final int SYNCHRO_AGENDA_CHUNCK_SIZE = 5;

	private static final int EXPIRATION_DELAY_DATE_PREMIERE_DISPO_SECONDE = 15;
	private static final int EXPIRATION_DELAY_DATE_DERNIERE_PUBLI_SECONDE = 60;
	private static final int EXPIRATION_DELAY_PRECEDENTE_PUBLI_SECONDE = 10 * 60; //10 min de cache pour les dates de publication
	private static final int EXPIRATION_DELAY_PROCHAINE_PUBLI_SECONDE = 10 * 60; //10 min de cache pour les dates de publication

	private static final long TTL_MINIMUM_TO_INCR_DISPO = 5;

	private static final DateTimeFormatter FORMATTER_LOCAL_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

	private final RedisConnector redisConnector;
	private final AgendaDAO agendaDAO;
	private final Optional<MasterManager> masterManagerOpt;
	private final boolean useDistributedWork;
	//init at start
	private SynchroDbRedisCreneauHelper synchroDbRedisCreneauHelper;

	private static final int MAX_LOOP = 100;

	/**
	 * Constructor.
	 * @param redisConnector Redis connector
	 * @param agendaDAO Agenda DAO
	 * @param masterManagerOpt Master manager
	 * @param distributedSynchroOpt if synchro is distributed (use Stella MasterManager)
	 */
	@Inject
	public RedisUnifiedFoConsultationPlanningPlugin(
			final RedisConnector redisConnector,
			final AgendaDAO agendaDAO,
			final Optional<MasterManager> masterManagerOpt,
			@ParamValue("distributedSynchro") final Optional<Boolean> distributedSynchroOpt) {
		Assertion.check()
				.isNotNull(redisConnector)
				.isNotNull(agendaDAO)
				.isNotNull(masterManagerOpt)
				.isNotNull(distributedSynchroOpt)
				.when(distributedSynchroOpt.orElse(false),
						() -> Assertion.check().isTrue(masterManagerOpt.isPresent(), "distributedSynchro requires masterManager"));
		//-----
		this.redisConnector = redisConnector;
		this.agendaDAO = agendaDAO;
		this.masterManagerOpt = masterManagerOpt;
		useDistributedWork = distributedSynchroOpt.orElse(false);
	}

	@Override
	public void start() {
		synchroDbRedisCreneauHelper = new SynchroDbRedisCreneauHelper();
	}

	@Override
	public void stop() {
		//nothing
	}

	@Trace(category = "redis", name = "getTrancheHoraireDisponibles")
	@Override
	public DtList<TrancheHoraire> getTrancheHoraireDisponibles(final CritereTrancheHoraire critereTrancheHoraire) {
		final var tranchesResult = new DtList<TrancheHoraire>(TrancheHoraire.class);
		final LocalDate minDisplayDay = critereTrancheHoraire.getDateMin();
		int minDisplayMinute = critereTrancheHoraire.getMinutesMin();

		final var jedis = redisConnector.getClient();
		for (var jourToAdd = 0; jourToAdd < 7; ++jourToAdd) {
			final var localDateToLookup = critereTrancheHoraire.getPremierJour().plusDays(jourToAdd);
			if (localDateToLookup.isBefore(minDisplayDay)) {
				continue;
			} else if (!localDateToLookup.isEqual(minDisplayDay)) {
				//le minDisplayMinute ne sert que si localDateToLookup == minDisplayDay
				minDisplayMinute = 0;
			}

			for (final Long ageId : critereTrancheHoraire.getAgeIds()) {
				// on boucle sur les agendas
				final var prefixCleFonctionelle = SynchroDbRedisCreneauHelper.getPrefixCleFonctionnelle(ageId, localDateToLookup);
				var cursor = "0";
				final var scanParams = new ScanParams();
				scanParams.count(100);
				final int filterMinDisplayMinute = minDisplayMinute;
				var loop = 0;
				do {
					//
					final var scanResult = jedis.sscan(prefixCleFonctionelle + ":horaire", cursor, scanParams);
					cursor = scanResult.getCursor();
					scanResult.getResult()
							.stream()
							.filter(minAndTrhId -> minAndTrhId.contains("$"))
							.map(minAndTrhId -> {
								final String[] minTrhIdArray = minAndTrhId.split("\\$");

								return Tuple.of(Integer.parseInt(minTrhIdArray[0]), Long.valueOf(minTrhIdArray[1]));
							})
							.filter(tuple -> tuple.val1() >= filterMinDisplayMinute)
							.forEach(tuple -> tranchesResult.add(fromMapAndKey(tuple.val1(), localDateToLookup, tuple.val2())));
					if (++loop > MAX_LOOP) {
						throw new VSystemException("Too many loop for " + localDateToLookup.toString());
					}
				} while (!"0".equals(cursor));
			}
		}

		tranchesResult.sort(Comparator.comparing(TrancheHoraire::getMinutesDebut));
		return tranchesResult;
	}

	private static TrancheHoraire fromMapAndKey(final int minutesDebut, final LocalDate localDate, final Long trhId) {
		final var trancheHoraire = new TrancheHoraire();
		trancheHoraire.setTrhId(trhId);
		trancheHoraire.setDateLocale(localDate);
		trancheHoraire.setMinutesDebut(minutesDebut);
		return trancheHoraire;
	}

	@Trace(category = "redis", name = "getDateDePremiereDisponibilite")
	@Override
	public Optional<LocalDate> getDateDePremiereDisponibilite(final CritereTrancheHoraire critereTrancheHoraire) {
		final var premierJour = critereTrancheHoraire.getPremierJour();
		// we make a hashcode of the list of ageId for the key, because here redis is a simple cache over the db
		final var keyProchaineDispo = "{" + PREFIX_REDIS_KEY + critereTrancheHoraire.getAgeIds().hashCode() + "}:" + FORMATTER_LOCAL_DATE.format(premierJour) + ":prochaine-dispo";
		return applyCache(keyProchaineDispo, EXPIRATION_DELAY_DATE_PREMIERE_DISPO_SECONDE,
				critereTrancheHoraire, super::getDateDePremiereDisponibilite,
				date -> FORMATTER_LOCAL_DATE.format(date),
				dateStr -> LocalDate.parse(dateStr, FORMATTER_LOCAL_DATE));
	}

	@Trace(category = "redis", name = "getDateDeDernierePublication")
	@Override
	public Optional<LocalDate> getDateDeDernierePublication(final List<UID<Agenda>> agendaUids) {
		final var keyDernierePublication = "{" + PREFIX_REDIS_KEY + agendaUids.hashCode() + "}:derniere-publication";
		return applyCache(keyDernierePublication, EXPIRATION_DELAY_DATE_DERNIERE_PUBLI_SECONDE,
				agendaUids, super::getDateDeDernierePublication,
				date -> FORMATTER_LOCAL_DATE.format(date),
				dateStr -> LocalDate.parse(dateStr, FORMATTER_LOCAL_DATE));
	}

	@Trace(category = "redis", name = "getPrecedentePublication")
	@Override
	public Optional<PublicationRange> getPrecedentePublication(final List<UID<Agenda>> agendaUids) {
		final var keyPrecedentePublication = "{" + PREFIX_REDIS_KEY + agendaUids.hashCode() + "}:precedente-publication";
		return applyCache(keyPrecedentePublication, EXPIRATION_DELAY_PRECEDENTE_PUBLI_SECONDE,
				agendaUids, super::getPrecedentePublication,
				(Function<PublicationRange, String>) this::formatPublicationRange,
				(Function<String, PublicationRange>) this::parsePublicationRange);
	}

	@Trace(category = "redis", name = "getProchainePublication")
	@Override
	public Optional<PublicationRange> getProchainePublication(final List<UID<Agenda>> agendaUids) {
		final var keyProchainePublication = "{" + PREFIX_REDIS_KEY + agendaUids.hashCode() + "}:prochaine-publication";
		return applyCache(keyProchainePublication, EXPIRATION_DELAY_PROCHAINE_PUBLI_SECONDE,
				agendaUids, super::getProchainePublication,
				(Function<PublicationRange, String>) this::formatPublicationRange,
				(Function<String, PublicationRange>) this::parsePublicationRange);
	}

	private String formatPublicationRange(final PublicationRange publicationRange) {
		return V_CORE_GSON.toJson(publicationRange);
	}

	private PublicationRange parsePublicationRange(final String publicationRangeStr) {
		return V_CORE_GSON.fromJson(publicationRangeStr, PublicationRange.class);
	}

	private <R, I> Optional<R> applyCache(final String cacheKey, final long cacheSecond, final I param, final Function<I, Optional<R>> function, final Function<R, String> toString,
			final Function<String, R> fromString) {
		final var jedis = redisConnector.getClient();
		final var cachedValue = jedis.get(cacheKey);
		if (cachedValue == null) {
			final var resultOpt = function.apply(param);
			resultOpt.ifPresentOrElse(result -> {
				jedis.set(cacheKey, toString.apply(result));
			}, () -> {
				jedis.set(cacheKey, "none");
			});
			jedis.expire(cacheKey, cacheSecond);
			return resultOpt;
		} else if ("none".equals(cachedValue)) {
			return Optional.empty();
		}
		return Optional.of(fromString.apply(cachedValue));
	}

	@DaemonScheduled(name = "DmnSynchroDbRedisCreneau", periodInSeconds = SYNCHRO_FREQUENCE_SECOND/*60s*/)
	@Transactional
	public void synchroDbRedisCreneau() {
		if (useDistributedWork) {
			synchroDbRedisCreneauDistributedWork();
		} else {
			synchroDbRedisCreneauLocal();
		}
	}

	private static final WorkResultHandler<Boolean> EMPTY_WORK_RESULT_HANDLER = new WorkResultHandler<>() {

		@Override
		public void onStart() {
			//nothing
		}

		@Override
		public void onDone(final Boolean result, final Throwable error) {
			//nothing
		}
	};

	private void synchroDbRedisCreneauDistributedWork() {
		//1- On tente de prendre un lock dans Redis : pour lire les taches à faire et remplir la todoList
		final var jedis = redisConnector.getClient();
		final boolean lock = RedisConnectorUtil.obtainLock(jedis, "DmnSynchroDbRedisCreneau.lock", SYNCHRO_FREQUENCE_SECOND - 1);
		if (lock) { //lock récupéré
			// pour l'instant on prend toutes les agendas
			final DtList<Agenda> agendas = agendaDAO.findAll(Criterions.alwaysTrue(), DtListState.of(null));
			IntStream.range(0, (agendas.size() + SYNCHRO_AGENDA_CHUNCK_SIZE - 1) / SYNCHRO_AGENDA_CHUNCK_SIZE)
					.mapToObj(i -> agendas.subList(i * SYNCHRO_AGENDA_CHUNCK_SIZE, Math.min(SYNCHRO_AGENDA_CHUNCK_SIZE * (i + 1), agendas.size()))) //crée une list<list<agenda>> tout les SYNCHRO_AGENDA_CHUNCK_SIZE éléments
					.map(listAgenda -> listAgenda.stream().map(agenda -> agenda.getUID().urn() + "->" + agenda.getNom()).collect(Collectors.toList())) //transforme en list<list<String>> avec les urn->nom
					.collect(Collectors.toList())
					.forEach(todoList -> masterManagerOpt.get().schedule(todoList, WorkEngineSynchroDbRedisCreneau.class, EMPTY_WORK_RESULT_HANDLER)); //enregistre les paquets de chose à faire
		}

		//2- Les Workers récupèrent les éléments de la todoList en boucle
	}

	private void synchroDbRedisCreneauLocal() {
		//1- On tente de prendre un lock dans Redis : pour lire les taches à faire et remplir la todoList
		final var jedis = redisConnector.getClient();
		final boolean lock = RedisConnectorUtil.obtainLock(jedis, "DmnSynchroDbRedisCreneau.lock", SYNCHRO_FREQUENCE_SECOND - 1);
		if (lock) { //lock récupéré
			// pour l'instant on prend toutes les agendas
			final DtList<Agenda> agendas = agendaDAO.findAll(Criterions.alwaysTrue(), DtListState.of(null));
			final String[] ids = new String[agendas.size()];
			agendas.stream().map(agenda -> agenda.getUID().urn() + "->" + agenda.getNom()).collect(Collectors.toList()).toArray(ids);
			jedis.rpush("DmnSynchroDbRedisCreneau.todoList", ids);
		} else {
			//Si on a pas le lock, on attend la fin du TTL
			//A fur et à mesure les nodes se synchronizeront et tomberont au même moment
			//il est important que le deamon qui remplit la todoList, traite aussi la file
			final long lockTTL = jedis.ttl("DmnSynchroDbRedisCreneau.lock");
			if (lockTTL == -1) {
				//pas le lock mais pas d'expire : il y a un pb : ne devrait pas arriver
				jedis.expire("DmnSynchroDbRedisCreneau.lock", SYNCHRO_FREQUENCE_SECOND - 1, ExpiryOption.NX);
			} else if (lockTTL < 5) {
				LOG.debug("lock libéré dans moins de 5s, on attend et on retente le lock");
				sleepSeconds(lockTTL + 1);
				synchroDbRedisCreneau();
				return;//et on quite
			} else if (lockTTL < SYNCHRO_FREQUENCE_SECOND - 2) {
				LOG.debug("locké posé il y a " + (SYNCHRO_FREQUENCE_SECOND - 1 - lockTTL) + "s on stop pour se faire rattraper");
				return;//et on quite
			} else {
				LOG.debug("lock posé il y a moins de 2s : continue ");
			}
		}

		//2- On récupère les éléments de la todoList en boucle
		KeyValue<String, List<String>> idsTodo;
		do {
			//on fait un pop bloquant avec un timeout de 1s sur un max de 5 éléments de la todoList
			idsTodo = jedis.blmpop(1, ListDirection.RIGHT, SYNCHRO_AGENDA_CHUNCK_SIZE /*~5*/, "DmnSynchroDbRedisCreneau.todoList");

			if (idsTodo != null) {
				final var workEngineSynchroDbRedisCreneau = new WorkEngineSynchroDbRedisCreneau();
				workEngineSynchroDbRedisCreneau.process(idsTodo.getValue());
			}
		} while (idsTodo != null && idsTodo.getValue().size() == SYNCHRO_AGENDA_CHUNCK_SIZE);
	}

	private void sleepSeconds(final long sleepSeconds) {
		try {
			Thread.sleep(Math.max(0, sleepSeconds) * 1000);
		} catch (final InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Transactional
	@EventBusSubscribed
	public void onTrancheHoraireEvent(final TrancheHoraireEvent trancheHoraireEvent) {
		final var jedis = redisConnector.getClient();
		final boolean setZero;
		final int increment;
		switch (trancheHoraireEvent.getType()) {
			case SUPPRIME:
				setZero = true;
				increment = 0;
				break;
			case CONSOMME:
				setZero = false;
				increment = -1;
				break;
			case LIBERE:
				setZero = false;
				increment = 1;
				break;
			default:
				throw new VSystemException("type d'evenement de tranche horaire inconnu {0}", trancheHoraireEvent.getType());
		}
		final Map<Long, List<HoraireImpacte>> horaireImpacteByAgeToResync = new HashMap<>();
		for (final HoraireImpacte horaire : trancheHoraireEvent.getHoraires()) {
			final var prefixCleFonctionelle = SynchroDbRedisCreneauHelper.getPrefixCleFonctionnelle(horaire.getAgeId(), horaire.getLocalDate());
			final var cleFonctionelle = prefixCleFonctionelle + ":" + horaire.getMinutesDebut();
			//cleFonctionelle and prefixCleFonctionelle
			final long ttl = jedis.ttl(cleFonctionelle);
			if (ttl < TTL_MINIMUM_TO_INCR_DISPO) { //si le creneau n'est plus là, ou si il expire dans moins de 5s, on ne fait pas de incr, on lance la resynchro
				horaireImpacteByAgeToResync.computeIfAbsent(horaire.getAgeId(), demid -> new ArrayList<>())
						.add(horaire);
			} else {
				long newVal = 0;
				if (!setZero) {
					newVal = jedis.hincrBy(cleFonctionelle, "nbDispos", increment);
					if (increment == 1 && newVal == 1) {
						final var trhId = jedis.hget(cleFonctionelle, "trhId");
						// we need to add the key in the set used by consultation
						jedis.sadd(prefixCleFonctionelle + ":horaire", horaire.getMinutesDebut().toString() + "$" + trhId);
					}
				}
				if (setZero || newVal < 0) {
					jedis.hset(cleFonctionelle, "nbDispos", "0");
				}
				if (newVal <= 0) {
					final var trhId = jedis.hget(cleFonctionelle, "trhId");
					// we need to delete the key in the set used by consultation
					jedis.srem(prefixCleFonctionelle + ":horaire", horaire.getMinutesDebut().toString() + "$" + trhId);
				}
				//LOG.debug("update redis cache of {0} to {3} (increment {1}, setZero {2})", cleFonctionelle, increment, setZero, newVal);
			}
		}
		if (!horaireImpacteByAgeToResync.isEmpty()) {
			synchroDbRedisCreneauHelper.synchroDbRedisCreneauFromHoraireImpacte(horaireImpacteByAgeToResync);
			final StringBuilder logContent = new StringBuilder("Contenu de trancheHoraireByDemToResync:\n");
			horaireImpacteByAgeToResync.forEach((id, trancheList) -> {
				final List<LocalDate> dates = trancheList.stream().map(HoraireImpacte::getLocalDate).distinct().toList();
				logContent.append("ageId: ").append(id).append(" => Dates: ").append(dates).append("\n");
			});
			LOG.info("Resync redis cache {0}", logContent.toString());
		}
	}

}
