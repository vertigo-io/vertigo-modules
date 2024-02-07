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
public final class AffectionReservation implements Data {
	private static final long serialVersionUID = 1L;

	private Long trhId;
	private String creIds;
	private Long creId;
	private Long recId;
	private java.time.Instant instantCreation;

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Id de tranche horaire'.
	 * @return Long trhId
	 */
	@Field(smartType = "STyPId", label = "Id de tranche horaire")
	public Long getTrhId() {
		return trhId;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Id de tranche horaire'.
	 * @param trhId Long
	 */
	public void setTrhId(final Long trhId) {
		this.trhId = trhId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Liste des creneaux disponibles'.
	 * @return String creIds
	 */
	@Field(smartType = "STyMultipleIds", label = "Liste des creneaux disponibles")
	public String getCreIds() {
		return creIds;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Liste des creneaux disponibles'.
	 * @param creIds String
	 */
	public void setCreIds(final String creIds) {
		this.creIds = creIds;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'creneau affecté'.
	 * @return Long creId
	 */
	@Field(smartType = "STyPId", label = "creneau affecté")
	public Long getCreId() {
		return creId;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'creneau affecté'.
	 * @param creId Long
	 */
	public void setCreId(final Long creId) {
		this.creId = creId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Id de la reservation'.
	 * @return Long recId
	 */
	@Field(smartType = "STyPId", label = "Id de la reservation")
	public Long getRecId() {
		return recId;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Id de la reservation'.
	 * @param recId Long
	 */
	public void setRecId(final Long recId) {
		this.recId = recId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de creation de la reservation'.
	 * @return Instant instantCreation
	 */
	@Field(smartType = "STyPInstant", label = "Date de creation de la reservation")
	public java.time.Instant getInstantCreation() {
		return instantCreation;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de creation de la reservation'.
	 * @param instantCreation Instant
	 */
	public void setInstantCreation(final java.time.Instant instantCreation) {
		this.instantCreation = instantCreation;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataUtil.toString(this);
	}
}
