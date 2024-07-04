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

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;

public class SimpleFieldType implements IEasyFormsFieldTypeDefinitionSupplier {
	private final String category;
	private final EasyFormsSmartTypes smartType;
	private final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent;

	public SimpleFieldType(final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
		this(DEFAULT_CATEGORY, smartType, uiComponent);
	}

	public SimpleFieldType(final String category, final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
		this.category = category;
		this.smartType = smartType;
		this.uiComponent = uiComponent;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public EasyFormsSmartTypes getSmartType() {
		return smartType;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return uiComponent;
	}
}