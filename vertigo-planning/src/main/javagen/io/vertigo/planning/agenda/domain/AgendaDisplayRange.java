package io.vertigo.planning.agenda.domain;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.stereotype.Field;
import io.vertigo.datamodel.data.util.DataModelUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class AgendaDisplayRange implements DataObject {
	private static final long serialVersionUID = 1L;

	private java.util.List<Long> ageIds = new java.util.ArrayList<>();
	private java.time.LocalDate showDate;
	private java.time.LocalDate firstDate;
	private java.time.LocalDate lastDate;
	private Boolean mondayLock;
	private Integer showDays;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Ids agenda'.
	 * @return List de Long ageIds
	 */
	@Field(smartType = "STyPId", cardinality = io.vertigo.core.lang.Cardinality.MANY, label = "Ids agenda")
	public java.util.List<Long> getAgeIds() {
		return ageIds;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Ids agenda'.
	 * @param ageIds List de Long
	 */
	public void setAgeIds(final java.util.List<Long> ageIds) {
		io.vertigo.core.lang.Assertion.check().isNotNull(ageIds);
		//---
		this.ageIds = ageIds;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date sélectionnée'.
	 * @return LocalDate showDate
	 */
	@Field(smartType = "STyPLocalDate", label = "Date sélectionnée")
	public java.time.LocalDate getShowDate() {
		return showDate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date sélectionnée'.
	 * @param showDate LocalDate
	 */
	public void setShowDate(final java.time.LocalDate showDate) {
		this.showDate = showDate;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de début'.
	 * @return LocalDate firstDate
	 */
	@Field(smartType = "STyPLocalDate", label = "Date de début")
	public java.time.LocalDate getFirstDate() {
		return firstDate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de début'.
	 * @param firstDate LocalDate
	 */
	public void setFirstDate(final java.time.LocalDate firstDate) {
		this.firstDate = firstDate;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de fin (incluse)'.
	 * @return LocalDate lastDate
	 */
	@Field(smartType = "STyPLocalDate", label = "Date de fin (incluse)")
	public java.time.LocalDate getLastDate() {
		return lastDate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de fin (incluse)'.
	 * @param lastDate LocalDate
	 */
	public void setLastDate(final java.time.LocalDate lastDate) {
		this.lastDate = lastDate;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Si commence toujours par lundi'.
	 * @return Boolean mondayLock
	 */
	@Field(smartType = "STyPBooleen", label = "Si commence toujours par lundi")
	public Boolean getMondayLock() {
		return mondayLock;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Si commence toujours par lundi'.
	 * @param mondayLock Boolean
	 */
	public void setMondayLock(final Boolean mondayLock) {
		this.mondayLock = mondayLock;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nombre de jour affichés'.
	 * @return Integer showDays
	 */
	@Field(smartType = "STyPNombre", label = "Nombre de jour affichés")
	public Integer getShowDays() {
		return showDays;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nombre de jour affichés'.
	 * @param showDays Integer
	 */
	public void setShowDays(final Integer showDays) {
		this.showDays = showDays;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
