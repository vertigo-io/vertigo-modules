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
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.data.stereotype.Field;
import io.vertigo.datamodel.data.util.DataModelUtil;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class TrancheHoraire implements Entity {
	private static final long serialVersionUID = 1L;

	private Long trhId;
	private java.time.LocalDate dateLocale;
	private Integer minutesDebut;
	private Integer minutesFin;
	private Integer nbGuichet;
	private java.time.Instant instantPublication;

	@io.vertigo.datamodel.data.stereotype.Association(
			name = "ATranchePlageHoraire",
			fkFieldName = "plhId",
			primaryDtDefinitionName = "DtPlageHoraire",
			primaryIsNavigable = true,
			primaryRole = "PlageHoraire",
			primaryLabel = "Plage horaire",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtTrancheHoraire",
			foreignIsNavigable = false,
			foreignRole = "TrancheHoraire",
			foreignLabel = "TrancheHoraire",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.planning.agenda.domain.PlageHoraire> plhIdAccessor = new StoreVAccessor<>(io.vertigo.planning.agenda.domain.PlageHoraire.class, "PlageHoraire");

	@io.vertigo.datamodel.data.stereotype.Association(
			name = "ATrancheHoraireAgenda",
			fkFieldName = "ageId",
			primaryDtDefinitionName = "DtAgenda",
			primaryIsNavigable = true,
			primaryRole = "Agenda",
			primaryLabel = "Agenda",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtTrancheHoraire",
			foreignIsNavigable = false,
			foreignRole = "TrancheHoraire",
			foreignLabel = "TrancheHoraire",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.planning.agenda.domain.Agenda> ageIdAccessor = new StoreVAccessor<>(io.vertigo.planning.agenda.domain.Agenda.class, "Agenda");

	/** {@inheritDoc} */
	@Override
	public UID<TrancheHoraire> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id'.
	 * @return Long trhId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPId", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id")
	public Long getTrhId() {
		return trhId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id'.
	 * @param trhId Long <b>Obligatoire</b>
	 */
	public void setTrhId(final Long trhId) {
		this.trhId = trhId;
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
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Plage horaire'.
	 * @return Long plhId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.data.stereotype.ForeignKey(smartType = "STyPId", label = "Plage horaire", fkDefinition = "DtPlageHoraire", cardinality = io.vertigo.core.lang.Cardinality.ONE )
	public Long getPlhId() {
		return (Long) plhIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Plage horaire'.
	 * @param plhId Long <b>Obligatoire</b>
	 */
	public void setPlhId(final Long plhId) {
		plhIdAccessor.setId(plhId);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Agenda'.
	 * @return Long ageId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.data.stereotype.ForeignKey(smartType = "STyPId", label = "Agenda", fkDefinition = "DtAgenda", cardinality = io.vertigo.core.lang.Cardinality.ONE )
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

 	/**
	 * Association : Plage horaire.
	 * @return l'accesseur vers la propriété 'Plage horaire'
	 */
	public StoreVAccessor<io.vertigo.planning.agenda.domain.PlageHoraire> plageHoraire() {
		return plhIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
