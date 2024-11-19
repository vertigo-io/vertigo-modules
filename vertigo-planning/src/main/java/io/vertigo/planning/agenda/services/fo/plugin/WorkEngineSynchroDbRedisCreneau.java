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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.commons.transaction.VTransactionWritable;
import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.planning.agenda.dao.TrancheHoraireDAO;
import io.vertigo.planning.agenda.domain.Agenda;
import io.vertigo.planning.agenda.domain.TrancheHoraire;
import io.vertigo.stella.work.WorkEngine;

public class WorkEngineSynchroDbRedisCreneau implements WorkEngine<List<String>, Boolean> {

	@Inject
	private VTransactionManager transactionManager;
	@Inject
	private AnalyticsManager analyticsManager;
	@Inject
	private TrancheHoraireDAO trancheHoraireDAO;

	private static final Logger LOG = LogManager.getLogger(WorkEngineSynchroDbRedisCreneau.class);

	@Override
	public Boolean process(final List<String> idsTodo) {
		final Map<Long, String> agendaNames = new HashMap<>();
		for (final var idTodo : idsTodo) {
			final String[] idSplit = idTodo.split("->");
			final UID<Agenda> agendaUid = UID.of(idSplit[0]);
			final String agendaName = idSplit[1];
			agendaNames.put(agendaUid.getId(), agendaName);
		}

		final List<TrancheHoraire> trancheHoraires;
		try (final VTransactionWritable tx = transactionManager.createCurrentTransaction()) {
			trancheHoraires = trancheHoraireDAO.synchroGetTrancheHorairesByAgeIds(new ArrayList<>(agendaNames.keySet()), Instant.now());
			tx.rollback();
		}
		LOG.debug("synchroagenda: ageSize: {}, trhSize: {} : {} ", idsTodo.size(), trancheHoraires.size(), agendaNames.keySet());
		final SynchroDbRedisCreneauHelper synchroDbRedisCreneauHelper = new SynchroDbRedisCreneauHelper();
		final Map<UID<Agenda>, List<TrancheHoraire>> trancheHorairesPerAgenda = trancheHoraires.stream()
				.collect(Collectors.groupingBy(trh -> trh.agenda().getUID()));
		for (final Entry<UID<Agenda>, List<TrancheHoraire>> trhPerAge : trancheHorairesPerAgenda.entrySet()) {
			final String agendaUrn = trhPerAge.getKey().urn();
			analyticsManager.trace("synchroagenda", agendaUrn, tracer -> {
				synchroDbRedisCreneauHelper.synchroDbRedisCreneauFromTrancheHoraire(Map.of(trhPerAge.getKey().getId(), trhPerAge.getValue()));
				tracer.incMeasure("nbDispos", 0) //pour init a 0
						.setTag("agenda", agendaUrn)
						.setMetadata("agendaName", agendaNames.get(trhPerAge.getKey().getId()));
			});
		}
		return true;
	}
}
