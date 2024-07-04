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
package io.vertigo.quarto.exporter.model;

import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.data.definitions.DataField;
import io.vertigo.datamodel.data.model.DtList;

/**
 * Définition d'une colonne de type dénormalisation à exporter. On précise la
 * liste et le champs a utiliser comme libellé à afficher à la place de l'id de
 * la liste de l'export.
 *
 * @author pchretien, npiedeloup
 */
public final class ExportDenormField extends ExportField {
	private final DtList<?> list;
	private final DataField keyField;
	private final DataField displayField;

	/**
	 * Constructor.
	 *
	 * @param dtField  Champ à exporter
	 * @param list Liste de éléments dénormés
	 * @param displayField Champs dénormé
	 */
	ExportDenormField(final DataField dtField, final LocaleMessageText label, final DtList<?> list, final DataField keyField, final DataField displayField) {
		super(dtField, label);
		this.list = list;
		this.keyField = keyField;
		this.displayField = displayField;
	}

	/**
	 * @return DtList<?> liste contenant les éléments dénormés.
	 */
	public DtList<?> getDenormList() {
		return list;
	}

	/**
	 * @return DataField représentant le display de la liste de dénorm.
	 */
	public DataField getDisplayField() {
		return displayField;
	}

	/**
	 * @return DataField représentant la clé de la liste de dénorm. (par défaut la  key du DT)
	 */
	public DataField getKeyField() {
		return keyField;
	}
}
