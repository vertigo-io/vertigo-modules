/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2021, Vertigo.io, team@vertigo.io
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
package io.vertigo.ui.data.domain.movies;

import java.util.List;

import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.KeyConcept;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.structure.stereotype.DisplayField;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.stereotype.SortField;
import io.vertigo.datamodel.structure.util.DtObjectUtil;
import io.vertigo.datastore.filestore.model.FileInfoURI;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class Movie implements KeyConcept {
	private static final long serialVersionUID = 1L;

	private Long movId;
	private String title;
	private java.time.LocalDate released;
	private Integer year;
	private Integer runtime;
	private String description;
	private String poster;
	private String rated;
	private java.time.Instant lastModified;
	private String tags;
	private List<FileInfoURI> pictures;

	/** {@inheritDoc} */
	@Override
	public UID<Movie> getUID() {
		return UID.of(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'MOV_ID'.
	 * @return Long movId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyId", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "MOV_ID")
	public Long getMovId() {
		return movId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'MOV_ID'.
	 * @param movId Long <b>Obligatoire</b>
	 */
	public void setMovId(final Long movId) {
		this.movId = movId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'TITLE'.
	 * @return String title
	 */
	@Field(smartType = "STyLabelLong", label = "TITLE")
	@SortField
	@DisplayField
	public String getTitle() {
		return title;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'TITLE'.
	 * @param title String
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Released'.
	 * @return LocalDate released
	 */
	@Field(smartType = "STyDate", label = "Released")
	public java.time.LocalDate getReleased() {
		return released;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Released'.
	 * @param released LocalDate
	 */
	public void setReleased(final java.time.LocalDate released) {
		this.released = released;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Year'.
	 * @return Integer year
	 */
	@Field(smartType = "STyYear", label = "Year")
	public Integer getYear() {
		return year;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Year'.
	 * @param year Integer
	 */
	public void setYear(final Integer year) {
		this.year = year;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Runtime'.
	 * @return Integer runtime
	 */
	@Field(smartType = "STyDuration", label = "Runtime")
	public Integer getRuntime() {
		return runtime;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Runtime'.
	 * @param runtime Integer
	 */
	public void setRuntime(final Integer runtime) {
		this.runtime = runtime;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Description'.
	 * @return String description
	 */
	@Field(smartType = "STyComment", label = "Description")
	public String getDescription() {
		return description;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Description'.
	 * @param description String
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Poster'.
	 * @return String poster
	 */
	@Field(smartType = "STyLabelLong", label = "Poster")
	public String getPoster() {
		return poster;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Poster'.
	 * @param poster String
	 */
	public void setPoster(final String poster) {
		this.poster = poster;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Poster'.
	 * @return String poster
	 */
	@Field(smartType = "STyMultipleIds", label = "Tags")
	public String getTags() {
		return tags;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Poster'.
	 * @param poster String
	 */
	public void setTags(final String tags) {
		this.tags = tags;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'rated'.
	 * @return String rated
	 */
	@Field(smartType = "STyLabelLong", label = "rated")
	public String getRated() {
		return rated;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'rated'.
	 * @param rated String
	 */
	public void setRated(final String rated) {
		this.rated = rated;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'lastModified'.
	 * @return Instant lastModified
	 */
	@Field(smartType = "STyLastModified", label = "lastModified")
	public java.time.Instant getLastModified() {
		return lastModified;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'lastModified'.
	 * @param lastModified Instant
	 */
	public void setLastModified(final java.time.Instant lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Pictures'.
	 * @return List<FileInfoURI> pictures
	 */
	@Field(smartType = "STyFileInfoURI", cardinality = Cardinality.MANY, persistent = false, label = "Pictures")
	public List<FileInfoURI> getPictures() {
		return pictures;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Pictures'.
	 * @param pictures List<FileInfoURI>
	 */
	public void setPictures(final List<FileInfoURI> pictures) {
		this.pictures = pictures;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
