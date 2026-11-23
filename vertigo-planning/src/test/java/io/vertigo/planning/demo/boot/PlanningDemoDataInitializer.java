/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2026, Vertigo.io, team@vertigo.io
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
package io.vertigo.planning.demo.boot;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import io.vertigo.core.node.component.ComponentInitializer;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.planning.agenda.domain.Agenda;
import io.vertigo.planning.agenda.domain.CreationPlageHoraireForm;
import io.vertigo.planning.agenda.domain.PlageHoraire;
import io.vertigo.planning.agenda.domain.PublicationTrancheHoraireForm;
import io.vertigo.planning.agenda.domain.TrancheHoraire;
import io.vertigo.planning.agenda.services.PlanningServices;
import io.vertigo.planning.demo.PlanningDemoData;

/**
 * Creates the data of the demo (the schema is created before, by PlanningDemoServer), with the services of the module, as a
 * user would : relative to the next week.
 * <ul>
 * <li>monday : Dr Martin and Dr Bernard 09:00-12:00, published : a plage assembling 2 agendas ; Dr Martin 14:00-17:00 not published ;</li>
 * <li>tuesday : Dr Martin 09:00-12:00 published, Dr Bernard 09:30-12:00 and Dr Petit 10:00-12:30 not published : 3 agendas, mixed states ;</li>
 * <li>wednesday : the 4 doctors 08:30-12:30 : 4 agendas, more than the 3 shown (maxMultiEventShow) ;</li>
 * <li>thursday : Dr Durand 14:00-18:00 published with 3 reservations ; Dr Bernard 09:00-12:00 not published with 1 reservation ;</li>
 * <li>friday : a plage contained in a plage of another agenda, created after it (Dr Petit 09:00-12:00, then Dr Durand 10:00-11:00)
 * and before it (Dr Durand 15:00-16:00, then Dr Bernard 14:00-17:00) ;</li>
 * <li>laboratoire : 07:30-10:30 every day, 4 counters, slots of 10 minutes, published on monday and tuesday with 5 reservations on monday ;
 * monday 14:00-16:00, 2 counters, not published ; tuesday 14:00-15:00 (a plage of one hour), 2 counters, not published ;</li>
 * <li>interviews : monday 09:00-12:00 (tranches of 45 minutes) and 14:00-17:00 (60 minutes), tuesday 09:00-12:00 (60 minutes),
 * thursday 14:00-17:00 (45 minutes), one counter, published on monday and tuesday with 2 interviews on monday morning.</li>
 * </ul>
 */
public final class PlanningDemoDataInitializer implements ComponentInitializer {
	@Inject
	private PlanningServices planningServices;

	/** {@inheritDoc} */
	@Override
	public void init() {
		createDemoData();
	}

	private void createDemoData() {
		final UID<Agenda> martin = planningServices.createAgenda(PlanningDemoData.DR_MARTIN).getUID();
		final UID<Agenda> bernard = planningServices.createAgenda(PlanningDemoData.DR_BERNARD).getUID();
		final UID<Agenda> petit = planningServices.createAgenda(PlanningDemoData.DR_PETIT).getUID();
		final UID<Agenda> durand = planningServices.createAgenda(PlanningDemoData.DR_DURAND).getUID();
		final UID<Agenda> laboratoire = planningServices.createAgenda(PlanningDemoData.LABORATOIRE).getUID();
		final UID<Agenda> entretiens = planningServices.createAgenda(PlanningDemoData.ENTRETIENS).getUID();

		final LocalDate monday = PlanningDemoData.demoWeekMonday();
		final LocalDate tuesday = monday.plusDays(1);
		final LocalDate wednesday = monday.plusDays(2);
		final LocalDate thursday = monday.plusDays(3);
		final LocalDate friday = monday.plusDays(4);

		// cabinet : one counter, slots of 15 minutes ; each plage created with its own agenda only (the conflict check covers the given agendas)
		createPlage(martin, monday, 9 * 60, 12 * 60, 1);
		createPlage(bernard, monday, 9 * 60, 12 * 60, 1);
		publish(List.of(martin, bernard), monday, monday);
		createPlage(martin, monday, 14 * 60, 17 * 60, 1);

		createPlage(martin, tuesday, 9 * 60, 12 * 60, 1);
		publish(List.of(martin), tuesday, tuesday);
		createPlage(bernard, tuesday, 9 * 60 + 30, 12 * 60, 1);
		createPlage(petit, tuesday, 10 * 60, 12 * 60 + 30, 1);

		createPlage(martin, wednesday, 8 * 60 + 30, 12 * 60 + 30, 1);
		createPlage(bernard, wednesday, 8 * 60 + 30, 12 * 60 + 30, 1);
		createPlage(petit, wednesday, 8 * 60 + 30, 12 * 60 + 30, 1);
		createPlage(durand, wednesday, 8 * 60 + 30, 12 * 60 + 30, 1);
		publish(List.of(martin, durand), wednesday, wednesday);

		final PlageHoraire durandThursday = createPlage(durand, thursday, 14 * 60, 18 * 60, 1);
		publish(List.of(durand), thursday, thursday);
		reserve(durandThursday, 3);
		reserve(createPlage(bernard, thursday, 9 * 60, 12 * 60, 1), 1);

		createPlage(petit, friday, 9 * 60, 12 * 60, 1);
		createPlage(durand, friday, 10 * 60, 11 * 60, 1);
		createPlage(durand, friday, 15 * 60, 16 * 60, 1);
		createPlage(bernard, friday, 14 * 60, 17 * 60, 1);

		// laboratoire : 4 counters, slots of 10 minutes
		final PlageHoraire laboratoireMonday = createPlage(laboratoire, monday, 7 * 60 + 30, 10 * 60 + 30, 4, 10);
		for (final LocalDate day : List.of(tuesday, wednesday, thursday, friday)) {
			createPlage(laboratoire, day, 7 * 60 + 30, 10 * 60 + 30, 4, 10);
		}
		publish(List.of(laboratoire), monday, tuesday);
		reserve(laboratoireMonday, 5);
		createPlage(laboratoire, monday, 14 * 60, 16 * 60, 2, 10);
		createPlage(laboratoire, tuesday, 14 * 60, 15 * 60, 2, 10);

		// interviews : one counter, tranches of 45 or 60 minutes
		final PlageHoraire entretiensMonday = createPlage(entretiens, monday, 9 * 60, 12 * 60, 1, 45);
		createPlage(entretiens, monday, 14 * 60, 17 * 60, 1, 60);
		createPlage(entretiens, tuesday, 9 * 60, 12 * 60, 1, 60);
		createPlage(entretiens, thursday, 14 * 60, 17 * 60, 1, 45);
		publish(List.of(entretiens), monday, tuesday);
		reserve(entretiensMonday, 2);
		PlanningDemoData.register(List.of(martin, bernard, petit, durand), laboratoire, entretiens);
	}

	private PlageHoraire createPlage(final UID<Agenda> agendaUid, final LocalDate day, final int minutesDebut, final int minutesFin, final int nbGuichet) {
		return createPlage(agendaUid, day, minutesDebut, minutesFin, nbGuichet, 15);
	}

	private PlageHoraire createPlage(final UID<Agenda> agendaUid, final LocalDate day, final int minutesDebut, final int minutesFin, final int nbGuichet, final int dureeCreneau) {
		final var form = new CreationPlageHoraireForm();
		form.setAgeId((Long) agendaUid.getId());
		form.setDateLocale(day);
		form.setMinutesDebut(minutesDebut);
		form.setMinutesFin(minutesFin);
		form.setNbGuichet(nbGuichet);
		form.setDureeCreneau(dureeCreneau);
		return planningServices.createPlageHoraire(form, List.of(agendaUid), 5);
	}

	private void publish(final List<UID<Agenda>> agendaUids, final LocalDate firstDay, final LocalDate lastDay) {
		final var form = new PublicationTrancheHoraireForm();
		form.setDateLocaleDebut(firstDay);
		form.setDateLocaleFin(lastDay);
		form.setPublishNow(true);
		planningServices.publishPlageHorairesAndRelinkReservation(agendaUids, form);
	}

	private void reserve(final PlageHoraire plageHoraire, final int count) {
		final var tranches = planningServices.getTrancheHoraireDisplayByPlage(plageHoraire.getUID());
		for (int i = 0; i < count; i++) {
			planningServices.reserverCreneauBO(UID.of(TrancheHoraire.class, tranches.get(i).getTrhId()), false);
		}
	}
}
