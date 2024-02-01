/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
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
import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.core.analytics.trace.Trace;
import io.vertigo.core.daemon.DaemonScheduled;
import io.vertigo.core.lang.Tuple;
import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.lang.json.CoreJsonAdapters;
import io.vertigo.core.node.config.discovery.NotDiscoverable;
import io.vertigo.datafactory.criteria.Criterions;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.DtListState;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.planning.agenda.dao.AgendaDAO;
import io.vertigo.planning.agenda.dao.TrancheHoraireDAO;
import io.vertigo.planning.agenda.domain.Agenda;
import io.vertigo.planning.agenda.domain.CritereTrancheHoraire;
import io.vertigo.planning.agenda.domain.PublicationRange;
import io.vertigo.planning.agenda.domain.TrancheHoraire;
import io.vertigo.planning.agenda.services.TrancheHoraireEvent;
import io.vertigo.planning.agenda.services.TrancheHoraireEvent.HoraireImpacte;
import io.vertigo.stella.master.MasterManager;
import io.vertigo.stella.master.WorkResultHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.ExpiryOption;
import redis.clients.jedis.args.ListDirection;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.util.KeyValue;

@NotDiscoverable
public class RedisUnifiedFoConsultationPlanningPlugin extends DbFoConsultationPlanningPlugin {
	private static final Gson V_CORE_GSON = CoreJsonAdapters.V_CORE_GSON;
	private static final Logger LOG = LogManager.getLogger(RedisUnifiedFoConsultationPlanningPlugin.class);
	private static final boolean USE_DISTRIBUTED_WORK = true;
	static final String PREFIX_REDIS_KEY = "cluster:";
	private static final int SYNCHRO_FREQUENCE_SECOND = 60;
	private static final int SYNCHRO_AGENDA_CHUNCK_SIZE = 5;

	private static final long EXPIRATION_DELAY_TRANCHE_HORAIRE_SECOND = SYNCHRO_FREQUENCE_SECOND + 60;

	private static final int EXPIRATION_DELAY_DATE_PREMIERE_DISPO_SECONDE = 15;
	private static final int EXPIRATION_DELAY_DATE_DERNIERE_PUBLI_SECONDE = 60;
	private static final int EXPIRATION_DELAY_PRECEDENTE_PUBLI_SECONDE = 10 * 60; //10 min de cache pour les dates de publication
	private static final int EXPIRATION_DELAY_PROCHAINE_PUBLI_SECONDE = 10 * 60; //10 min de cache pour les dates de publication

	private static final long TTL_MINIMUM_TO_INCR_DISPO = 5;

	private static final DateTimeFormatter FORMATTER_LOCAL_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

	@Inject
	private RedisConnector redisConnector;

	@Inject
	private AgendaDAO agendaDAO;
	@Inject
	private TrancheHoraireDAO trancheHoraireDAO;
	@Inject
	private AnalyticsManager analyticsManager;
	@Inject
	private MasterManager masterManager;

	private static final int MAX_LOOP = 100;

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
				final var prefixCleFonctionelle = getPrefixCleFonctionnelle(ageId, localDateToLookup);
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

	private static String getPrefixCleFonctionnelle(final Long ageId, final LocalDate localDateToLookup) {
		return "{" + PREFIX_REDIS_KEY + ageId + "}:" + FORMATTER_LOCAL_DATE.format(localDateToLookup);
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
				(date) -> FORMATTER_LOCAL_DATE.format(date),
				(dateStr) -> LocalDate.parse(dateStr, FORMATTER_LOCAL_DATE));
	}

	@Trace(category = "redis", name = "getDateDeDernierePublication")
	@Override
	public Optional<LocalDate> getDateDeDernierePublication(final List<UID<Agenda>> agendaUids) {
		final var keyDernierePublication = "{" + PREFIX_REDIS_KEY + agendaUids.hashCode() + "}:derniere-publication";
		return applyCache(keyDernierePublication, EXPIRATION_DELAY_DATE_DERNIERE_PUBLI_SECONDE,
				agendaUids, super::getDateDeDernierePublication,
				(date) -> FORMATTER_LOCAL_DATE.format(date),
				(dateStr) -> LocalDate.parse(dateStr, FORMATTER_LOCAL_DATE));
	}

	@Trace(category = "redis", name = "getPrecedentePublication")
	@Override
	public Optional<PublicationRange> getPrecedentePublication(final List<UID<Agenda>> agendaUids) {
		final var keyPrecedentePublication = "{" + PREFIX_REDIS_KEY + agendaUids.hashCode() + "}:precedente-publication";
		return applyCache(keyPrecedentePublication, EXPIRATION_DELAY_PRECEDENTE_PUBLI_SECONDE,
				agendaUids, super::getPrecedentePublication,
				(publicationRange) -> formatPublicationRange(publicationRange),
				(publicationRangeStr) -> parsePublicationRange(publicationRangeStr));
	}

	@Trace(category = "redis", name = "getProchainePublication")
	@Override
	public Optional<PublicationRange> getProchainePublication(final List<UID<Agenda>> agendaUids) {
		final var keyProchainePublication = "{" + PREFIX_REDIS_KEY + agendaUids.hashCode() + "}:prochaine-publication";
		return applyCache(keyProchainePublication, EXPIRATION_DELAY_PROCHAINE_PUBLI_SECONDE,
				agendaUids, super::getProchainePublication,
				(publicationRange) -> formatPublicationRange(publicationRange),
				(publicationRangeStr) -> parsePublicationRange(publicationRangeStr));
	}

	private String formatPublicationRange(final PublicationRange publicationRange) {
		return V_CORE_GSON.toJson(publicationRange);
	}

	private PublicationRange parsePublicationRange(final String publicationRangeStr) {
		return V_CORE_GSON.fromJson(publicationRangeStr, PublicationRange.class);
	}

	private <R, I> Optional<R> applyCache(final String cacheKey, final long cacheSecond, final I param, final Function<I, Optional<R>> function, final Function<R, String> toString, final Function<String, R> fromString) {
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
		if (USE_DISTRIBUTED_WORK) {
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
			LOG.debug("HAVE LOCK");
			// pour l'instant on prend toutes les agendas
			final DtList<Agenda> agendas = agendaDAO.findAll(Criterions.alwaysTrue(), DtListState.of(null));
			IntStream.range(0, (agendas.size() + SYNCHRO_AGENDA_CHUNCK_SIZE - 1) / SYNCHRO_AGENDA_CHUNCK_SIZE)
					.mapToObj(i -> agendas.subList(i * SYNCHRO_AGENDA_CHUNCK_SIZE, Math.min(SYNCHRO_AGENDA_CHUNCK_SIZE * (i + 1), agendas.size()))) //crée une list<list<agenda>> tout les SYNCHRO_AGENDA_CHUNCK_SIZE éléments
					.map(listAgenda -> listAgenda.stream().map(agenda -> agenda.getUID().urn() + "->" + agenda.getNom()).collect(Collectors.toList())) //transforme en list<list<String>> avec les urn->nom
					.collect(Collectors.toList())
					.forEach(todoList -> masterManager.schedule(todoList, WorkEngineSynchroDbRedisCreneau.class, EMPTY_WORK_RESULT_HANDLER)); //enregistre les paquets de chose à faire
			LOG.debug("push todo");
		} else {
			LOG.debug("DON'T HAVE LOCK");
		}

		//2- Les Workers récupèrent les éléments de la todoList en boucle
	}

	private void synchroDbRedisCreneauLocal() {
		//1- On tente de prendre un lock dans Redis : pour lire les taches à faire et remplir la todoList
		final var jedis = redisConnector.getClient();
		final boolean lock = RedisConnectorUtil.obtainLock(jedis, "DmnSynchroDbRedisCreneau.lock", SYNCHRO_FREQUENCE_SECOND - 1);
		if (lock) { //lock récupéré
			LOG.debug("HAVE LOCK");
			// pour l'instant on prend toutes les agendas
			final DtList<Agenda> agendas = agendaDAO.findAll(Criterions.alwaysTrue(), DtListState.of(null));
			final String[] ids = new String[agendas.size()];
			agendas.stream().map(agenda -> agenda.getUID().urn() + "->" + agenda.getNom()).collect(Collectors.toList()).toArray(ids);
			jedis.rpush("DmnSynchroDbRedisCreneau.todoList", ids);
			LOG.debug("push todo");
		} else {
			LOG.debug("DON'T HAVE LOCK");
			//Si on a pas le lock, on attend la fin du TTL
			//A fur et à mesure les nodes se synchronizeront et tomberont au même moment
			//il est important que le deamon qui remplit la todoList, traite aussi la file
			final long lockTTL = jedis.ttl("DmnSynchroDbRedisCreneau.lock");
			if (lockTTL == -1) {
				//pas le lock mais pas d'expire : il y a un pb : ne devrait pas arriver
				LOG.debug("FIX expire");
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
			LOG.debug("pre blmpop");
			idsTodo = jedis.blmpop(1, ListDirection.RIGHT, SYNCHRO_AGENDA_CHUNCK_SIZE /*~5*/, "DmnSynchroDbRedisCreneau.todoList");

			if (idsTodo != null) {
				final var workEngineSynchroDbRedisCreneau = new WorkEngineSynchroDbRedisCreneau();
				workEngineSynchroDbRedisCreneau.process(idsTodo.getValue());
			} else {
				LOG.debug("post blmpop : null");
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

	private void synchroDbRedisCreneauFromTrancheHoraire(final Map<Long, List<TrancheHoraire>> trancheHorairesByDemId) {
		final TrancheHoraireUpdateStats stats = new TrancheHoraireUpdateStats();
		//final var jedis = redisUnifiedConnector.getClient();
		for (final Entry<Long, List<TrancheHoraire>> trancheHoraireEntry : trancheHorairesByDemId.entrySet()) {
			final var ageId = trancheHoraireEntry.getKey();
			final Map<String, List<TrancheHoraire>> dateMap = trancheHoraireEntry.getValue().stream()
					.collect(Collectors.groupingBy(th -> getPrefixCleFonctionnelle(ageId, th.getDateLocale())));

			for (final var dateMapEntry : dateMap.entrySet()) {
				try (var jedis = redisConnector.getClient(dateMapEntry.getKey())) {
					synchroDbRedisByPrefix(jedis, dateMapEntry.getKey(), dateMapEntry.getValue(), trancheHoraire -> {
						if (trancheHoraire.getNbGuichet() > 0) {
							final int ageDispoSecond = (int) ChronoUnit.SECONDS.between(trancheHoraire.getInstantPublication(), Instant.now());
							++stats.nbTranches;
							stats.sumAgeDispo += ageDispoSecond;
							stats.minAgeDispo = Math.min(ageDispoSecond, stats.minAgeDispo);
							stats.maxAgeDispo = Math.max(ageDispoSecond, stats.maxAgeDispo);
							analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
									.incMeasure("nbDispos", trancheHoraire.getNbGuichet()));
						}
					});
				}
			}
		}

		if (stats.nbTranches > 0) {
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.setMeasure("nbTranches", stats.nbTranches)
					.setMeasure("sumAgeDispo", stats.sumAgeDispo)
					.setMeasure("minAgeDispo", stats.minAgeDispo)
					.setMeasure("maxAgeDispo", stats.maxAgeDispo));
		}
	}

	private void synchroDbRedisByPrefix(final Jedis jedis, final String prefixCleFonctionelle, final List<TrancheHoraire> trancheHoraireList,
			final Consumer<TrancheHoraire> statUpdater) {
		final String cleJournee = prefixCleFonctionelle + ":horaire";
		final long time = System.currentTimeMillis();
		try (final var tx = jedis.multi()) { //Bien vérifier que toutes les clés de cette Tx ont le même hashTag pour etre compatible avec le mode Cluster de Redis
			// on supprime l'ancien set avant d'y remettre les valeurs à jour afin de ne pas conserver d'ancienne valeurs obsoletes dans le set
			tx.del(cleJournee);
			// on ajoute les nouvelles valeurs
			for (final TrancheHoraire trancheHoraire : trancheHoraireList) {
				// on ajoute la tranche horaire dans le set
				tx.sadd(cleJournee, trancheHoraire.getMinutesDebut().toString() + "$" + trancheHoraire.getTrhId());
				// on ajoute le nombre de dispos dans une clé dédiée
				final var cleFonctionelle = prefixCleFonctionelle + ":" + trancheHoraire.getMinutesDebut();
				tx.hset(cleFonctionelle, Map.of(
						"nbDispos", trancheHoraire.getNbGuichet().toString(), //pas vraiment le nombre de guichet, le select retourne le nombre de dispo
						"trhId", trancheHoraire.getTrhId().toString()));
				tx.expire(cleFonctionelle, EXPIRATION_DELAY_TRANCHE_HORAIRE_SECOND);

				// on met a jour les statistiques pour le traceur interne (logs)
				statUpdater.accept(trancheHoraire);
			}
			tx.expire(cleJournee, EXPIRATION_DELAY_TRANCHE_HORAIRE_SECOND);
			tx.exec();
		} finally {
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure("sub_redis_duration", System.currentTimeMillis() - time)
					.incMeasure("sub_redis_count", 1));
		}
	}

	/*private void synchroDbRedisByPrefix(final JedisCluster jedis, final String prefixCleFonctionelle, final List<TrancheHoraire> trancheHoraireList,
			final Consumer<TrancheHoraire> statUpdater) {
		final String cleJournee = prefixCleFonctionelle + ":horaire";
		final long time = System.currentTimeMillis();
		try (final var plCleFonctionelle = jedis.pipelined()) {
			try (final var plCleJournee = jedis.pipelined()) {
				// on supprime l'ancien set avant d'y remettre les valeurs à jour afin de ne pas conserver d'ancienne valeurs obsoletes dans le set
				plCleJournee.del(cleJournee);
				// on ajoute les nouvelles valeurs
				for (final TrancheHoraire trancheHoraire : trancheHoraireList) {
					// on ajoute la tranche horaire dans le set
					plCleJournee.sadd(cleJournee, trancheHoraire.getMinutesDebut().toString() + "$" + trancheHoraire.getTrhId());
					// on ajoute le nombre de dispos dans une clé dédiée
					final var cleFonctionelle = prefixCleFonctionelle + ":" + trancheHoraire.getMinutesDebut();
					plCleFonctionelle.hset(cleFonctionelle, Map.of(
							"nbDispos", trancheHoraire.getNbGuichet().toString(), //pas vraiment le nombre de guichet, le select retourne le nombre de dispo
							"trhId", trancheHoraire.getTrhId().toString()));
					plCleFonctionelle.expire(cleFonctionelle, EXPIRATION_DELAY_TRANCHE_HORAIRE_SECOND);

					// on met a jour les statistiques pour le traceur interne (logs)
					statUpdater.accept(trancheHoraire);
				}
				plCleJournee.expire(cleJournee, EXPIRATION_DELAY_TRANCHE_HORAIRE_SECOND);
			} //close pipeline1 over cleJournee : exec sync
		} finally { //close pipeline2 over cleFonctionelle : exec sync
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure("sub_redis_duration", System.currentTimeMillis() - time)
					.incMeasure("sub_redis_count", 1));
		}
	}*/

	private void synchroDbRedisCreneauFromHoraireImpacte(final Map<Long, List<HoraireImpacte>> horaireImpacteByDem) {
		final Map<Long, List<TrancheHoraire>> trancheHoraireByDemToResync = new HashMap<>();

		for (final Entry<Long, List<HoraireImpacte>> entry : horaireImpacteByDem.entrySet()) {
			final List<LocalDate> listDates = entry.getValue().stream()
					.map(HoraireImpacte::getLocalDate)
					.toList();
			final var trancheHoraires = trancheHoraireDAO.synchroGetTrancheHorairesByAgeIdAndDates(List.of(entry.getKey()), listDates, Instant.now());
			trancheHoraireByDemToResync.put(entry.getKey(), trancheHoraires);
		}
		synchroDbRedisCreneauFromTrancheHoraire(trancheHoraireByDemToResync);
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
		final Map<Long, List<HoraireImpacte>> horaireImpacteByDemToResync = new HashMap<>();
		for (final HoraireImpacte horaire : trancheHoraireEvent.getHoraires()) {
			final var prefixCleFonctionelle = getPrefixCleFonctionnelle(horaire.getAgeId(), horaire.getLocalDate());
			final var cleFonctionelle = prefixCleFonctionelle + ":" + horaire.getMinutesDebut();
			//cleFonctionelle and prefixCleFonctionelle
			final long ttl = jedis.ttl(cleFonctionelle);
			if (ttl < TTL_MINIMUM_TO_INCR_DISPO) { //si le creneau n'est plus là, ou si il expire dans moins de 5s, on ne fait pas de incr, on lance la resynchro
				horaireImpacteByDemToResync.computeIfAbsent(horaire.getAgeId(), (demid) -> new ArrayList<>())
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
			}
		}
		if (!horaireImpacteByDemToResync.isEmpty()) {
			synchroDbRedisCreneauFromHoraireImpacte(horaireImpacteByDemToResync);
		}
	}

	private static final class TrancheHoraireUpdateStats {
		private int nbTranches = 0;
		private int sumAgeDispo = 0;
		private int minAgeDispo = Integer.MAX_VALUE;
		private int maxAgeDispo = -1;
	}
}
