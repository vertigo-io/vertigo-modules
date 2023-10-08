package io.vertigo.planning.agenda.domain;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class ReservationCreneau implements Entity {
	private static final long serialVersionUID = 1L;

	private Long recId;
	private java.time.LocalDate dateLocale;
	private Integer minutesDebut;
	private Integer minutesFin;
	private java.time.Instant instantCreation;

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "AReservationCreneauAgenda",
			fkFieldName = "ageId",
			primaryDtDefinitionName = "DtAgenda",
			primaryIsNavigable = true,
			primaryRole = "Agenda",
			primaryLabel = "Agenda",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtReservationCreneau",
			foreignIsNavigable = false,
			foreignRole = "ReservationCreneau",
			foreignLabel = "ReservationCreneau",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.planning.agenda.domain.Agenda> ageIdAccessor = new StoreVAccessor<>(io.vertigo.planning.agenda.domain.Agenda.class, "Agenda");

	/** {@inheritDoc} */
	@Override
	public UID<ReservationCreneau> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id'.
	 * @return Long recId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPId", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id")
	public Long getRecId() {
		return recId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id'.
	 * @param recId Long <b>Obligatoire</b>
	 */
	public void setRecId(final Long recId) {
		this.recId = recId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date du créneau'.
	 * @return LocalDate dateLocale <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPLocalDate", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Date du créneau")
	public java.time.LocalDate getDateLocale() {
		return dateLocale;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date du créneau'.
	 * @param dateLocale LocalDate <b>Obligatoire</b>
	 */
	public void setDateLocale(final java.time.LocalDate dateLocale) {
		this.dateLocale = dateLocale;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Heure de début'.
	 * @return Integer minutesDebut <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPHeureMinute", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Heure de début")
	public Integer getMinutesDebut() {
		return minutesDebut;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Heure de début'.
	 * @param minutesDebut Integer <b>Obligatoire</b>
	 */
	public void setMinutesDebut(final Integer minutesDebut) {
		this.minutesDebut = minutesDebut;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Heure de fin'.
	 * @return Integer minutesFin <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPHeureMinute", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Heure de fin")
	public Integer getMinutesFin() {
		return minutesFin;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Heure de fin'.
	 * @param minutesFin Integer <b>Obligatoire</b>
	 */
	public void setMinutesFin(final Integer minutesFin) {
		this.minutesFin = minutesFin;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de réservation'.
	 * @return Instant instantCreation <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPInstant", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Date de réservation")
	public java.time.Instant getInstantCreation() {
		return instantCreation;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de réservation'.
	 * @param instantCreation Instant <b>Obligatoire</b>
	 */
	public void setInstantCreation(final java.time.Instant instantCreation) {
		this.instantCreation = instantCreation;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Agenda'.
	 * @return Long ageId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyPId", label = "Agenda", fkDefinition = "DtAgenda", cardinality = io.vertigo.core.lang.Cardinality.ONE )
	public Long getAgeId() {
		return (Long) ageIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Agenda'.
	 * @param ageId Long <b>Obligatoire</b>
	 */
	public void setAgeId(final Long ageId) {
		ageIdAccessor.setId(ageId);
	}

 	/**
	 * Association : Agenda.
	 * @return l'accesseur vers la propriété 'Agenda'
	 */
	public StoreVAccessor<io.vertigo.planning.agenda.domain.Agenda> agenda() {
		return ageIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}