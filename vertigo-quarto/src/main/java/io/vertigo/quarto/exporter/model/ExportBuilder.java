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
package io.vertigo.quarto.exporter.model;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Builder;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.quarto.exporter.model.Export.Orientation;

/**
 * Builder des données à exporter.
 * @author pchretien, npiedeloup
 */
public final class ExportBuilder implements Builder<Export> {
	private final List<ExportSheet> sheets = new ArrayList<>();

	private final ExportFormat format;
	private final String fileName;

	// Variables à affecter par des SETTERS
	private String myTitle;
	private String myAuthor;
	private Export.Orientation myOrientation = Orientation.Portait;

	/**
	 * Constructor.
	 * @param format type du format de sortie. Ceci configurera le Handler de traitement de l'edition
	 * @param fileName nom du fichier de sortie.
	 */
	public ExportBuilder(final ExportFormat format, final String fileName) {
		Assertion.check()
				.isNotNull(format)
				.isNotBlank(fileName, "FileName doit être non vide");
		//-----
		this.format = format;
		this.fileName = fileName;
	}

	/**
	 * @param title Titre du document (Facultatif)
	 */
	public ExportBuilder withTitle(final String title) {
		myTitle = title;
		return this;
	}

	/**
	 * @param author Auteur du document (Facultatif)
	 */
	public ExportBuilder withAuthor(final String author) {
		myAuthor = author;
		return this;
	}

	/**
	 * @param orientation Orientation du document (Facultatif, mode portrait par défaut)
	 */
	public ExportBuilder withOrientation(final Orientation orientation) {
		myOrientation = orientation;
		return this;
	}

	/**
	 * @param dto DTO à exporter
	 * @param title Titre de l'objet
	 * @return Parametre d'export pour une donnée de type DtObject
	 */
	public ExportSheetBuilder beginSheet(final DataObject dto, final String title) {
		return new ExportSheetBuilder(this, dto, title);
	}

	/**
	 * @param dtc DTC à exporter
	 * @param title Titre de la liste
	 * @return Parametre d'export pour une donnée de type DtList
	 */
	public ExportSheetBuilder beginSheet(final DtList<?> dtc, final String title) {
		return new ExportSheetBuilder(this, dtc, title);
	}

	/**
	 * @param sheet parametre de données(DTO ou DTC) à ajouter à ce document.
	 */
	ExportBuilder addSheet(final ExportSheet sheet) {
		Assertion.check().isNotNull(sheet);
		//-----
		sheets.add(sheet);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public Export build() {
		return new Export(format, fileName, myTitle, myAuthor, myOrientation, sheets);
	}
}
