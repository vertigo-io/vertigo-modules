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
package io.vertigo.easyforms.runner.pack.provider.uicomponent;

import java.util.List;

import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;

public class SelectUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

	public static final String SEARCHABLE = "selectSearchable";

	@Override
	public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
		return List.of(
				IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_FIELD_PARAM,
				new EasyFormsTemplateItemField(SEARCHABLE, FieldTypeEnum.YES_NO));
	}

}