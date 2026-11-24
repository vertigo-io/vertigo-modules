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
package io.vertigo.planning.demo.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.planning.agenda.controllers.AbstractAgendaController;
import io.vertigo.planning.agenda.controllers.AgendaControllerHelper;
import io.vertigo.planning.agenda.domain.Agenda;
import io.vertigo.planning.agenda.domain.CreationPlageHoraireForm;
import io.vertigo.planning.agenda.domain.DuplicationSemaineForm;
import io.vertigo.planning.agenda.domain.InfoCalendrierDisplay;
import io.vertigo.planning.agenda.services.PlanningServices;
import io.vertigo.planning.demo.PlanningDemoData;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;

/**
 * An agenda page of the demo : the context of the module, opened on the week of the demo data.
 */
abstract class AbstractDemoAgendaController extends AbstractAgendaController {
	private static final int WEEK_DAYS_NUMBER = 5; // monday to friday
	private static final ViewContextKey<Boolean> canCreateReservationKey = ViewContextKey.of("canCreateReservation");

	@Inject
	private PlanningServices planningServices;

	protected final void initDemoContext(final ViewContext viewContext, final List<UID<Agenda>> agendaUids, final boolean modeGuichet, final boolean modeTranchesHoraire,
			final int nbGuichet, final int dureeCreneau) {
		final var creationPlageHoraireForm = new CreationPlageHoraireForm();
		creationPlageHoraireForm.setAgeId((Long) agendaUids.get(0).getId());
		creationPlageHoraireForm.setNbGuichet(nbGuichet);
		creationPlageHoraireForm.setDureeCreneau(dureeCreneau);
		final var duplicationSemaineForm = new DuplicationSemaineForm();
		duplicationSemaineForm.setDureeCreneau(dureeCreneau);
		initContext(viewContext, planningServices.getAgendasDisplay(agendaUids), WEEK_DAYS_NUMBER, creationPlageHoraireForm, duplicationSemaineForm, modeGuichet, modeTranchesHoraire);
		// read by vu:agenda-detail-plage, not published by initContext : the demo has no booking from the agenda
		viewContext.publishRef(canCreateReservationKey, false);
		// the demo data is on the next week
		final var agendaRange = viewContext.getUiObject(AgendaControllerHelper.agendaRangeKey).getServerSideObject();
		agendaControllerHelper.prepareContextAtDate(PlanningDemoData.demoWeekMonday(), agendaRange, viewContext);
	}

	/**
	 * @return the public holidays of the page (closed days), none by default
	 */
	protected List<LocalDate> holidays() {
		return List.of();
	}

	/** The infos of the calendar, as a project gives them : the public holidays of the page, closed. */
	@Override
	protected DtList<InfoCalendrierDisplay> getInfoCalendrierForRange(final ViewContext viewContext, final LocalDate startDate, final LocalDate endDate) {
		final DtList<InfoCalendrierDisplay> infos = new DtList<>(InfoCalendrierDisplay.class);
		for (final LocalDate holiday : holidays()) {
			if (!holiday.isBefore(startDate) && !holiday.isAfter(endDate)) {
				final var info = new InfoCalendrierDisplay();
				info.setDate(holiday);
				info.setLibelle("Jour férié");
				info.setInfobulle("Fermé");
				info.setHtmlStyle("background: #b71c1c; color: white");
				info.setSiFerme(true); // no duplication of plages on this day
				infos.add(info);
			}
		}
		return infos;
	}
}
