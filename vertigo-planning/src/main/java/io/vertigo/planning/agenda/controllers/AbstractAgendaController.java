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
package io.vertigo.planning.agenda.controllers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.planning.agenda.domain.Agenda;
import io.vertigo.planning.agenda.domain.AgendaDisplay;
import io.vertigo.planning.agenda.domain.AgendaDisplayRange;
import io.vertigo.planning.agenda.domain.CreationPlageHoraireForm;
import io.vertigo.planning.agenda.domain.DuplicationSemaineForm;
import io.vertigo.planning.agenda.domain.PlageHoraireDisplay;
import io.vertigo.planning.agenda.domain.PublicationTrancheHoraireForm;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.impl.springmvc.argumentresolvers.ViewAttribute;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;
import io.vertigo.vega.webservice.validation.UiMessageStack;

public class AbstractAgendaController extends AbstractVSpringMvcController {

	@Inject
	private AgendaControllerHelper agendaControllerHelper;

	/**
	 * Init context for agenda page.
	 * @param viewContext ViewContext
	 * @param ageUids list of agenda UIDs
	 * @param weekDaysNumber number of days
	 * @param creationPlageHoraireForm initialized form for creating
	 * @param duplicationSemaineForm initialized form for duplicating
	 * @param modeGuichet true if mode guichet
	 * @param modeTranchesHoraire true if mode tranche horaire (don't show plages)
	 * @Deprecated Send agendasDisplay instead of ageUids
	 */
	@Deprecated
	public void initContextWithAgendaUids(final ViewContext viewContext, final List<UID<Agenda>> ageUids, final Integer weekDaysNumber, final CreationPlageHoraireForm creationPlageHoraireForm,
			final DuplicationSemaineForm duplicationSemaineForm, final boolean modeGuichet, final boolean modeTranchesHoraire) {
		agendaControllerHelper.initContextWithAgendaUids(getAgendaLabel(), viewContext, ageUids, weekDaysNumber, creationPlageHoraireForm, duplicationSemaineForm, modeGuichet, modeTranchesHoraire);
	}

	/**
	 * Init context for agenda page.
	 * @param viewContext ViewContext
	 * @param ageUids list of agenda UIDs
	 * @param weekDaysNumber number of days
	 * @param creationPlageHoraireForm initialized form for creating
	 * @param duplicationSemaineForm initialized form for duplicating
	 * @param modeGuichet true if mode guichet
	 * @param modeTranchesHoraire true if mode tranche horaire (don't show plages)
	 */
	public void initContext(final ViewContext viewContext, final DtList<AgendaDisplay> agendasDisplay, final Integer weekDaysNumber, final CreationPlageHoraireForm creationPlageHoraireForm,
			final DuplicationSemaineForm duplicationSemaineForm, final boolean modeGuichet, final boolean modeTranchesHoraire) {
		//---
		agendaControllerHelper.initContext(getAgendaLabel(), viewContext, agendasDisplay, weekDaysNumber, creationPlageHoraireForm, duplicationSemaineForm, modeGuichet, modeTranchesHoraire);
	}

	@PostMapping("/_reload")
	public ViewContext reload(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("plageHoraireDetail") final PlageHoraireDisplay plageHoraireDetail, final UiMessageStack uiMessageStack) {
		return agendaControllerHelper.reload(viewContext, agenda, plageHoraireDetail, uiMessageStack);
	}

	@PostMapping("/_semainePrecedente")
	public ViewContext semainePrecedente(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda) {
		return agendaControllerHelper.semainePrecedente(viewContext, agenda);
	}

	@PostMapping("/_semaineSuivante")
	public ViewContext semaineSuivante(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda) {
		return agendaControllerHelper.semaineSuivante(viewContext, agenda);
	}

	@PostMapping("/_createPlage")
	public ViewContext createPlage(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("creationPlageHoraireForm") final CreationPlageHoraireForm creationPlageHoraireForm) {
		//---
		return agendaControllerHelper.createPlage(viewContext, agenda, creationPlageHoraireForm);
	}

	@PostMapping("_prepareDuplicateSemaine")
	public ViewContext prepareDuplicateSemaine(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agendaRange,
			@ViewAttribute("duplicationSemaineForm") final DuplicationSemaineForm duplicationSemaineForm) {
		return agendaControllerHelper.prepareDuplicateSemaine(viewContext, agendaRange, duplicationSemaineForm);
	}

	@PostMapping("/_duplicateSemaine")
	public ViewContext duplicateSemaine(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("duplicationSemaineForm") final DuplicationSemaineForm duplicationSemaineForm) {
		return agendaControllerHelper.duplicateSemaine(viewContext, agenda, duplicationSemaineForm);
	}

	@PostMapping("/_publishPlage")
	public ViewContext publishPlage(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("publicationTrancheHoraireForm") final PublicationTrancheHoraireForm publicationTrancheHoraireForm) {
		return agendaControllerHelper.publishPlage(viewContext, agenda, publicationTrancheHoraireForm);
	}

	@PostMapping("/_deletePlage")
	public ViewContext deletePlage(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda, @RequestParam("plhId") final Long plhId) {
		return agendaControllerHelper.deletePlage(viewContext, agenda, plhId);
	}

	@PostMapping("/_loadPlageHoraireDetail")
	public ViewContext loadPlageHoraireDetail(final ViewContext viewContext,
			@ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@RequestParam("plhId") final Long plhId, final UiMessageStack uiMessageStack) {
		return agendaControllerHelper.loadPlageHoraireDetail(viewContext, agenda, plhId, uiMessageStack);
	}

	@PostMapping("/_deleteTrancheHoraire")
	public ViewContext deleteTrancheHoraire(final ViewContext viewContext,
			@ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("plageHoraireDetail") final PlageHoraireDisplay plageHoraireDetail,
			@RequestParam("trhId") final Long trhId, final UiMessageStack uiMessageStack) {
		return agendaControllerHelper.deleteTrancheHoraire(viewContext, agenda, plageHoraireDetail, trhId, uiMessageStack);
	}

	/**
	 * Overridable label use for agenda selection.
	 * @return label
	 */
	protected String getAgendaLabel() {
		return "Agenda";
	}

}
