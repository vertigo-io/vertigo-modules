package io.vertigo.planning.domain;

import java.util.Arrays;
import java.util.Iterator;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.definitions.DtFieldName;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class DtDefinitions implements Iterable<Class<?>> {

	/**
	 * Enumération des DtDefinitions.
	 */
	public enum Definitions {
		/** Objet de données AffectionReservation. */
		AffectionReservation(io.vertigo.planning.agenda.domain.AffectionReservation.class),
		/** Objet de données Agenda. */
		Agenda(io.vertigo.planning.agenda.domain.Agenda.class),
		/** Objet de données AgendaDisplayRange. */
		AgendaDisplayRange(io.vertigo.planning.agenda.domain.AgendaDisplayRange.class),
		/** Objet de données CreationPlageHoraireForm. */
		CreationPlageHoraireForm(io.vertigo.planning.agenda.domain.CreationPlageHoraireForm.class),
		/** Objet de données Creneau. */
		Creneau(io.vertigo.planning.agenda.domain.Creneau.class),
		/** Objet de données CritereTrancheHoraire. */
		CritereTrancheHoraire(io.vertigo.planning.agenda.domain.CritereTrancheHoraire.class),
		/** Objet de données DateDisponibleDisplay. */
		DateDisponibleDisplay(io.vertigo.planning.agenda.domain.DateDisponibleDisplay.class),
		/** Objet de données DefaultPlageHoraire. */
		DefaultPlageHoraire(io.vertigo.planning.agenda.domain.DefaultPlageHoraire.class),
		/** Objet de données DuplicationSemaineForm. */
		DuplicationSemaineForm(io.vertigo.planning.agenda.domain.DuplicationSemaineForm.class),
		/** Objet de données PlageHoraire. */
		PlageHoraire(io.vertigo.planning.agenda.domain.PlageHoraire.class),
		/** Objet de données PlageHoraireDisplay. */
		PlageHoraireDisplay(io.vertigo.planning.agenda.domain.PlageHoraireDisplay.class),
		/** Objet de données PublicationRange. */
		PublicationRange(io.vertigo.planning.agenda.domain.PublicationRange.class),
		/** Objet de données PublicationTrancheHoraireForm. */
		PublicationTrancheHoraireForm(io.vertigo.planning.agenda.domain.PublicationTrancheHoraireForm.class),
		/** Objet de données ReservationCreneau. */
		ReservationCreneau(io.vertigo.planning.agenda.domain.ReservationCreneau.class),
		/** Objet de données TrancheHoraire. */
		TrancheHoraire(io.vertigo.planning.agenda.domain.TrancheHoraire.class),
		/** Objet de données TrancheHoraireDisplay. */
		TrancheHoraireDisplay(io.vertigo.planning.agenda.domain.TrancheHoraireDisplay.class)		;

		private final Class<?> clazz;

		private Definitions(final Class<?> clazz) {
			this.clazz = clazz;
		}

		/** 
		 * Classe associée.
		 * @return Class d'implémentation de l'objet 
		 */
		public Class<?> getDtClass() {
			return clazz;
		}
	}

	/**
	 * Enumération des champs de AffectionReservation.
	 */
	public enum AffectionReservationFields implements DtFieldName<io.vertigo.planning.agenda.domain.AffectionReservation> {
		/** Propriété 'Id de tranche horaire'. */
		trhId,
		/** Propriété 'Liste des creneaux disponibles'. */
		creIds,
		/** Propriété 'creneau affecté'. */
		creId,
		/** Propriété 'Id de la reservation'. */
		recId,
		/** Propriété 'Date de creation de la reservation'. */
		instantCreation	}

	/**
	 * Enumération des champs de Agenda.
	 */
	public enum AgendaFields implements DtFieldName<io.vertigo.planning.agenda.domain.Agenda> {
		/** Propriété 'Id'. */
		ageId,
		/** Propriété 'Nom'. */
		nom	}

	/**
	 * Enumération des champs de AgendaDisplayRange.
	 */
	public enum AgendaDisplayRangeFields implements DtFieldName<io.vertigo.planning.agenda.domain.AgendaDisplayRange> {
		/** Propriété 'Id démarche'. */
		demId,
		/** Propriété 'Id agenda'. */
		ageId,
		/** Propriété 'Date sélectionnée'. */
		showDate,
		/** Propriété 'Date de début'. */
		firstDate,
		/** Propriété 'Date de fin (incluse)'. */
		lastDate,
		/** Propriété 'Si commence toujours par lundi'. */
		mondayLock,
		/** Propriété 'Nombre de jour affichés'. */
		showDays	}

	/**
	 * Enumération des champs de CreationPlageHoraireForm.
	 */
	public enum CreationPlageHoraireFormFields implements DtFieldName<io.vertigo.planning.agenda.domain.CreationPlageHoraireForm> {
		/** Propriété 'Date de la plage horaire'. */
		dateLocale,
		/** Propriété 'Heure de début'. */
		minutesDebut,
		/** Propriété 'Heure de fin'. */
		minutesFin,
		/** Propriété 'Nombre de guichets'. */
		nbGuichet,
		/** Propriété 'Durée en minutes'. */
		dureeCreneau	}

	/**
	 * Enumération des champs de Creneau.
	 */
	public enum CreneauFields implements DtFieldName<io.vertigo.planning.agenda.domain.Creneau> {
		/** Propriété 'Id'. */
		creId,
		/** Propriété 'Tranche horaire'. */
		trhId,
		/** Propriété 'Réservation'. */
		recId	}

	/**
	 * Enumération des champs de CritereTrancheHoraire.
	 */
	public enum CritereTrancheHoraireFields implements DtFieldName<io.vertigo.planning.agenda.domain.CritereTrancheHoraire> {
		/** Propriété 'Ids d'agenda'. */
		ageIds,
		/** Propriété 'Premier jour'. */
		premierJour,
		/** Propriété 'Date de début'. */
		dateMin,
		/** Propriété 'Heure de début'. */
		minutesMin,
		/** Propriété 'Date de fin'. */
		dateMax,
		/** Propriété 'Date de première disponibilité'. */
		datePremiereDispo	}

	/**
	 * Enumération des champs de DateDisponibleDisplay.
	 */
	public enum DateDisponibleDisplayFields implements DtFieldName<io.vertigo.planning.agenda.domain.DateDisponibleDisplay> {
		/** Propriété 'Date disponible'. */
		dateLocale,
		/** Propriété 'Non publié'. */
		nbNonPublie,
		/** Propriété 'Planifié'. */
		nbPlanifie,
		/** Propriété 'Publié'. */
		nbPublie,
		/** Propriété 'Réservé'. */
		nbReserve,
		/** Propriété 'Total'. */
		nbTotal,
		/** Propriété 'Date publication'. */
		instantPublication	}

	/**
	 * Enumération des champs de DefaultPlageHoraire.
	 */
	public enum DefaultPlageHoraireFields implements DtFieldName<io.vertigo.planning.agenda.domain.DefaultPlageHoraire> {
		/** Propriété 'Jour de la semaine'. */
		jourDeSemaine,
		/** Propriété 'Heure de début'. */
		minutesDebut,
		/** Propriété 'Heure de fin'. */
		minutesFin,
		/** Propriété 'Nombre de guichets'. */
		nbGuichet	}

	/**
	 * Enumération des champs de DuplicationSemaineForm.
	 */
	public enum DuplicationSemaineFormFields implements DtFieldName<io.vertigo.planning.agenda.domain.DuplicationSemaineForm> {
		/** Propriété 'Date de début de la source'. */
		dateLocaleFromDebut,
		/** Propriété 'Date de fin (incluse) de la source'. */
		dateLocaleFromFin,
		/** Propriété 'Date de début'. */
		dateLocaleToDebut,
		/** Propriété 'Date de fin (incluse)'. */
		dateLocaleToFin,
		/** Propriété 'Durée en minutes'. */
		dureeCreneau	}

	/**
	 * Enumération des champs de PlageHoraire.
	 */
	public enum PlageHoraireFields implements DtFieldName<io.vertigo.planning.agenda.domain.PlageHoraire> {
		/** Propriété 'Id'. */
		plhId,
		/** Propriété 'Date de la plage'. */
		dateLocale,
		/** Propriété 'Heure de début'. */
		minutesDebut,
		/** Propriété 'Heure de fin'. */
		minutesFin,
		/** Propriété 'Nombre de guichets'. */
		nbGuichet,
		/** Propriété 'Agenda'. */
		ageId	}

	/**
	 * Enumération des champs de PlageHoraireDisplay.
	 */
	public enum PlageHoraireDisplayFields implements DtFieldName<io.vertigo.planning.agenda.domain.PlageHoraireDisplay> {
		/** Propriété 'Id'. */
		plhId,
		/** Propriété 'Date de la plage horaire'. */
		dateLocale,
		/** Propriété 'Heure de début'. */
		minutesDebut,
		/** Propriété 'Heure de fin'. */
		minutesFin,
		/** Propriété 'Nombre de guichets'. */
		nbGuichet,
		/** Propriété 'Réservé'. */
		nbReserve,
		/** Propriété 'Réservé et non publié'. */
		nbReserveNonPublie,
		/** Propriété 'Total'. */
		nbTotal,
		/** Propriété 'Non publié'. */
		nbNonPublie,
		/** Propriété 'Planifié'. */
		nbPlanifie,
		/** Propriété 'Publié'. */
		nbPublie,
		/** Propriété 'Date publication'. */
		instantPublication	}

	/**
	 * Enumération des champs de PublicationRange.
	 */
	public enum PublicationRangeFields implements DtFieldName<io.vertigo.planning.agenda.domain.PublicationRange> {
		/** Propriété 'Date de début'. */
		dateMin,
		/** Propriété 'Date de fin'. */
		dateMax,
		/** Propriété 'Date publication'. */
		instantPublication	}

	/**
	 * Enumération des champs de PublicationTrancheHoraireForm.
	 */
	public enum PublicationTrancheHoraireFormFields implements DtFieldName<io.vertigo.planning.agenda.domain.PublicationTrancheHoraireForm> {
		/** Propriété 'Publication immediate'. */
		publishNow,
		/** Propriété 'Date de début'. */
		dateLocaleDebut,
		/** Propriété 'Date de fin (incluse)'. */
		dateLocaleFin,
		/** Propriété 'Date de publication'. */
		publicationDateLocale,
		/** Propriété 'Heure de publication'. */
		publicationMinutesDebut	}

	/**
	 * Enumération des champs de ReservationCreneau.
	 */
	public enum ReservationCreneauFields implements DtFieldName<io.vertigo.planning.agenda.domain.ReservationCreneau> {
		/** Propriété 'Id'. */
		recId,
		/** Propriété 'Date du créneau'. */
		dateLocale,
		/** Propriété 'Heure de début'. */
		minutesDebut,
		/** Propriété 'Heure de fin'. */
		minutesFin,
		/** Propriété 'Date de réservation'. */
		instantCreation,
		/** Propriété 'Agenda'. */
		ageId	}

	/**
	 * Enumération des champs de TrancheHoraire.
	 */
	public enum TrancheHoraireFields implements DtFieldName<io.vertigo.planning.agenda.domain.TrancheHoraire> {
		/** Propriété 'Id'. */
		trhId,
		/** Propriété 'Date du créneau'. */
		dateLocale,
		/** Propriété 'Heure de début'. */
		minutesDebut,
		/** Propriété 'Heure de fin'. */
		minutesFin,
		/** Propriété 'Nombre de guichets'. */
		nbGuichet,
		/** Propriété 'Date publication'. */
		instantPublication,
		/** Propriété 'Plage horaire'. */
		plhId,
		/** Propriété 'Agenda'. */
		ageId	}

	/**
	 * Enumération des champs de TrancheHoraireDisplay.
	 */
	public enum TrancheHoraireDisplayFields implements DtFieldName<io.vertigo.planning.agenda.domain.TrancheHoraireDisplay> {
		/** Propriété 'Id'. */
		trhId,
		/** Propriété 'Date du creneau'. */
		dateLocale,
		/** Propriété 'Heure de début'. */
		minutesDebut,
		/** Propriété 'Heure de fin'. */
		minutesFin,
		/** Propriété 'Nombre de guichets'. */
		nbGuichet,
		/** Propriété 'Réservé'. */
		nbReserve,
		/** Propriété 'Réservé et non publié'. */
		nbReserveNonPublie,
		/** Propriété 'Total'. */
		nbTotal,
		/** Propriété 'Publication'. */
		etatPublication,
		/** Propriété 'Date publication'. */
		instantPublication	}

	/** {@inheritDoc} */
	@Override
	public Iterator<Class<?>> iterator() {
		return new Iterator<>() {
			private Iterator<Definitions> it = Arrays.asList(Definitions.values()).iterator();

			/** {@inheritDoc} */
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			/** {@inheritDoc} */
			@Override
			public Class<?> next() {
				return it.next().getDtClass();
			}
		};
	}
}