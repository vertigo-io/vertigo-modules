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
public final class CreationPlageHoraireForm implements Data {
	private static final long serialVersionUID = 1L;

	private java.time.LocalDate dateLocale;
	private Integer minutesDebut;
	private Integer minutesFin;
	private Integer nbGuichet;
	private Integer dureeCreneau;

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de la plage horaire'.
	 * @return LocalDate dateLocale <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPLocalDate", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Date de la plage horaire")
	public java.time.LocalDate getDateLocale() {
		return dateLocale;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de la plage horaire'.
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
	 * Récupère la valeur de la propriété 'Nombre de guichets'.
	 * @return Integer nbGuichet <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPNbGuichet", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Nombre de guichets")
	public Integer getNbGuichet() {
		return nbGuichet;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nombre de guichets'.
	 * @param nbGuichet Integer <b>Obligatoire</b>
	 */
	public void setNbGuichet(final Integer nbGuichet) {
		this.nbGuichet = nbGuichet;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Durée en minutes'.
	 * @return Integer dureeCreneau <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPMinute", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Durée en minutes")
	public Integer getDureeCreneau() {
		return dureeCreneau;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Durée en minutes'.
	 * @param dureeCreneau Integer <b>Obligatoire</b>
	 */
	public void setDureeCreneau(final Integer dureeCreneau) {
		this.dureeCreneau = dureeCreneau;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataUtil.toString(this);
	}
}
