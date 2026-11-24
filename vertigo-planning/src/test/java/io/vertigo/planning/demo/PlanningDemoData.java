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
package io.vertigo.planning.demo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.lang.Assertion;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.planning.agenda.domain.Agenda;

/**
 * The agendas of the demo, created at startup by the data initializer.
 * The cabinet médical des Tilleuls : 4 doctors, one personal agenda each ; the laboratoire : one agenda with 4 counters (guichets) ;
 * the interviews : one agenda shown by tranches (mode tranches horaires).
 * The data is put on the next week (always in the future, whatever the day of the run), the pages open on that week.
 */
public final class PlanningDemoData {
	public static final String DR_MARTIN = "Dr Anne Martin";
	public static final String DR_BERNARD = "Dr Paul Bernard";
	public static final String DR_PETIT = "Dr Claire Petit";
	public static final String DR_DURAND = "Dr Louis Durand";
	public static final String LABORATOIRE = "Laboratoire, prises de sang";
	public static final String ENTRETIENS = "Entretiens de recrutement";

	// computed once : a run going past midnight from sunday to monday keeps the week of its data
	private static final LocalDate DEMO_WEEK_MONDAY = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

	private static final List<UID<Agenda>> cabinetAgendaUids = new ArrayList<>();
	private static UID<Agenda> laboratoireAgendaUid;
	private static UID<Agenda> entretiensAgendaUid;

	private PlanningDemoData() {
		//private
	}

	/**
	 * @return the monday of the week shown by the demo pages (the next week)
	 */
	public static LocalDate demoWeekMonday() {
		return DEMO_WEEK_MONDAY;
	}

	public static synchronized void register(final List<UID<Agenda>> cabinetAgendas, final UID<Agenda> laboratoireAgenda, final UID<Agenda> entretiensAgenda) {
		cabinetAgendaUids.clear();
		cabinetAgendaUids.addAll(cabinetAgendas);
		laboratoireAgendaUid = laboratoireAgenda;
		entretiensAgendaUid = entretiensAgenda;
	}

	/**
	 * @return the 4 agendas of the cabinet, Dr Martin first
	 */
	public static synchronized List<UID<Agenda>> cabinetAgendaUids() {
		Assertion.check().isFalse(cabinetAgendaUids.isEmpty(), "The demo data is not initialized");
		return List.copyOf(cabinetAgendaUids);
	}

	/**
	 * @return the agenda of the laboratoire
	 */
	public static synchronized UID<Agenda> laboratoireAgendaUid() {
		Assertion.check().isNotNull(laboratoireAgendaUid, "The demo data is not initialized");
		return laboratoireAgendaUid;
	}

	/**
	 * @return the agenda of the interviews
	 */
	public static synchronized UID<Agenda> entretiensAgendaUid() {
		Assertion.check().isNotNull(entretiensAgendaUid, "The demo data is not initialized");
		return entretiensAgendaUid;
	}
}
