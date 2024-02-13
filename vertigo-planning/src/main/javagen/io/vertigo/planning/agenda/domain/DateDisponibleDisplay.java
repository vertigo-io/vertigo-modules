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
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.stereotype.Field;
import io.vertigo.datamodel.data.util.DataModelUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class DateDisponibleDisplay implements DataObject {
	private static final long serialVersionUID = 1L;

	private java.time.LocalDate dateLocale;
	private Integer nbNonPublie;
	private Integer nbPlanifie;
	private Integer nbPublie;
	private Integer nbReserve;
	private Integer nbTotal;
	private java.time.Instant instantPublication;

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date disponible'.
	 * @return LocalDate dateLocale
	 */
	@Field(smartType = "STyPLocalDate", label = "Date disponible")
	public java.time.LocalDate getDateLocale() {
		return dateLocale;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date disponible'.
	 * @param dateLocale LocalDate
	 */
	public void setDateLocale(final java.time.LocalDate dateLocale) {
		this.dateLocale = dateLocale;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Non publié'.
	 * @return Integer nbNonPublie
	 */
	@Field(smartType = "STyPNombre", label = "Non publié")
	public Integer getNbNonPublie() {
		return nbNonPublie;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Non publié'.
	 * @param nbNonPublie Integer
	 */
	public void setNbNonPublie(final Integer nbNonPublie) {
		this.nbNonPublie = nbNonPublie;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Planifié'.
	 * @return Integer nbPlanifie
	 */
	@Field(smartType = "STyPNombre", label = "Planifié")
	public Integer getNbPlanifie() {
		return nbPlanifie;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Planifié'.
	 * @param nbPlanifie Integer
	 */
	public void setNbPlanifie(final Integer nbPlanifie) {
		this.nbPlanifie = nbPlanifie;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Publié'.
	 * @return Integer nbPublie
	 */
	@Field(smartType = "STyPNombre", label = "Publié")
	public Integer getNbPublie() {
		return nbPublie;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Publié'.
	 * @param nbPublie Integer
	 */
	public void setNbPublie(final Integer nbPublie) {
		this.nbPublie = nbPublie;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Réservé'.
	 * @return Integer nbReserve
	 */
	@Field(smartType = "STyPNombre", label = "Réservé")
	public Integer getNbReserve() {
		return nbReserve;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Réservé'.
	 * @param nbReserve Integer
	 */
	public void setNbReserve(final Integer nbReserve) {
		this.nbReserve = nbReserve;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Total'.
	 * @return Integer nbTotal
	 */
	@Field(smartType = "STyPNombre", label = "Total")
	public Integer getNbTotal() {
		return nbTotal;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Total'.
	 * @param nbTotal Integer
	 */
	public void setNbTotal(final Integer nbTotal) {
		this.nbTotal = nbTotal;
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
		return DataModelUtil.toString(this);
	}
}
