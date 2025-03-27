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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.vertigo.core.lang.VUserException;
import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.planning.agenda.domain.Agenda;
import io.vertigo.planning.agenda.domain.AgendaDisplay;
import io.vertigo.planning.agenda.domain.AgendaDisplayRange;
import io.vertigo.planning.agenda.domain.CreationPlageHoraireForm;
import io.vertigo.planning.agenda.domain.DefaultPlageHoraire;
import io.vertigo.planning.agenda.domain.DuplicationSemaineForm;
import io.vertigo.planning.agenda.domain.PlageHoraire;
import io.vertigo.planning.agenda.domain.PlageHoraireDisplay;
import io.vertigo.planning.agenda.domain.PublicationTrancheHoraireForm;
import io.vertigo.planning.agenda.domain.TrancheHoraire;
import io.vertigo.planning.agenda.domain.TrancheHoraireDisplay;
import io.vertigo.planning.agenda.services.PlanningServices;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.argumentresolvers.ViewAttribute;
import io.vertigo.vega.webservice.validation.UiMessageStack;

public class AgendaControllerHelper implements Component {

	protected static final int NB_JOURS_DUPLICATE = 6;
	protected static final ViewContextKey<Integer> weekDaysNumberKey = ViewContextKey.of("weekDaysNumber");
	protected static final ViewContextKey<String> weekDaysKey = ViewContextKey.of("weekDays");
	protected static final ViewContextKey<Boolean> modeGuichetKey = ViewContextKey.of("modeGuichet");
	protected static final ViewContextKey<Boolean> modeTranchesHoraireKey = ViewContextKey.of("modeTranchesHoraire");

	public static final ViewContextKey<AgendaDisplayRange> agendaRangeKey = ViewContextKey.of("agendaRange");
	protected static final ViewContextKey<PlageHoraireDisplay> plagesHoraireKey = ViewContextKey.of("plagesHoraire");
	protected static final ViewContextKey<PlageHoraireDisplay> reservationOrphelinesKey = ViewContextKey.of("reservationOrphelines");

	protected static final ViewContextKey<TrancheHoraireDisplay> tranchesHoraireKey = ViewContextKey.of("tranchesHoraire");

	//creation plage
	protected static final ViewContextKey<String> agendaLabelKey = ViewContextKey.of("agendaLabel");
	public static final ViewContextKey<AgendaDisplay> agendasDisplayKey = ViewContextKey.of("agendasDisplay");
	protected static final ViewContextKey<DefaultPlageHoraire> defaultPlageHoraire = ViewContextKey.of("defaultPlageHoraire");
	protected static final ViewContextKey<CreationPlageHoraireForm> creationPlageHoraireFormKey = ViewContextKey.of("creationPlageHoraireForm");

	//publish plage
	protected static final ViewContextKey<PublicationTrancheHoraireForm> publicationTrancheHoraireFormKey = ViewContextKey.of("publicationTrancheHoraireForm");

	//detail plage
	protected static final ViewContextKey<PlageHoraireDisplay> plageHoraireDetailKey = ViewContextKey.of("plageHoraireDetail");
	protected static final ViewContextKey<TrancheHoraireDisplay> trancheHorairesDetailKey = ViewContextKey.of("trancheHorairesDetail");

	//duplication semaine
	protected static final ViewContextKey<DuplicationSemaineForm> duplicationSemaineFormKey = ViewContextKey.of("duplicationSemaineForm");

	@Inject
	private PlanningServices planningServices;

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
	public void initContextWithAgendaUids(final String agendaLabel, final ViewContext viewContext, final List<UID<Agenda>> ageUids, final Integer weekDaysNumber,
			final CreationPlageHoraireForm creationPlageHoraireForm,
			final DuplicationSemaineForm duplicationSemaineForm, final boolean modeGuichet, final boolean modeTranchesHoraire) {
		final var agendasDisplay = planningServices.getAgendasDisplay(ageUids);
		initContext(agendaLabel, viewContext, agendasDisplay, weekDaysNumber, creationPlageHoraireForm, duplicationSemaineForm, modeGuichet, modeTranchesHoraire);
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
	public void initContext(final String agendaLabel, final ViewContext viewContext, final DtList<AgendaDisplay> agendasDisplay, final Integer weekDaysNumber,
			final CreationPlageHoraireForm creationPlageHoraireForm,
			final DuplicationSemaineForm duplicationSemaineForm, final boolean modeGuichet, final boolean modeTranchesHoraire) {
		//---
		final var todayDate = LocalDate.now();
		viewContext.publishRef(modeGuichetKey, modeGuichet);
		viewContext.publishRef(modeTranchesHoraireKey, modeTranchesHoraire);
		//---
		final var agendaDisplayRange = new AgendaDisplayRange();
		final var ageUids = agendasDisplay.stream().map(a -> UID.of(Agenda.class, a.getAgeId())).toList();
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		agendaDisplayRange.setAgeIds(ageIds);
		agendaDisplayRange.setShowDays(weekDaysNumber);
		agendaDisplayRange.setMondayLock(true);

		//prépare le agendaDisplayRange, reload les creneaux et prepare la popin publish plage
		prepareContextAtDate(todayDate, agendaDisplayRange, viewContext);
		viewContext.publishRef(weekDaysNumberKey, weekDaysNumber);
		viewContext.publishRef(weekDaysKey, Arrays.toString(IntStream.concat(IntStream.range(1, weekDaysNumber), IntStream.of(weekDaysNumber == 7 ? 0 : weekDaysNumber)).toArray()));

		//pour popin creation plage
		viewContext.publishDto(creationPlageHoraireFormKey, creationPlageHoraireForm);
		final var defaultPlageHoraires = planningServices.getDefaultPlageHoraireByAgenda(ageUids, todayDate.minusMonths(1), todayDate.plusMonths(3));
		viewContext.publishDtList(defaultPlageHoraire, defaultPlageHoraires);
		viewContext.publishDtList(agendasDisplayKey, agendasDisplay);
		viewContext.publishRef(agendaLabelKey, agendaLabel);

		//pour popin detail plage avec la liste des tranches
		viewContext.publishDto(plageHoraireDetailKey, new PlageHoraireDisplay());
		viewContext.publishDtList(trancheHorairesDetailKey, new DtList<>(TrancheHoraireDisplay.class));

		//pour popin duplication
		viewContext.publishDto(duplicationSemaineFormKey, duplicationSemaineForm);
	}

	@PostMapping("/_reload")
	public ViewContext reload(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("plageHoraireDetail") final PlageHoraireDisplay plageHoraireDetail, final UiMessageStack uiMessageStack) {
		prepareContextAtDate(agenda.getShowDate(), agenda, viewContext);
		if (plageHoraireDetail.getPlhId() != null) {
			loadPlageHoraireDetail(viewContext, agenda, plageHoraireDetail.getPlhId(), uiMessageStack);
		}
		return viewContext;
	}

	@PostMapping("/_semainePrecedente")
	public ViewContext semainePrecedente(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda) {
		prepareContextAtDate(agenda.getShowDate().minusWeeks(1), agenda, viewContext);
		return viewContext;
	}

	@PostMapping("/_semaineSuivante")
	public ViewContext semaineSuivante(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda) {
		prepareContextAtDate(agenda.getShowDate().plusWeeks(1), agenda, viewContext);
		return viewContext;
	}

	@PostMapping("/_createPlage")
	public ViewContext createPlage(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("creationPlageHoraireForm") final CreationPlageHoraireForm creationPlageHoraireForm) {
		//---
		final List<UID<Agenda>> ageUids = agenda.getAgeIds().stream().map(ageId -> UID.of(Agenda.class, ageId)).toList();
		planningServices.createPlageHoraire(creationPlageHoraireForm, ageUids, agenda.getShowDays());
		prepareContextAtDate(creationPlageHoraireForm.getDateLocale(), agenda, viewContext);
		return viewContext;
	}

	@PostMapping("_prepareDuplicateSemaine")
	public ViewContext prepareDuplicateSemaine(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agendaRange,
			@ViewAttribute("duplicationSemaineForm") final DuplicationSemaineForm duplicationSemaineForm) {
		if (viewContext.getUiList(plagesHoraireKey).isEmpty()) {
			//erreur bloquante
			throw new VUserException("La semaine que vous souhaitez dupliquer n'a aucune plage horaire");
		}

		//par defaut
		//le modèle de la semaine à dupliquer permet de déterminer les jours travaillés, utilisés ci-dessous (ex : du lundi au vendredi)
		//la date de début correspond à la première date sans plages horaires sur le modèle de la semaine à dupliquer (en ne comptant que les jours travaillés)
		//la date de fin correspond à la date de début plus 6 jours (cela fait 1 semaine, car la date de fin est incluses)
		duplicationSemaineForm.setDateLocaleFromDebut(agendaRange.getFirstDate());
		duplicationSemaineForm.setDateLocaleFromFin(agendaRange.getLastDate());
		duplicationSemaineForm.setDateLocaleToDebut(planningServices.getPremierJourLibrePourDuplication(agendaRange));
		duplicationSemaineForm.setDateLocaleToFin(duplicationSemaineForm.getDateLocaleToDebut().plusDays(NB_JOURS_DUPLICATE)); //1 semaine entiere (borne incluse)
		viewContext.publishDto(duplicationSemaineFormKey, duplicationSemaineForm);

		return viewContext;
	}

	@PostMapping("/_duplicateSemaine")
	public ViewContext duplicateSemaine(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("duplicationSemaineForm") final DuplicationSemaineForm duplicationSemaineForm) {
		final List<UID<Agenda>> ageUids = agenda.getAgeIds().stream().map(ageId -> UID.of(Agenda.class, ageId)).toList();
		planningServices.duplicateSemaine(ageUids, duplicationSemaineForm, getDureeCreneauPerAgenda(ageUids, duplicationSemaineForm));
		prepareContextAtDate(duplicationSemaineForm.getDateLocaleToDebut(), agenda, viewContext);
		return viewContext;
	}

	protected Map<UID<Agenda>, Integer> getDureeCreneauPerAgenda(final List<UID<Agenda>> ageUids, final DuplicationSemaineForm duplicationSemaineForm) {
		//On applique la duplicationSemaineForm dureeCreneau a tous les agendas
		return ageUids.stream()
				.collect(Collectors.toMap(
						ageUid -> ageUid,
						ageUid -> duplicationSemaineForm.getDureeCreneau()));
	}

	@PostMapping("/_publishPlage")
	public ViewContext publishPlage(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("publicationTrancheHoraireForm") final PublicationTrancheHoraireForm publicationTrancheHoraireForm) {

		final List<UID<Agenda>> ageUids = agenda.getAgeIds().stream().map(ageId -> UID.of(Agenda.class, ageId)).toList();
		planningServices.publishPlageHorairesAndRelinkReservation(ageUids, publicationTrancheHoraireForm);

		prepareContextAtDate(publicationTrancheHoraireForm.getDateLocaleDebut(), agenda, viewContext);
		return viewContext;
	}

	@PostMapping("/_deletePlage")
	public ViewContext deletePlage(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda, @RequestParam("plhId") final Long plhId) {
		final UID<PlageHoraire> plhUid = UID.of(PlageHoraire.class, plhId);
		final List<UID<Agenda>> ageUids = agenda.getAgeIds().stream().map(ageId -> UID.of(Agenda.class, ageId)).toList();
		final var ageUid = planningServices.checkAuthorizedAgendaOfPlh(plhUid, ageUids);
		//---
		planningServices.deletePlageHoraireCascade(ageUid, UID.of(PlageHoraire.class, plhId));

		prepareContextAtDate(agenda.getShowDate(), agenda, viewContext);
		return viewContext;
	}

	@PostMapping("/_loadPlageHoraireDetail")
	public ViewContext loadPlageHoraireDetail(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda, @RequestParam("plhId") final Long plhId,
			final UiMessageStack uiMessageStack) {
		final UID<PlageHoraire> plhUid = UID.of(PlageHoraire.class, plhId);
		final List<UID<Agenda>> ageUids = agenda.getAgeIds().stream().map(ageId -> UID.of(Agenda.class, ageId)).toList();
		planningServices.checkAuthorizedAgendaOfPlh(plhUid, ageUids);
		//---
		final var plageHoraireDisplay = planningServices.getPlageHoraireDisplay(plhUid);
		final var agendasDisplay = viewContext.readDtList(agendasDisplayKey, uiMessageStack);
		final var agendaName = agendasDisplay.stream()
				.filter(a -> a.getAgeId().equals(plageHoraireDisplay.getAgeId()))
				.findFirst()
				.map(AgendaDisplay::getOverridedName).orElse(plageHoraireDisplay.getAgeNom());
		plageHoraireDisplay.setAgeNom(agendaName);
		final var trancheHoraire = planningServices.getTrancheHoraireDisplayByPlage(plhUid);

		viewContext.publishDto(plageHoraireDetailKey, plageHoraireDisplay);
		viewContext.publishDtList(trancheHorairesDetailKey, trancheHoraire);
		return viewContext;
	}

	@PostMapping("/_deleteTrancheHoraire")
	public ViewContext deleteTrancheHoraire(final ViewContext viewContext, @ViewAttribute("agendaRange") final AgendaDisplayRange agenda,
			@ViewAttribute("plageHoraireDetail") final PlageHoraireDisplay plageHoraireDetail, @RequestParam("trhId") final Long trhId, final UiMessageStack uiMessageStack) {
		final UID<Agenda> ageUid = UID.of(Agenda.class, plageHoraireDetail.getAgeId());
		final List<UID<Agenda>> ageUids = agenda.getAgeIds().stream().map(ageId -> UID.of(Agenda.class, ageId)).toList();
		planningServices.checkAuthorizedAgenda(ageUid, ageUids);
		//---
		final UID<TrancheHoraire> trhUid = UID.of(TrancheHoraire.class, trhId);
		planningServices.closeTrancheHoraire(ageUid, trhUid);
		return loadPlageHoraireDetail(viewContext, agenda, plageHoraireDetail.getPlhId(), uiMessageStack);
	}

	protected void prepareContextAtDate(final LocalDate showDate, final AgendaDisplayRange agenda, final ViewContext viewContext) {
		agenda.setShowDate(showDate);
		agenda.setFirstDate(toPreviousMonday(showDate));
		agenda.setLastDate(agenda.getFirstDate().plusDays(agenda.getShowDays() - 1L));
		viewContext.publishDto(agendaRangeKey, agenda);
		viewContext.publishDto(publicationTrancheHoraireFormKey, preparePublicationTrancheHoraireForm(agenda));

		final List<UID<Agenda>> ageUids = agenda.getAgeIds().stream().map(ageId -> UID.of(Agenda.class, ageId)).toList();
		reloadEvents(viewContext, ageUids, agenda.getFirstDate(), agenda.getLastDate());
	}

	private static PublicationTrancheHoraireForm preparePublicationTrancheHoraireForm(final AgendaDisplayRange agendaRange) {
		//par defaut non renseigné : il faut regarder l'historique pour proposer par exemple : la semaine prochaine à 8h00
		final LocalDate today = LocalDate.now();
		final var publicationTrancheHoraireForm = new PublicationTrancheHoraireForm();
		publicationTrancheHoraireForm.setDateLocaleDebut(today.isAfter(agendaRange.getFirstDate()) ? today : agendaRange.getFirstDate()); //par défaut aujourd'hui->samedi
		publicationTrancheHoraireForm.setDateLocaleFin(agendaRange.getLastDate());
		//publicationTrancheHoraireForm.setPublicationDateLocale(toPreviousMonday(LocalDate.now().plusWeeks(1)));
		//publicationTrancheHoraireForm.setPublicationMinutesDebut(8 * 60);
		publicationTrancheHoraireForm.setPublishNow(false); //par défaut on planifie
		return publicationTrancheHoraireForm;
	}

	private void reloadEvents(final ViewContext viewContext, final List<UID<Agenda>> ageUids, final LocalDate firstDate, final LocalDate lastDate) {
		if (viewContext.getBoolean(modeTranchesHoraireKey)) {
			final var tranchesHoraire = ageUids.isEmpty() ? new DtList<>(TrancheHoraireDisplay.class) : planningServices.getTrancheHoraireDisplayByDate(ageUids, firstDate, lastDate);
			viewContext.publishDtList(tranchesHoraireKey, tranchesHoraire);
			viewContext.publishDtList(plagesHoraireKey, new DtList<>(PlageHoraireDisplay.class));
		} else {
			final var plagesHoraire = ageUids.isEmpty() ? new DtList<>(PlageHoraireDisplay.class) : planningServices.getPlageHoraireDisplayByAgendas(ageUids, firstDate, lastDate);
			viewContext.publishDtList(plagesHoraireKey, plagesHoraire);
			viewContext.publishDtList(tranchesHoraireKey, new DtList<>(TrancheHoraireDisplay.class));
		}

		final var reservationOrphelines = ageUids.isEmpty() ? new DtList<>(PlageHoraireDisplay.class) : planningServices.getReservationOrphelines(ageUids, firstDate, lastDate);
		viewContext.publishDtList(reservationOrphelinesKey, reservationOrphelines);
	}

	private static LocalDate toPreviousMonday(final LocalDate localDate) {
		return localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	}

	protected void refreshAgendaDisplay(final ViewContext viewContext, final DtList<AgendaDisplay> agendasDisplay, final AgendaDisplayRange agendaDisplayRange) {
		final var ageUids = agendasDisplay.stream().map(a -> UID.of(Agenda.class, a.getAgeId())).toList();
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		agendaDisplayRange.setAgeIds(ageIds);
		prepareContextAtDate(agendaDisplayRange.getShowDate(), agendaDisplayRange, viewContext);
		viewContext.publishDtList(agendasDisplayKey, agendasDisplay);
	}
}
