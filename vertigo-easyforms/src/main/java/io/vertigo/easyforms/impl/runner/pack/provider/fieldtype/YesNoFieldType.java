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
package io.vertigo.easyforms.impl.runner.pack.provider.fieldtype;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.RadioCheckUiComponent;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.ui.EasyFormsListItem;

public class YesNoFieldType implements IEasyFormsFieldTypeDefinitionSupplier {
	@Override
	public EasyFormsSmartTypes getSmartType() {
		return EasyFormsSmartTypes.EfBoolean;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return UiComponentEnum.RADIO;
	}

	@Override
	public Map<String, Object> getUiParams() {
		return Map.of(
				RadioCheckUiComponent.LAYOUT, RadioCheckUiComponent.VALUE_HORIZONTAL,
				IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
				IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
				(Serializable) List.of(
						new EasyFormsListItem(true, "#{EfTrue}"),
						new EasyFormsListItem(false, "#{EfFalse}")));
	}
}
