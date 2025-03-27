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

import java.util.function.Function;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.data.definitions.DataField;
import io.vertigo.datamodel.data.model.DataObject;

public class ExportCustomField extends ExportField {

	private Function<DataObject, String> getter;

	/**
	 * 
	 * @param dtField is mandatory for super class ExportField, provide any field as
	 *                it won't be used
	 * @param getter
	 * @param label
	 */
	public ExportCustomField(final DataField dtField, final Function<DataObject, String> getter,
			final LocaleMessageText label) {
		super(dtField, label);
		Assertion.check().isNotNull(getter);
		this.getter = getter;
	}

	/**
	 * Get value from DataObject
	 * 
	 * @param dto
	 * @return
	 */
	public String apply(DataObject dto) {
		return getter.apply(dto);
	}

}
