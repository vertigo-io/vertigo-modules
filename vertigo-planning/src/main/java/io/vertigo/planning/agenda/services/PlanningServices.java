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
package io.vertigo.planning.agenda.services;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import io.vertigo.account.authorization.VSecurityException;
import io.vertigo.commons.transaction.Transactional;
import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.commons.transaction.VTransactionWritable;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.lang.VUserException;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.criteria.Criterions;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.DtListState;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.data.util.VCollectors;
import io.vertigo.planning.agenda.AgendaPAO;
import io.vertigo.planning.agenda.dao.AgendaDAO;
import io.vertigo.planning.agenda.dao.CreneauDAO;
import io.vertigo.planning.agenda.dao.PlageHoraireDAO;
import io.vertigo.planning.agenda.dao.ReservationCreneauDAO;
import io.vertigo.planning.agenda.dao.TrancheHoraireDAO;
import io.vertigo.planning.agenda.domain.AffectionReservation;
import io.vertigo.planning.agenda.domain.Agenda;
import io.vertigo.planning.agenda.domain.AgendaDisplay;
import io.vertigo.planning.agenda.domain.AgendaDisplayRange;
import io.vertigo.planning.agenda.domain.CreationPlageHoraireForm;
import io.vertigo.planning.agenda.domain.Creneau;
import io.vertigo.planning.agenda.domain.DateDisponibleDisplay;
import io.vertigo.planning.agenda.domain.DefaultPlageHoraire;
import io.vertigo.planning.agenda.domain.DuplicationSemaineForm;
import io.vertigo.planning.agenda.domain.PlageHoraire;
import io.vertigo.planning.agenda.domain.PlageHoraireDisplay;
import io.vertigo.planning.agenda.domain.PublicationTrancheHoraireForm;
import io.vertigo.planning.agenda.domain.ReservationCreneau;
import io.vertigo.planning.agenda.domain.TrancheHoraire;
import io.vertigo.planning.agenda.domain.TrancheHoraireDisplay;
import io.vertigo.planning.domain.DtDefinitions.AgendaFields;
import io.vertigo.planning.domain.DtDefinitions.CreationPlageHoraireFormFields;
import io.vertigo.planning.domain.DtDefinitions.CreneauFields;
import io.vertigo.planning.domain.DtDefinitions.DuplicationSemaineFormFields;
import io.vertigo.planning.domain.DtDefinitions.PlageHoraireFields;
import io.vertigo.planning.domain.DtDefinitions.PublicationTrancheHoraireFormFields;
import io.vertigo.planning.domain.DtDefinitions.TrancheHoraireFields;
import io.vertigo.vega.webservice.validation.UiErrorBuilder;

@Transactional
public class PlanningServices implements Component {

	@Inject
	private AgendaDAO agendaDAO;
	@Inject
	private PlageHoraireDAO plageHoraireDAO;
	@Inject
	private TrancheHoraireDAO trancheHoraireDAO;
	@Inject
	private CreneauDAO creneauDAO;
	@Inject
	private ReservationCreneauDAO reservationCreneauDAO;
	@Inject
	private AgendaPAO agendaPAO;
	@Inject
	private VTransactionManager transactionManager;
	@Inject
	private PlanningServicesConfig planningServicesConfig;

	public Agenda createAgenda(final String name) {
		Assertion.check().isNotBlank(name, "A agenda must have a name");
		//---
		final var agenda = new Agenda();
		agenda.setNom(name);
		final var createdAgenda = agendaDAO.create(agenda);
		return createdAgenda;
	}

	public PlageHoraire createPlageHoraire(final CreationPlageHoraireForm creationPlageHoraireForm, final List<UID<Agenda>> authorizedAgeUids, final int maxWeekDaysNumber) {
		/** Contrôles User */
		final var uiErrorBuilder = new UiErrorBuilder();
		final var decalageJours = ChronoUnit.DAYS.between(LocalDate.now(), creationPlageHoraireForm.getDateLocale());
		if (Math.abs(decalageJours) > planningServicesConfig.getCreateMaxDaysFromNow()) {
			uiErrorBuilder.addError(creationPlageHoraireForm, CreationPlageHoraireFormFields.dateLocale,
					LocaleMessageText.of("La date de la plage horaire doit être à moins de {0}", getDaysLimitLabel(planningServicesConfig.getCreateMaxDaysFromNow(), false)));
		}
		if (creationPlageHoraireForm.getNbGuichet() <= 0 || creationPlageHoraireForm.getNbGuichet() > planningServicesConfig.getCreateMaxNbGuichet()) {
			uiErrorBuilder.addError(creationPlageHoraireForm, CreationPlageHoraireFormFields.nbGuichet,
					LocaleMessageText.of("Le nombre de guichets doit être un nombre compris entre 1 et {0}", planningServicesConfig.getCreateMaxNbGuichet()));
		}

		if (creationPlageHoraireForm.getMinutesDebut() < planningServicesConfig.getCreatePlageHeureMin()) {
			uiErrorBuilder.addError(creationPlageHoraireForm, CreationPlageHoraireFormFields.minutesDebut,
					LocaleMessageText.of("La plage horaire doit débuter au plus tôt à {0}", getMinutesLabel(planningServicesConfig.getCreatePlageHeureMin())));
		}
		if (creationPlageHoraireForm.getMinutesFin() > planningServicesConfig.getCreatePlageHeureMax()) {
			uiErrorBuilder.addError(creationPlageHoraireForm, CreationPlageHoraireFormFields.minutesFin,
					LocaleMessageText.of("La plage horaire doit finir au plus tard à {0}", getMinutesLabel(planningServicesConfig.getCreatePlageHeureMax())));
		}

		final var dureePlageMinute = creationPlageHoraireForm.getMinutesFin() - creationPlageHoraireForm.getMinutesDebut();
		if (dureePlageMinute <= 0) {
			uiErrorBuilder.addError(creationPlageHoraireForm, CreationPlageHoraireFormFields.minutesFin,
					LocaleMessageText.of("L'heure de fin de la plage horaire doit être postérieur à l'heure de début"));
		} else if (dureePlageMinute < planningServicesConfig.getCreateMinDureePlageMinute()) {
			uiErrorBuilder.addError(creationPlageHoraireForm, CreationPlageHoraireFormFields.minutesDebut,
					LocaleMessageText.of("La plage horaire doit avoir une durée d'au moins {0} minutes", planningServicesConfig.getCreateMinDureePlageMinute()));
		} else if (dureePlageMinute > planningServicesConfig.getCreateMaxDureePlageHeure() * 60) {
			uiErrorBuilder.addError(creationPlageHoraireForm, CreationPlageHoraireFormFields.minutesDebut,
					LocaleMessageText.of("La plage horaire doit avoir une durée inférieure à {0} heures", planningServicesConfig.getCreateMaxDureePlageHeure()));
		}

		if (creationPlageHoraireForm.getDateLocale().getDayOfWeek().getValue() > maxWeekDaysNumber) {
			uiErrorBuilder.addError(creationPlageHoraireForm, CreationPlageHoraireFormFields.dateLocale,
					LocaleMessageText.of("Il n'est pas possible de créer une plage sur ce jour de la semaine"));
		}
		//---

		final var ageId = checkAuthorizedAgenda(UID.of(Agenda.class, creationPlageHoraireForm.getAgeId()), authorizedAgeUids);
		final var agenda = agendaDAO.readOneForUpdate(ageId); //ForUpdate pour éviter les doublons

		final var authorizedAgeIds = authorizedAgeUids.stream().map(UID::getId).map(Long.class::cast).toList();
		final var firstPlageConflict = plageHoraireDAO.getExistsConflictingPlageHoraire(
				authorizedAgeIds,
				creationPlageHoraireForm.getDateLocale(),
				creationPlageHoraireForm.getMinutesDebut(),
				creationPlageHoraireForm.getMinutesFin());
		if (firstPlageConflict.isPresent()) {
			//TODO : libelle de la plage
			uiErrorBuilder.addError(creationPlageHoraireForm, CreationPlageHoraireFormFields.minutesDebut,
					LocaleMessageText.of("La plage horaire est en conflit avec une autre"));
		}

		uiErrorBuilder.throwUserExceptionIfErrors();
		/*****/

		final var plageHoraire = new PlageHoraire();
		plageHoraire.agenda().setUID(agenda.getUID());
		plageHoraire.setDateLocale(creationPlageHoraireForm.getDateLocale());
		plageHoraire.setMinutesDebut(creationPlageHoraireForm.getMinutesDebut());
		plageHoraire.setMinutesFin(creationPlageHoraireForm.getMinutesFin());
		plageHoraire.setNbGuichet(creationPlageHoraireForm.getNbGuichet());
		plageHoraireDAO.save(plageHoraire);

		final var trancheHoraires = createTrancheHoraires(plageHoraire, creationPlageHoraireForm.getDureeCreneau());
		trancheHoraireDAO.batchInsertTrancheHoraire(trancheHoraires);
		// On ne crée pas les créneaux : seulement à la publication

		return plageHoraire;
	}

	private static DtList<TrancheHoraire> createTrancheHoraires(final PlageHoraire plageHoraire, final int dureeTrancheMinute) {
		return createTrancheHoraires(plageHoraire, dureeTrancheMinute, Collections.emptyList());
	}

	private static DtList<TrancheHoraire> createTrancheHoraires(final PlageHoraire plageHoraire, final int dureeTrancheMinute, final List<TrancheHoraire> closedTranchesHoraires) {
		final var trancheHoraires = new DtList<>(TrancheHoraire.class);
		for (int i = plageHoraire.getMinutesDebut(); i < plageHoraire.getMinutesFin(); i += dureeTrancheMinute) {
			boolean isClosed = false;
			for (final TrancheHoraire closedTrancheHoraire : closedTranchesHoraires) {
				if (i < closedTrancheHoraire.getMinutesFin() && i + dureeTrancheMinute > closedTrancheHoraire.getMinutesDebut()) {
					isClosed = true;
					break;
				}
			}
			trancheHoraires.add(createTrancheHoraire(plageHoraire, i, dureeTrancheMinute, isClosed ? 0 : plageHoraire.getNbGuichet()));
		}
		return trancheHoraires;
	}

	private static TrancheHoraire createTrancheHoraire(final PlageHoraire plageHoraire, final int startMinuteOfDay, final int dureeTrancheMinute, final int nbGuichet) {
		final var trancheHoraire = new TrancheHoraire();
		trancheHoraire.plageHoraire().setUID(plageHoraire.getUID());
		trancheHoraire.agenda().setUID(plageHoraire.agenda().getUID());
		trancheHoraire.setDateLocale(plageHoraire.getDateLocale());
		trancheHoraire.setMinutesDebut(startMinuteOfDay);
		trancheHoraire.setMinutesFin(startMinuteOfDay + dureeTrancheMinute);
		trancheHoraire.setNbGuichet(nbGuichet);
		return trancheHoraire;
	}

	public void duplicateSemaine(final List<UID<Agenda>> ageUids, final DuplicationSemaineForm duplicationSemaineForm, final Map<UID<Agenda>, Integer> dureeTranchePerAgenda) {
		//security filter
		final var uiErrorBuilder = new UiErrorBuilder();
		uiErrorBuilder.checkFieldDateAfterOrEquals(duplicationSemaineForm, DuplicationSemaineFormFields.dateLocaleFromDebut, DuplicationSemaineFormFields.dateLocaleFromFin,
				LocaleMessageText.of("La date de fin de la selection à copier doit être postérieur à la date de début"));

		uiErrorBuilder.checkFieldDateAfterOrEquals(duplicationSemaineForm, DuplicationSemaineFormFields.dateLocaleToDebut, DuplicationSemaineFormFields.dateLocaleToFin,
				LocaleMessageText.of("La date de fin de la plage de copie doit être postérieur à la date de début"));

		final var dureeDuplicationJours = ChronoUnit.DAYS.between(duplicationSemaineForm.getDateLocaleToDebut(), duplicationSemaineForm.getDateLocaleToFin());
		if (dureeDuplicationJours > planningServicesConfig.getDuplicateMaxDaysPeriode()) {
			uiErrorBuilder.addError(duplicationSemaineForm, DuplicationSemaineFormFields.dateLocaleToFin,
					LocaleMessageText.of("Vous ne pouvez pas dupliquer une semaine sur plus de {0}", getDaysLimitLabel(planningServicesConfig.getDuplicateMaxDaysPeriode(), false)));
		}

		final var decalageJours = ChronoUnit.DAYS.between(LocalDate.now(), duplicationSemaineForm.getDateLocaleToDebut());
		if (decalageJours > planningServicesConfig.getDuplicateMaxDaysFromNow()) { //abs : on autorise la publication de creneau passé
			uiErrorBuilder.addError(duplicationSemaineForm, DuplicationSemaineFormFields.dateLocaleToDebut,
					LocaleMessageText.of("Vous ne pouvez pas dupliquer à plus de {0}", getDaysLimitLabel(planningServicesConfig.getDuplicateMaxDaysFromNow(), false)));
		} else if (Math.abs(decalageJours) > planningServicesConfig.getDuplicateMaxDaysFromNow()) { //abs : on autorise la publication de creneau passé
			uiErrorBuilder.addError(duplicationSemaineForm, DuplicationSemaineFormFields.dateLocaleToDebut,
					LocaleMessageText.of("Vous ne pouvez pas dupliquer à plus de {0}", getDaysLimitLabel(planningServicesConfig.getDuplicateMaxDaysFromNow(), false)));
		}

		if (duplicationSemaineForm.getDateLocaleToDebut().isBefore(LocalDate.now())) {
			uiErrorBuilder.addError(duplicationSemaineForm, DuplicationSemaineFormFields.dateLocaleToDebut,
					LocaleMessageText.of("Vous ne pouvez pas dupliquer avant la date du jour"));
		} else if (duplicationSemaineForm.getDateLocaleToDebut().isBefore(duplicationSemaineForm.getDateLocaleFromFin())) {
			uiErrorBuilder.addError(duplicationSemaineForm, DuplicationSemaineFormFields.dateLocaleToDebut,
					LocaleMessageText.of("Vous ne pouvez pas dupliquer avant la semaine utilisée comme modèle"));
		}

		uiErrorBuilder.throwUserExceptionIfErrors();
		/*****/

		//on lock tous les agendas existants concernés : Attention ça peut locker la table !!
		ageUids.forEach(ageUid -> {
			if ((Long) ageUid.getId() > 0L) {
				agendaDAO.readOneForUpdate(ageUid);
			}
		}); //ForUpdate pour éviter les doublons
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		final var ageIdsArray = ageIds.toArray(Serializable[]::new);
		final var plageHorairesFrom = plageHoraireDAO.findAll(
				Criterions.in(PlageHoraireFields.ageId, ageIdsArray)
						.and(Criterions.isGreaterThanOrEqualTo(PlageHoraireFields.dateLocale, duplicationSemaineForm.getDateLocaleFromDebut()))
						.and(Criterions.isLessThanOrEqualTo(PlageHoraireFields.dateLocale, duplicationSemaineForm.getDateLocaleFromFin())),
				DtListState.of(null, 0, PlageHoraireFields.dateLocale.name(), false));
		if (plageHorairesFrom.isEmpty()) {
			//erreur bloquante
			throw new VUserException("La semaine que vous souhaitez dupliquer n'a aucune plage horaire");
		}
		final var closedTranchesHorairesFrom = trancheHoraireDAO.getTrancheHorairesFermeesByAgeIds(ageIds,
				duplicationSemaineForm.getDateLocaleFromDebut(), duplicationSemaineForm.getDateLocaleFromFin());

		final var previousPlageHorairesTo = plageHoraireDAO.findAll(
				Criterions.in(PlageHoraireFields.ageId, ageIdsArray)
						.and(Criterions.isGreaterThanOrEqualTo(PlageHoraireFields.dateLocale, duplicationSemaineForm.getDateLocaleToDebut()))
						.and(Criterions.isLessThanOrEqualTo(PlageHoraireFields.dateLocale, duplicationSemaineForm.getDateLocaleToFin())),
				DtListState.of(null, 0, PlageHoraireFields.dateLocale.name(), false));

		final Map<DayOfWeek, List<PlageHoraire>> mapPlagesHorairesFromPerDayOfWeek = plageHorairesFrom.stream()
				.collect(Collectors.groupingBy(plh -> plh.getDateLocale().getDayOfWeek()));

		final Map<LocalDate, List<PlageHoraire>> mapPreviousPlagesHorairesToPerLocalDate = previousPlageHorairesTo.stream()
				.collect(Collectors.groupingBy((Function<? super PlageHoraire, ? extends LocalDate>) PlageHoraire::getDateLocale));

		final Map<DayOfWeek, List<TrancheHoraire>> mapClosedTranchesHorairesFromPerDayOfWeek = closedTranchesHorairesFrom.stream()
				.collect(Collectors.groupingBy(trh -> trh.getDateLocale().getDayOfWeek()));

		final var plageHorairesToCreate = new DtList<>(PlageHoraire.class);
		final var trancheHoraires = new DtList<>(TrancheHoraire.class);
		//final int dureeTrancheMinute = duplicationSemaineForm.getDureeCreneau();
		for (var d = 0; d < dureeDuplicationJours + 1; ++d) { //+1 => date de fin incluse
			final var currentCopyDate = duplicationSemaineForm.getDateLocaleToDebut().plusDays(d);
			final var plageHorairesCopyFrom = mapPlagesHorairesFromPerDayOfWeek.get(currentCopyDate.getDayOfWeek());
			if (plageHorairesCopyFrom == null || plageHorairesCopyFrom.isEmpty()) {
				continue;
			}

			final var plageHorairesPreviousTo = mapPreviousPlagesHorairesToPerLocalDate.get(currentCopyDate);
			for (final PlageHoraire plageHoraireFrom : plageHorairesCopyFrom) {
				//on vérifie qu'il n'y a pas de conflit
				if (plageHorairesPreviousTo != null
						&& !plageHorairesPreviousTo.isEmpty()
						&& conflictPlageHoraire(plageHoraireFrom, plageHorairesPreviousTo)) {
					continue;
				}
				//sinon on crée la plage
				final var plageHoraire = new PlageHoraire();
				plageHoraire.agenda().setUID(plageHoraireFrom.agenda().getUID());
				plageHoraire.setDateLocale(currentCopyDate);
				plageHoraire.setMinutesDebut(plageHoraireFrom.getMinutesDebut());
				plageHoraire.setMinutesFin(plageHoraireFrom.getMinutesFin());
				plageHoraire.setNbGuichet(plageHoraireFrom.getNbGuichet());
				plageHorairesToCreate.add(plageHoraire);

				//(les tranches seront mis à jour après l'insert pour pouvoir récupérer la PK générée)
			}
		}
		//le batch ne marche pas car l'id n'est pas setté
		//on le fait quand meme en dernier pour locker moins longtemps
		//Creation des tranches horaires associées
		for (final PlageHoraire plageHoraire : plageHorairesToCreate) {
			plageHoraireDAO.save(plageHoraire);
			//cette fois l'id de la plage existe et peut être associée dans la FK des tranches
			//RDV-351 : il faut vérifier les tranches supprimées
			final Integer dureeCreneauAgenda = dureeTranchePerAgenda.get(plageHoraire.agenda().getUID());
			Assertion.check().isNotNull(dureeCreneauAgenda, "La durée de créneau n'est pas définie pour l'agenda {0}", plageHoraire.agenda().getUID());
			trancheHoraires.addAll(createTrancheHoraires(plageHoraire, dureeCreneauAgenda,
					mapClosedTranchesHorairesFromPerDayOfWeek.getOrDefault(plageHoraire.getDateLocale().getDayOfWeek(), Collections.emptyList())));
		}
		trancheHoraireDAO.batchInsertTrancheHoraire(trancheHoraires);
	}

	private static boolean conflictPlageHoraire(final PlageHoraire plageHoraireFrom, final List<PlageHoraire> plhsPreviousTo) {
		for (final PlageHoraire plhPreviousTo : plhsPreviousTo) {
			Assertion.check().isTrue(plageHoraireFrom.getAgeId().equals(plhPreviousTo.getAgeId()), "Les plages comparées ne sont pas sur le même agenda");
			if (plageHoraireFrom.getMinutesDebut() < plhPreviousTo.getMinutesFin()
					&& plageHoraireFrom.getMinutesFin() > plhPreviousTo.getMinutesDebut()) {
				return true;
			}
		}
		return false;
	}

	public void deletePlageHoraireCascade(final UID<Agenda> agendaUid, final UID<PlageHoraire> plageHoraireUid) {
		Assertion.check().isNotNull(agendaUid)
				.isNotNull(plageHoraireUid);
		//--
		agendaDAO.readOneForUpdate(agendaUid); //ForUpdate pour éviter les doublons
		agendaPAO.deletePlageHoraireCascadeByPlhId((Long) plageHoraireUid.getId());
	}

	public void closeTrancheHoraire(final UID<Agenda> agendaUid, final UID<TrancheHoraire> trancheHoraireUid) {
		Assertion.check().isNotNull(agendaUid)
				.isNotNull(trancheHoraireUid);
		//--
		agendaDAO.readOneForUpdate(agendaUid); //ForUpdate pour éviter les doublons
		agendaPAO.closeTrancheHoraireByTrhId(List.of((Long) trancheHoraireUid.getId()));
	}

	public Agenda getAgenda(final UID<Agenda> ageUid) {
		Assertion.check().isNotNull(ageUid);
		//---
		return agendaDAO.get(ageUid);
	}

	/*public void saveAgenda(final Agenda agenda) {
		Assertion.check().isNotNull(agenda.getAgeId());
		//---
		agendaDAO.save(agenda);
	}*/

	public DtList<Agenda> getAgendas(final List<UID<Agenda>> ageUids) {
		Assertion.check()
				.isNotNull(ageUids)
				.isTrue(ageUids.size() > 0, "Aucun agenda à charger")
				.isTrue(ageUids.size() <= PlanningServicesConfig.MAX_AGENDA_TO_LOAD, "Too much agenda to load, max {}", PlanningServicesConfig.MAX_AGENDA_TO_LOAD);
		//---
		final Serializable[] ageIds = ageUids.stream().map(UID::getId).toArray(Serializable[]::new);
		return agendaDAO.findAll(Criterions.in(AgendaFields.ageId, ageIds), DtListState.defaultOf(Agenda.class));
	}

	public DtList<AgendaDisplay> getAgendasDisplay(final List<UID<Agenda>> ageUids) {
		final var agendas = getAgendas(ageUids);
		return agendas.stream()
				.map(agenda -> {
					final var agendaDisplay = new AgendaDisplay();
					agendaDisplay.setAgeId(agenda.getAgeId());
					agendaDisplay.setName(agenda.getNom());
					return agendaDisplay;
				})
				.collect(VCollectors.toDtList(AgendaDisplay.class));
	}

	public DtList<PlageHoraireDisplay> getPlageHoraireDisplayByAgendas(final List<UID<Agenda>> ageUids, final LocalDate firstDate, final LocalDate lastDate) {
		Assertion.check().isNotNull(ageUids)
				.isNotNull(firstDate)
				.isNotNull(lastDate)
				.isTrue(ageUids.size() > 0, "Aucun agenda à charger")
				.isTrue(ageUids.size() <= PlanningServicesConfig.MAX_AGENDA_TO_LOAD, "Too much agenda to load, max {}", PlanningServicesConfig.MAX_AGENDA_TO_LOAD);
		//--
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		return agendaPAO.getPlageHoraireDisplayByAgeIds(ageIds, firstDate, lastDate, Instant.now());
	}

	/**
	 * Détermine les plages horaires par défaut d'un agenda.
	 * Pour l'instant en dur, plus tard pourrait analyser l'existant.
	 * @param agendaUid uid de l'agenda
	 * @param minusMonths Nombre de mois à analyser
	 * @param firstDate Première date de la période à simuler
	 * @return Liste des plages horaires par défaut
	 */
	public DtList<DefaultPlageHoraire> getDefaultPlageHoraireByAgenda(final List<UID<Agenda>> ageUids, final LocalDate minusMonths, final LocalDate firstDate) {

		//default
		final DtList<DefaultPlageHoraire> defaultPlageHoraires = new DtList<>(DefaultPlageHoraire.class);

		//A terme : regarder les creneaux du passé pour reproduire le pattern.
		//pour le moment valeur empirique
		defaultPlageHoraires.add(newDefaultPlageHoraire(1, 8, 30, 12, 0, 1));
		defaultPlageHoraires.add(newDefaultPlageHoraire(1, 14, 0, 16, 30, 1));
		defaultPlageHoraires.add(newDefaultPlageHoraire(2, 8, 30, 12, 0, 1));
		defaultPlageHoraires.add(newDefaultPlageHoraire(2, 14, 0, 16, 30, 1));
		defaultPlageHoraires.add(newDefaultPlageHoraire(3, 8, 30, 12, 0, 1));
		defaultPlageHoraires.add(newDefaultPlageHoraire(3, 14, 0, 16, 30, 1));
		defaultPlageHoraires.add(newDefaultPlageHoraire(4, 8, 30, 12, 0, 1));
		defaultPlageHoraires.add(newDefaultPlageHoraire(4, 14, 0, 16, 30, 1));
		defaultPlageHoraires.add(newDefaultPlageHoraire(5, 8, 30, 12, 0, 1));
		defaultPlageHoraires.add(newDefaultPlageHoraire(5, 14, 0, 16, 30, 1));

		return defaultPlageHoraires;
	}

	private static DefaultPlageHoraire newDefaultPlageHoraire(final int jour, final int hourDebut, final int minDebut, final int hourFin, final int minFin, final int nbGuichet) {
		DefaultPlageHoraire defaultPlageHoraire;
		defaultPlageHoraire = new DefaultPlageHoraire();
		defaultPlageHoraire.setJourDeSemaine(jour);
		defaultPlageHoraire.setMinutesDebut(hourDebut * 60 + minDebut);
		defaultPlageHoraire.setMinutesFin(hourFin * 60 + minFin);
		defaultPlageHoraire.setNbGuichet(nbGuichet);
		return defaultPlageHoraire;
	}

	public PlageHoraireDisplay getPlageHoraireDisplay(final UID<PlageHoraire> plageUid) {
		Assertion.check()
				.isNotNull(plageUid);
		//-----
		return agendaPAO.getPlageHoraireDisplayByPlhId((Long) plageUid.getId(), Instant.now());
	}

	public DtList<TrancheHoraireDisplay> getTrancheHoraireDisplayByPlage(final UID<PlageHoraire> plageUid) {
		Assertion.check()
				.isNotNull(plageUid);
		//-----
		return agendaPAO.getTrancheHoraireDisplayByPlhId((Long) plageUid.getId(), Instant.now());
	}

	public DtList<PlageHoraireDisplay> getReservationOrphelines(final List<UID<Agenda>> ageUids, final LocalDate firstDate, final LocalDate lastDate) {
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		return agendaPAO.countUnlinkReservationPerXminByAgeId(ageIds, firstDate, lastDate, PlanningServicesConfig.ORPHAN_RESERVATION_AGG_MINUTES);
	}

	public LocalDate getPremierJourLibrePourDuplication(final AgendaDisplayRange agendaRange) {
		//on prend soit la date du jour soit la date de début de la source + 1 semaine
		final var localDateTo = LocalDate.now().isAfter(agendaRange.getLastDate()) ? LocalDate.now() : agendaRange.getFirstDate().plusWeeks(1);

		final var firsts = agendaPAO.getFirstLocalDatesFreeOfPlageHorairePerDayOfWeek(
				agendaRange.getAgeIds(),
				agendaRange.getFirstDate(),
				agendaRange.getLastDate(),
				localDateTo,
				LocalDate.now().plusYears(1));
		if (firsts.isEmpty()) {
			return localDateTo;
		}
		return firsts.get(0);
	}

	public DtList<Creneau> getAvailableCreneaux(final UID<TrancheHoraire> trhUid, final int maxAvailabilities) {
		// Here we don't lock lines we just show the state at that time
		final var availableCreneaux = creneauDAO.findAll(Criterions.isEqualTo(CreneauFields.trhId, trhUid.getId()).and(Criterions.isNull(CreneauFields.recId)), DtListState.of(maxAvailabilities));
		return availableCreneaux;
	}

	public Optional<ReservationCreneau> reserverCreneau(final UID<TrancheHoraire> trhUid) {
		// Trouver un créneau sur une tranche : on utilise le mecanisme de lock de la BDD (for update skip locks)
		final var creneauOpt = creneauDAO.selectCreneauForUpdateByTrhId((Long) trhUid.getId());
		if (creneauOpt.isEmpty()) {
			//on a pas trouvé de creneau disponible à cette date
			return Optional.empty();

		}

		final var creneau = creneauOpt.get();
		creneau.trancheHoraire().load();
		final var trancheHoraire = creneau.trancheHoraire().get();
		//---

		final var reservationCreneau = prepareReservationCreneau(trancheHoraire);
		reservationCreneauDAO.create(reservationCreneau);

		creneau.reservationCreneau().setUID(reservationCreneau.getUID());
		creneauDAO.save(creneau);
		//---
		return Optional.of(reservationCreneau);
	}

	public Optional<ReservationCreneau> reserverCreneau(final List<UID<Agenda>> ageUids, final LocalDate startDate, final LocalDate endDate, final Integer startMinutes, final Integer endMinutes) {
		// Trouver un créneau sur une liste d'agenda avec des critères de sélection : on utilise le mecanisme de lock de la BDD (for update skip locks)
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		final var creneauOpt = creneauDAO.selectCreneauForUpdateByAgeIds(ageIds, startDate, endDate, startMinutes, endMinutes, Instant.now());
		if (creneauOpt.isEmpty()) {
			//on a pas trouvé de creneau disponible à cette date
			return Optional.empty();
		}

		final var creneau = creneauOpt.get();
		creneau.trancheHoraire().load();
		final var trancheHoraire = creneau.trancheHoraire().get();
		//---

		final var reservationCreneau = prepareReservationCreneau(trancheHoraire);
		reservationCreneauDAO.create(reservationCreneau);

		creneau.reservationCreneau().setUID(reservationCreneau.getUID());
		creneauDAO.save(creneau);
		//---
		return Optional.of(reservationCreneau);
	}

	public ReservationCreneau reserverCreneauWithOverbooking(final UID<TrancheHoraire> trhUid) {
		final var reservationCreneauOpt = reserverCreneau(trhUid);
		if (reservationCreneauOpt.isPresent()) {
			return reservationCreneauOpt.get();
		}
		final var trancheHoraire = trancheHoraireDAO.get(trhUid);
		final var reservationCreneau = prepareReservationCreneau(trancheHoraire);
		reservationCreneauDAO.create(reservationCreneau);
		return reservationCreneau;
	}

	public DtList<ReservationCreneau> reserverCreneaux(final UID<TrancheHoraire> trhUid, final DtList<Creneau> creneaux) {
		Assertion.check()
				.isNotNull(trhUid)
				.isNotNull(creneaux);
		//---
		try (final VTransactionWritable tx = transactionManager.createAutonomousTransaction()) {
			final var trancheHoraire = trancheHoraireDAO.get(trhUid);
			final DtList<ReservationCreneau> reservationsCreneau = creneaux.stream()
					// First we ensure that all creneaux are associated with the provided tranche
					.peek(creneau -> {
						if (!trhUid.equals(creneau.trancheHoraire().getUID())) {
							throw new VSystemException("All creneau must be associated with the same tranche, the creneau with creId '{0}' doesn't", creneau.getCreId());
						}
					})
					.map(creneau -> prepareReservationCreneau(trancheHoraire))
					.collect(VCollectors.toDtList(ReservationCreneau.class));

			reservationCreneauDAO.insertReservationsCreneau(reservationsCreneau);
			IntStream.range(0, creneaux.size()).forEach(idx -> {
				final var creneau = creneaux.get(idx);
				creneau.reservationCreneau().setUID(reservationsCreneau.get(idx).getUID());
			});
			final var modifiedRows = agendaPAO.reserverCreneaux(creneaux);
			if (modifiedRows != creneaux.size()) {
				throw new VSystemException("Error reserving the provided creneau, none is reserved");
			}
			tx.commit();
			return reservationsCreneau;
		}

	}

	private ReservationCreneau prepareReservationCreneau(final TrancheHoraire trancheHoraire) {
		final var reservationCreneau = new ReservationCreneau();
		reservationCreneau.agenda().setUID(trancheHoraire.agenda().getUID());
		reservationCreneau.setDateLocale(trancheHoraire.getDateLocale());
		reservationCreneau.setMinutesDebut(trancheHoraire.getMinutesDebut());
		reservationCreneau.setMinutesFin(trancheHoraire.getMinutesFin());
		reservationCreneau.setInstantCreation(Instant.now());

		return reservationCreneau;
	}

	public void publishPlageHorairesAndRelinkReservation(final List<UID<Agenda>> ageUids, final PublicationTrancheHoraireForm publicationTrancheHoraireForm) {

		//On change le status des trancheHoraires non publiées en masse (y compris celles planifiées)
		publishPlageHoraires(ageUids, publicationTrancheHoraireForm);

		//On pose le lien vers une reservation depuis le creneau
		relinkReservationsToCreneaux(ageUids, publicationTrancheHoraireForm.getDateLocaleDebut(), publicationTrancheHoraireForm.getDateLocaleFin());
	}

	private void publishPlageHoraires(final List<UID<Agenda>> ageUids, final PublicationTrancheHoraireForm publicationTrancheHoraireForm) {
		//On change le status des trancheHoraires non publiées en masse (y compris celles planifiées)
		final var uiErrorBuilder = new UiErrorBuilder();
		final var decalageJours = ChronoUnit.DAYS.between(LocalDate.now(), publicationTrancheHoraireForm.getDateLocaleDebut());
		if (Math.abs(decalageJours) > planningServicesConfig.getPublishMaxDaysFromNow()) { //abs : on autorise la publication de creneau passé
			uiErrorBuilder.addError(publicationTrancheHoraireForm, PublicationTrancheHoraireFormFields.dateLocaleDebut,
					LocaleMessageText.of("Vous ne pouvez pas publier des créneaux à plus de {0}", getDaysLimitLabel(planningServicesConfig.getPublishMaxDaysFromNow(), false)));
		} else if (decalageJours < 0) { //on n'autorise pas la publication de creneau passé RDV-154
			uiErrorBuilder.addError(publicationTrancheHoraireForm, PublicationTrancheHoraireFormFields.dateLocaleDebut,
					LocaleMessageText.of("Vous ne pouvez pas publier des créneaux du passé"));
		}

		final var dureeJours = ChronoUnit.DAYS.between(publicationTrancheHoraireForm.getDateLocaleDebut(), publicationTrancheHoraireForm.getDateLocaleFin());
		if (dureeJours < 0) {//inferieur strictement
			uiErrorBuilder.addError(publicationTrancheHoraireForm, PublicationTrancheHoraireFormFields.dateLocaleFin,
					LocaleMessageText.of("La date de fin de la selection doit être postérieur à la date de début"));
		} else if (dureeJours > planningServicesConfig.getPublishMaxDaysPeriode()) {
			uiErrorBuilder.addError(publicationTrancheHoraireForm, PublicationTrancheHoraireFormFields.dateLocaleFin,
					LocaleMessageText.of("Vous ne pouvez pas publier plus de {0} à la fois", getDaysLimitLabel(planningServicesConfig.getPublishMaxDaysPeriode(), true)));
		}
		if (!publicationTrancheHoraireForm.getPublishNow()) {
			uiErrorBuilder.checkFieldNotNull(publicationTrancheHoraireForm, PublicationTrancheHoraireFormFields.publicationDateLocale,
					LocaleMessageText.of("La date de publication est obligatoire"));
			uiErrorBuilder.checkFieldNotNull(publicationTrancheHoraireForm, PublicationTrancheHoraireFormFields.publicationMinutesDebut,
					LocaleMessageText.of("L'heure de publication est obligatoire"));
			if (publicationTrancheHoraireForm.getPublicationDateLocale() != null) {
				final var decalageJoursPublish = ChronoUnit.DAYS.between(LocalDate.now(), publicationTrancheHoraireForm.getPublicationDateLocale());
				if (decalageJoursPublish < 0) {
					uiErrorBuilder.addError(publicationTrancheHoraireForm, PublicationTrancheHoraireFormFields.publicationDateLocale,
							LocaleMessageText.of("Vous ne pouvez pas planifier la publication à une date dans le passé"));
				}
			}
		}
		uiErrorBuilder.throwUserExceptionIfErrors();

		Instant datePublication;
		if (publicationTrancheHoraireForm.getPublishNow()) {
			datePublication = Instant.now().plusSeconds(planningServicesConfig.getPublishNowDelaySecond());
		} else {

			final var localTime = LocalTime.ofSecondOfDay(publicationTrancheHoraireForm.getPublicationMinutesDebut() * 60L);
			final var publishLocalDateTime = LocalDateTime.of(publicationTrancheHoraireForm.getPublicationDateLocale(), localTime);

			//on suppose l'heure exprimée à paris par défaut
			String zonCd = "Europe/Paris";
			if (publicationTrancheHoraireForm.getPublicationZonCd() != null) {
				zonCd = publicationTrancheHoraireForm.getPublicationZonCd();
			}
			datePublication = publishLocalDateTime.atZone(ZoneId.of(zonCd)).toInstant();
		}
		uiErrorBuilder.throwUserExceptionIfErrors();
		/*****/
		//on planifie
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		agendaPAO.publishTrancheHoraireByAgeIds(ageIds,
				publicationTrancheHoraireForm.getDateLocaleDebut(), publicationTrancheHoraireForm.getDateLocaleFin(),
				Instant.now(), datePublication);

		//on crée les créneaux en masse
		agendaPAO.createCreneauOfPublishedTrancheHoraireByAgeIds(ageIds,
				publicationTrancheHoraireForm.getDateLocaleDebut(), publicationTrancheHoraireForm.getDateLocaleFin(),
				datePublication);
	}

	/**
	 * Reassocie les creneaux publiés aux ReservationCreneaux existant.
	 * Utilisé après une publication de plage horaire pour rattacher les réservations préexistantes.
	 * ou après un import de réservation.
	 * @param ageUid Uid de l'agenda
	 * @param dateLocaleDebut Date de début
	 * @param dateLocaleFin Date de fin
	 */
	public void relinkReservationsToCreneaux(final List<UID<Agenda>> ageUids, final LocalDate dateLocaleDebut, final LocalDate dateLocaleFin) {
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		final var affectations = agendaPAO.getLinkReservationAfterPublishByAgeIds(ageIds, dateLocaleDebut, dateLocaleFin);
		final Set<String> affectedCreneau = new HashSet<>();
		for (final AffectionReservation affectation : affectations) {
			final var creIdsAsString = affectation.getCreIds().split(";");
			for (final String creIdAsString : creIdsAsString) {
				if (affectedCreneau.add(creIdAsString)) {
					affectation.setCreId(Long.valueOf(creIdAsString));
					break;
				}
			}
		}
		agendaPAO.linkCreneauToReservation(affectations);
	}

	public void supprimerReservationCreneau(final List<UID<ReservationCreneau>> recUids) {
		Assertion.check().isNotNull(recUids);
		//---
		final List<Long> recIds = recUids.stream()
				.filter(id -> id != null) //protect against bad caller
				.map(UID::getId)
				.map(Long.class::cast).toList();
		agendaPAO.supprimerReservationsCreneau(recIds);
	}

	public Optional<TrancheHoraire> getTrancheHoraireIfExists(final UID<TrancheHoraire> trhUid) {
		return trancheHoraireDAO.findOptional(Criterions.isEqualTo(TrancheHoraireFields.trhId, trhUid.getId()));
	}

	/**
	 * Récupère les date disponibles d'un mois.
	 * N'affiche pas les disponibilités < date du jour.
	 *
	 * @param ageUid l'id d'agenda
	 * @param yearMonth le mois à afficher.
	 * @return la liste d'affichage
	 */
	public DtList<DateDisponibleDisplay> getDateDisponibleDisplayFuturOnly(final List<UID<Agenda>> ageUids, final YearMonth yearMonth) {
		Assertion.check()
				.isNotNull(ageUids)
				.isNotNull(yearMonth);
		//-----
		if (yearMonth.isBefore(YearMonth.now())) { // mois passé, on affiche rien (et s'économise un appel base)
			return new DtList<>(DateDisponibleDisplay.class);
		}

		final var firstDayOfMonth = yearMonth.atDay(1);
		final LocalDate today = LocalDate.now();
		final var startLocaldate = firstDayOfMonth.isBefore(today) ? today : firstDayOfMonth; // on commence pas avant la date du jour

		return getDateDisponibleDisplay(ageUids, startLocaldate, yearMonth.atEndOfMonth());
	}

	public DtList<DateDisponibleDisplay> getDateDisponibleDisplay(final List<UID<Agenda>> ageUids, final LocalDate startDate, final LocalDate endDate) {
		Assertion.check()
				.isNotNull(ageUids)
				.isNotNull(startDate)
				.isNotNull(endDate);
		//-----
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		return agendaPAO.getDateDisponibleDisplayByAgeIds(ageIds, startDate, endDate, Instant.now());
	}

	public DtList<TrancheHoraireDisplay> getTrancheHoraireDisplayByDate(final List<UID<Agenda>> ageUids, final LocalDate startDate, final LocalDate endDate) {
		Assertion.check()
				.isNotNull(ageUids)
				.isNotNull(startDate)
				.isNotNull(endDate);
		//-----
		final var ageIds = ageUids.stream().map(UID::getId).map(Long.class::cast).toList();
		return agendaPAO.getTrancheHoraireDisplayByDate(ageIds, startDate, endDate, Instant.now());
	}

	public void deleteEmptyAgenda(final UID<Agenda> agendaUid) {
		agendaDAO.delete(agendaUid);
	}

	/**
	 * Purge agenda data linked to plage_horaire older than given date
	 *
	 * @param olderThan
	 */
	public int purgeAgendaOlderThan(final LocalDate olderThan) {
		Assertion.check().isNotNull(olderThan);
		return agendaPAO.purgePlageHoraireByDateLocale(olderThan);
	}

	public UID<Agenda> checkAuthorizedAgendaOfPlh(final UID<PlageHoraire> plhUid, final List<UID<Agenda>> authorizedAgeUids) {
		final var plageHoraire = plageHoraireDAO.get(plhUid);
		final var ageUid = UID.of(Agenda.class, plageHoraire.getAgeId());
		return checkAuthorizedAgenda(ageUid, authorizedAgeUids);
	}

	public UID<Agenda> checkAuthorizedAgenda(final UID<Agenda> ageUid, final List<UID<Agenda>> authorizedAgeUids) {
		//check ageId in agenda.getAgeIds()
		if (!authorizedAgeUids.contains(ageUid)) {
			throw new VSecurityException(LocaleMessageText.of("Vous n'avez pas les droits pour agir sur cet agenda"));
		}
		return ageUid;
	}

	protected final String getMinutesLabel(final int minutesOfDay) {
		return String.format("%02d:%02d", minutesOfDay / 60, minutesOfDay % 60);
	}

	protected String getDaysLimitLabel(final int days, final boolean onlyOneLevel) {
		Assertion.check().isTrue(days > 0, "Can't display less than 1 day");
		//--
		final int years = days / 365;
		final int remainingDaysAfterYears = days % 365;
		final int months = remainingDaysAfterYears / 30;
		final int remainingDaysAfterMonths = remainingDaysAfterYears % 30;
		final int weeks = remainingDaysAfterMonths / 7;
		final int remainingDaysAfterWeeks = remainingDaysAfterMonths % 7;

		if (years > 0 && (remainingDaysAfterYears == 0 || onlyOneLevel)) {
			return plurialize(years, "an", "ans");
		} else if (months > 0 && (remainingDaysAfterMonths == 0 || onlyOneLevel)) {
			return plurialize(months, "mois", "mois");
		} else if (weeks > 0 && (remainingDaysAfterWeeks == 0 || onlyOneLevel)) {
			return plurialize(weeks, "semaine", "semaines");
		} else if (years > 0) {
			return plurialize(years, "an", "ans") + " et " + plurialize(months, "mois", "mois");
		} else if (months > 0) {
			return plurialize(months, "mois", "mois") + " et " + plurialize(weeks, "semaine", "semaines");
		} else if (weeks > 0) {
			return plurialize(weeks, "semaine", "semaines") + " et " + plurialize(remainingDaysAfterWeeks, "jour", "jours");
		} else {
			return plurialize(days, "jour", "jours");
		}
	}

	private static String plurialize(final int value, final String singular, final String plural) {
		return value + " " + (value == 1 ? singular : plural);
	}

}
