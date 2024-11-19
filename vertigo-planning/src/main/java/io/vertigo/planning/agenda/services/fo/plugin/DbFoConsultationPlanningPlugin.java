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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.core.node.config.discovery.NotDiscoverable;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.planning.agenda.AgendaPAO;
import io.vertigo.planning.agenda.dao.TrancheHoraireDAO;
import io.vertigo.planning.agenda.domain.Agenda;
import io.vertigo.planning.agenda.domain.CritereTrancheHoraire;
import io.vertigo.planning.agenda.domain.PublicationRange;
import io.vertigo.planning.agenda.domain.TrancheHoraire;
import io.vertigo.planning.agenda.services.fo.FoConsultationPlanningPlugin;

@NotDiscoverable
public class DbFoConsultationPlanningPlugin implements FoConsultationPlanningPlugin {

	@Inject
	private TrancheHoraireDAO trancheHoraireDAO;
	@Inject
	private AgendaPAO agendaPAO;

	@Override
	public DtList<TrancheHoraire> getTrancheHoraireDisponibles(final CritereTrancheHoraire critereTrancheHoraire) {
		return trancheHoraireDAO.getTrancheHorairesDisponibleByAgeIds(critereTrancheHoraire.getAgeIds(), critereTrancheHoraire.getPremierJour(), critereTrancheHoraire.getPremierJour().plusDays(6),
				Instant.now(),
				critereTrancheHoraire.getDateMin(), critereTrancheHoraire.getMinutesMin());
	}

	@Override
	public Optional<LocalDate> getDateDePremiereDisponibilite(final CritereTrancheHoraire critereTrancheHoraire) {
		return agendaPAO.getDatePremiereDisponibiliteByAgeIds(
				critereTrancheHoraire.getAgeIds(), critereTrancheHoraire.getPremierJour(), critereTrancheHoraire.getDateMax(), Instant.now(),
				critereTrancheHoraire.getDateMin(), critereTrancheHoraire.getMinutesMin());
	}

	@Override
	public Optional<LocalDate> getDateDeDernierePublication(final List<UID<Agenda>> ageUids) {
		return agendaPAO.getDateDernierePublicationByAgeIds(
				ageUids.stream().map(UID::getId).map(Long.class::cast).collect(Collectors.toList()),
				Instant.now());
	}

	@Override
	public Optional<PublicationRange> getPrecedentePublication(final List<UID<Agenda>> ageUids) {
		return agendaPAO.getPrecedentePublicationByAgeIds(
				ageUids.stream().map(UID::getId).map(Long.class::cast).collect(Collectors.toList()),
				Instant.now());
	}

	@Override
	public Optional<PublicationRange> getProchainePublication(final List<UID<Agenda>> ageUids) {
		return agendaPAO.getProchainePublicationByAgeIds(
				ageUids.stream().map(UID::getId).map(Long.class::cast).collect(Collectors.toList()),
				Instant.now());
	}

}
