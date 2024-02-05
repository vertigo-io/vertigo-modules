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

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.data.definitions.DataField;

/**
 * Définition d'une colonne à exporter.
 *
 * @author pchretien, npiedeloup
 */
public class ExportField {
	private final DataField dtField;
	private final LocaleMessageText label;

	/**
	 * Constructor.
	 * @param dtField DataField
	 */
	public ExportField(final DataField dtField, final LocaleMessageText label) {
		Assertion.check().isNotNull(dtField);
		//label may be null
		//-----
		this.dtField = dtField;
		this.label = label;
	}

	/**
	 * @return DataField
	 */
	public final DataField getDataField() {
		return dtField;
	}

	/**
	 * @return Label du dtField
	 */
	public final LocaleMessageText getLabel() {
		//Selon que le label est surchargé ou non
		return label != null ? label : dtField.getLabel();
	}

}
