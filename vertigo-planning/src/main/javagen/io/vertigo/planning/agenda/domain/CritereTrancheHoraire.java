/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.planning.agenda.domain;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.data.model.Data;
import io.vertigo.datamodel.data.stereotype.Field;
import io.vertigo.datamodel.data.util.DataUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class CritereTrancheHoraire implements Data {
	private static final long serialVersionUID = 1L;

	private java.util.List<Long> ageIds = new java.util.ArrayList<>();
	private java.time.LocalDate premierJour;
	private java.time.LocalDate dateMin;
	private Integer minutesMin;
	private java.time.LocalDate dateMax;
	private java.time.LocalDate datePremiereDispo;

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Ids d'agenda'.
	 * @return List de Long ageIds
	 */
	@Field(smartType = "STyPId", cardinality = io.vertigo.core.lang.Cardinality.MANY, label = "Ids d'agenda")
	public java.util.List<Long> getAgeIds() {
		return ageIds;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Ids d'agenda'.
	 * @param ageIds List de Long
	 */
	public void setAgeIds(final java.util.List<Long> ageIds) {
		io.vertigo.core.lang.Assertion.check().isNotNull(ageIds);
		//---
		this.ageIds = ageIds;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Premier jour'.
	 * @return LocalDate premierJour
	 */
	@Field(smartType = "STyPLocalDate", label = "Premier jour")
	public java.time.LocalDate getPremierJour() {
		return premierJour;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Premier jour'.
	 * @param premierJour LocalDate
	 */
	public void setPremierJour(final java.time.LocalDate premierJour) {
		this.premierJour = premierJour;
	}

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
	 * Récupère la valeur de la propriété 'Heure de début'.
	 * @return Integer minutesMin <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPHeureMinute", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Heure de début")
	public Integer getMinutesMin() {
		return minutesMin;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Heure de début'.
	 * @param minutesMin Integer <b>Obligatoire</b>
	 */
	public void setMinutesMin(final Integer minutesMin) {
		this.minutesMin = minutesMin;
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
	 * Récupère la valeur de la propriété 'Date de première disponibilité'.
	 * @return LocalDate datePremiereDispo
	 */
	@Field(smartType = "STyPLocalDate", label = "Date de première disponibilité")
	public java.time.LocalDate getDatePremiereDispo() {
		return datePremiereDispo;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de première disponibilité'.
	 * @param datePremiereDispo LocalDate
	 */
	public void setDatePremiereDispo(final java.time.LocalDate datePremiereDispo) {
		this.datePremiereDispo = datePremiereDispo;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataUtil.toString(this);
	}
}
