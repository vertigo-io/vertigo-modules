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

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.connectors.redis.RedisConnector;
import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.component.di.DIInjector;
import io.vertigo.planning.agenda.dao.TrancheHoraireDAO;
import io.vertigo.planning.agenda.domain.TrancheHoraire;
import io.vertigo.planning.agenda.services.TrancheHoraireEvent.HoraireImpacte;
import redis.clients.jedis.Jedis;

public final class SynchroDbRedisCreneauHelper {

	static final String PREFIX_REDIS_KEY = "cluster:";
	private static final int SYNCHRO_FREQUENCE_SECOND = 60;
	private static final long EXPIRATION_DELAY_TRANCHE_HORAIRE_SECOND = SYNCHRO_FREQUENCE_SECOND + 60;

	private static final DateTimeFormatter FORMATTER_LOCAL_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

	@Inject
	private RedisConnector redisConnector;
	@Inject
	private AnalyticsManager analyticsManager;
	@Inject
	private TrancheHoraireDAO trancheHoraireDAO;

	//private static final Logger LOG = LogManager.getLogger(SynchroDbRedisCreneauHelper.class);

	protected SynchroDbRedisCreneauHelper() {
		DIInjector.injectMembers(this, Node.getNode().getComponentSpace());
	}

	protected void synchroDbRedisCreneauFromHoraireImpacte(final Map<Long, List<HoraireImpacte>> horaireImpacteByAge) {
		// Collecter tous les ids et toutes les dates
		final List<Long> allDemIds = horaireImpacteByAge.keySet().stream().toList();
		final List<LocalDate> allDates = horaireImpacteByAge.values().stream()
				.flatMap(listHoraireImpacte -> listHoraireImpacte.stream().map(HoraireImpacte::getLocalDate))
				.distinct()
				.toList();

		// Faire une requête unique pour récupérer toutes les TrancheHoraire impactées
		final List<TrancheHoraire> trancheHoraires = trancheHoraireDAO.synchroGetTrancheHorairesByAgeIdsAndDates(allDemIds, allDates, Instant.now());

		// Regrouper les TrancheHoraire par id
		final Map<Long, List<TrancheHoraire>> trancheHoraireByAgeToResync = trancheHoraires.stream()
				.collect(Collectors.groupingBy(TrancheHoraire::getAgeId));

		//reuse common resynch
		synchroDbRedisCreneauFromTrancheHoraire(trancheHoraireByAgeToResync);
	}

	protected void synchroDbRedisCreneauFromTrancheHoraire(final Map<Long, List<TrancheHoraire>> trancheHorairesByDemId) {
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

	protected static String getPrefixCleFonctionnelle(final Long ageId, final LocalDate localDateToLookup) {
		return "{" + PREFIX_REDIS_KEY + ageId + "}:" + FORMATTER_LOCAL_DATE.format(localDateToLookup);
	}

	private static final class TrancheHoraireUpdateStats {

		private int nbTranches = 0;
		private int sumAgeDispo = 0;
		private int minAgeDispo = Integer.MAX_VALUE;
		private int maxAgeDispo = -1;
	}

}
