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
package io.vertigo.easyforms.impl.runner.pack.provider.uicomponent;

import java.util.List;

import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.easyforms.impl.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public class TextAreaUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

	public static final String AUTOGROW = "autogrow";

	@Override
	public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
		return List.of(
				new EasyFormsTemplateItemField(DtProperty.MAX_LENGTH.getName(), FieldTypeEnum.COUNT_STRICT),
				new EasyFormsTemplateItemField(AUTOGROW, FieldTypeEnum.YES_NO));
	}

}