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
public final class PublicationTrancheHoraireForm implements DataObject {
	private static final long serialVersionUID = 1L;

	private Boolean publishNow;
	private java.time.LocalDate dateLocaleDebut;
	private java.time.LocalDate dateLocaleFin;
	private java.time.LocalDate publicationDateLocale;
	private Integer publicationMinutesDebut;
	private String publicationZonCd;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Publication immediate'.
	 * @return Boolean publishNow
	 */
	@Field(smartType = "STyPBooleen", label = "Publication immediate")
	public Boolean getPublishNow() {
		return publishNow;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Publication immediate'.
	 * @param publishNow Boolean
	 */
	public void setPublishNow(final Boolean publishNow) {
		this.publishNow = publishNow;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de début'.
	 * @return LocalDate dateLocaleDebut <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPLocalDate", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Date de début")
	public java.time.LocalDate getDateLocaleDebut() {
		return dateLocaleDebut;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de début'.
	 * @param dateLocaleDebut LocalDate <b>Obligatoire</b>
	 */
	public void setDateLocaleDebut(final java.time.LocalDate dateLocaleDebut) {
		this.dateLocaleDebut = dateLocaleDebut;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de fin (incluse)'.
	 * @return LocalDate dateLocaleFin <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPLocalDate", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Date de fin (incluse)")
	public java.time.LocalDate getDateLocaleFin() {
		return dateLocaleFin;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de fin (incluse)'.
	 * @param dateLocaleFin LocalDate <b>Obligatoire</b>
	 */
	public void setDateLocaleFin(final java.time.LocalDate dateLocaleFin) {
		this.dateLocaleFin = dateLocaleFin;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de publication'.
	 * @return LocalDate publicationDateLocale
	 */
	@Field(smartType = "STyPLocalDate", label = "Date de publication")
	public java.time.LocalDate getPublicationDateLocale() {
		return publicationDateLocale;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de publication'.
	 * @param publicationDateLocale LocalDate
	 */
	public void setPublicationDateLocale(final java.time.LocalDate publicationDateLocale) {
		this.publicationDateLocale = publicationDateLocale;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Heure de publication'.
	 * @return Integer publicationMinutesDebut
	 */
	@Field(smartType = "STyPHeureMinute", label = "Heure de publication")
	public Integer getPublicationMinutesDebut() {
		return publicationMinutesDebut;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Heure de publication'.
	 * @param publicationMinutesDebut Integer
	 */
	public void setPublicationMinutesDebut(final Integer publicationMinutesDebut) {
		this.publicationMinutesDebut = publicationMinutesDebut;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Fuseau horaire publication'.
	 * @return String publicationZonCd
	 */
	@Field(smartType = "STyPCode", label = "Fuseau horaire publication")
	public String getPublicationZonCd() {
		return publicationZonCd;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Fuseau horaire publication'.
	 * @param publicationZonCd String
	 */
	public void setPublicationZonCd(final String publicationZonCd) {
		this.publicationZonCd = publicationZonCd;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
