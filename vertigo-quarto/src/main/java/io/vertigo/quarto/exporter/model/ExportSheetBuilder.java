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
import java.util.Collection;
import java.util.List;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Builder;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.structure.definitions.DtDefinition;
import io.vertigo.datamodel.structure.definitions.DtField;
import io.vertigo.datamodel.structure.definitions.DtFieldName;
import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.datamodel.structure.model.DtObject;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * Parametre d'export pour les données de type DT.
 * La particularité est que l'on fournit la liste des colonnes du DT a exporter,
 * avec éventuellement des paramètres d'affichage particulier pour une colonne.
 * @author pchretien, npiedeloup
 */
public final class ExportSheetBuilder implements Builder<ExportSheet> {
	/**
	 * List des champs à exporter
	 */
	private final List<ExportField> exportFields = new ArrayList<>();

	/**
	 * Objet à exporter. dto XOR dtc est renseigné.
	 */
	private final DtObject dto;
	private final DtList<?> dtc;
	private final DtDefinition dtDefinition;

	private final String title;
	private final ExportBuilder exportBuilder;

	/**
	 * Constructor.
	 * @param exportBuilder Parent ExportBuilder
	 * @param dto Object to export
	 * @param title Sheet title
	 */
	ExportSheetBuilder(final ExportBuilder exportBuilder, final DtObject dto, final String title) {
		Assertion.check()
				.isNotNull(exportBuilder)
				.isNotNull(dto);
		// title may be null
		//-----
		this.exportBuilder = exportBuilder;
		this.dto = dto;
		dtc = null;
		this.title = title;
		dtDefinition = DtObjectUtil.findDtDefinition(dto);
	}

	/**
	 * Constructor.
	 * @param exportBuilder Parent ExportBuilder
	 * @param dtc List to export
	 * @param title Sheet title
	 */
	ExportSheetBuilder(final ExportBuilder exportBuilder, final DtList<?> dtc, final String title) {
		Assertion.check()
				.isNotNull(exportBuilder)
				.isNotNull(dtc);
		// title may be null
		//-----
		this.exportBuilder = exportBuilder;
		this.dtc = dtc;
		dto = null;
		this.title = title;
		dtDefinition = dtc.getDefinition();
	}

	/**
	 * Ajoute un champs du Dt dans l'export, le label de la colonne sera celui indiqué dans le DT pour ce champs.
	 * @param fieldName ajout d'un champs du Dt à exporter
	 * @return ExportSheetBuilder
	 */
	public ExportSheetBuilder addField(final DtFieldName fieldName) {
		addField(fieldName, null);
		return this;
	}

	/**
	 * Ajoute un champs du Dt dans l'export, le label de la colonne sera celui indiqué dans le DT pour ce champs.
	 * @param fieldName ajout d'un champs du Dt à exporter
	 * @param list Liste des éléments dénormés
	 * @param displayfield Field du libellé à utiliser.
	 * @return ExportSheetBuilder
	 */
	public ExportSheetBuilder addField(final DtFieldName fieldName, final DtList<?> list, final DtFieldName displayfield) {
		addField(fieldName, list, displayfield, null);
		return this;
	}

	/**
	 * @param fieldName ajout d'un champs du Dt à exporter
	 * @param overridedLabel nom spécifique à utiliser dans l'export, null si l'on souhaite utiliser celui indiqué dans le DT pour ce champs
	 * @return ExportSheetBuilder
	 */
	public ExportSheetBuilder addField(final DtFieldName fieldName, final LocaleMessageText overridedLabel) {
		Assertion.check()
				.isNotNull(fieldName)
				// On vérifie que la colonne est bien dans la définition de la DTC
				.isTrue(dtDefinition.contains(fieldName.name()), "Le champ " + fieldName.name() + " n'est pas dans la liste à exporter");
		// On ne vérifie pas que les champs ne sont placés qu'une fois
		// car pour des raisons diverses ils peuvent l'être plusieurs fois.
		//-----
		final ExportField exportField = new ExportField(resolveDtField(fieldName), overridedLabel);
		exportFields.add(exportField);
		return this;
	}

	/**
	 * @param fieldName ajout d'un champs du Dt à exporter
	 * @param list Liste des éléments dénormés
	 * @param displayfield Field du libellé à utiliser.
	 * @param overridedLabel nom spécifique à utiliser dans l'export, null si l'on souhaite utiliser celui indiqué dans le DT pour ce champs
	 * @return ExportSheetBuilder
	 */
	public ExportSheetBuilder addField(final DtFieldName fieldName, final DtList<?> list, final DtFieldName displayfield, final LocaleMessageText overridedLabel) {
		final DtFieldName keyFieldName = () -> list.getDefinition().getKeyField().orElseGet(() -> list.getDefinition().getIdField().get()).name();
		return addField(fieldName, list, keyFieldName, displayfield, overridedLabel);
	}

	/**
	 * @param fieldName ajout d'un champs du Dt à exporter
	 * @param list Liste des éléments dénormés
	 * @param keyfield Field du libellé à utiliser.
	 * @param displayfield Field du libellé à utiliser.
	 * @param overridedLabel nom spécifique à utiliser dans l'export, null si l'on souhaite utiliser celui indiqué dans le DT pour ce champs
	 * @return ExportSheetBuilder
	 */
	public ExportSheetBuilder addField(final DtFieldName fieldName, final DtList<?> list, final DtFieldName keyfield, final DtFieldName displayfield, final LocaleMessageText overridedLabel) {
		Assertion.check()
				.isNotNull(fieldName)
				// On vérifie que la colonne est bien dans la définition de la DTC
				.isTrue(dtDefinition.contains(fieldName.name()), "Le champ " + fieldName.name() + " n'est pas dans la liste à exporter")
				.isTrue(list.getDefinition().contains(keyfield.name()), "Le champ " + keyfield.name() + " n'est pas dans la liste de dénorm")
				.isTrue(list.getDefinition().contains(displayfield.name()), "Le champ " + displayfield.name() + " n'est pas dans la liste de dénorm");
		// On ne vérifie pas que les champs ne sont placés qu'une fois
		// car pour des raisons diverses ils peuvent l'être plusieurs fois.
		//-----
		final ExportField exportField = new ExportDenormField(resolveDtField(fieldName), overridedLabel, list,
				resolveDtField(keyfield, list.getDefinition()), resolveDtField(displayfield, list.getDefinition()));
		exportFields.add(exportField);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ExportSheet build() {
		if (exportFields.isEmpty()) {
			// si la liste des colonnes est vide alors par convention on les
			// prend toutes.
			final Collection<DtField> fields = dtDefinition.getFields();
			for (final DtField dtField : fields) {
				exportFields.add(new ExportField(dtField, null));
			}
		}
		return new ExportSheet(title, exportFields, dto, dtc);
	}

	/**
	 * Close sheet.
	 * @return ExportBuilder
	 */
	public ExportBuilder endSheet() {
		return exportBuilder.addSheet(build());
	}

	private DtField resolveDtField(final DtFieldName fieldName) {
		return resolveDtField(fieldName, dtDefinition);
	}

	private static DtField resolveDtField(final DtFieldName fieldName, final DtDefinition definition) {
		return definition.getField(fieldName);
	}
}
