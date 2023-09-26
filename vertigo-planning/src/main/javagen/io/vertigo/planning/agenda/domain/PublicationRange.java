package io.vertigo.planning.agenda.domain;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.DtObject;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class PublicationRange implements DtObject {
	private static final long serialVersionUID = 1L;

	private java.time.LocalDate dateMin;
	private java.time.LocalDate dateMax;
	private java.time.Instant instantPublication;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de début'.
	 * @return LocalDate dateMin
	 */
	@Field(smartType = "STyPLocalDate", label = "Date de début")
	public java.time.LocalDate getDateMin() {
		return dateMin;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de début'.
	 * @param dateMin LocalDate
	 */
	public void setDateMin(final java.time.LocalDate dateMin) {
		this.dateMin = dateMin;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de fin'.
	 * @return LocalDate dateMax
	 */
	@Field(smartType = "STyPLocalDate", label = "Date de fin")
	public java.time.LocalDate getDateMax() {
		return dateMax;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de fin'.
	 * @param dateMax LocalDate
	 */
	public void setDateMax(final java.time.LocalDate dateMax) {
		this.dateMax = dateMax;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date publication'.
	 * @return Instant instantPublication
	 */
	@Field(smartType = "STyPInstant", label = "Date publication")
	public java.time.Instant getInstantPublication() {
		return instantPublication;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date publication'.
	 * @param instantPublication Instant
	 */
	public void setInstantPublication(final java.time.Instant instantPublication) {
		this.instantPublication = instantPublication;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
