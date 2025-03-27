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
package io.vertigo.easyforms.runner.pack.provider.fieldtype;

import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.runner.pack.provider.uicomponent.RadioCheckUiComponent;
import io.vertigo.easyforms.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;

public class CustomListFieldType implements IEasyFormsFieldTypeDefinitionSupplier {

	private final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent;

	public CustomListFieldType(final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
		this.uiComponent = uiComponent;
	}

	@Override
	public EasyFormsSmartTypes getSmartType() {
		return EasyFormsSmartTypes.EfLabel;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return uiComponent;
	}

	@Override
	public Map<String, Object> getUiParams() {
		return Map.of(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME);
	}

	@Override
	public List<AbstractEasyFormsTemplateItem> getExposedComponentParams() {
		if (uiComponent == UiComponentEnum.RADIO || uiComponent == UiComponentEnum.CHECKBOX) {
			return List.of(
					new EasyFormsTemplateItemField(RadioCheckUiComponent.LAYOUT, FieldTypeEnum.INTERNAL_LAYOUT)
							.withParameters(Map.of(RadioCheckUiComponent.LAYOUT, RadioCheckUiComponent.VALUE_HORIZONTAL))
							.withDefaultValue(RadioCheckUiComponent.VALUE_VERTICAL)
							.withMandatory(),
					new EasyFormsTemplateItemField(IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME, FieldTypeEnum.INTERNAL_MAP).withMandatory());
		}
		return List.of(new EasyFormsTemplateItemField(IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME, FieldTypeEnum.INTERNAL_MAP).withMandatory());
	}

	@Override
	public boolean isList() {
		return uiComponent == UiComponentEnum.CHECKBOX;
	}

}
