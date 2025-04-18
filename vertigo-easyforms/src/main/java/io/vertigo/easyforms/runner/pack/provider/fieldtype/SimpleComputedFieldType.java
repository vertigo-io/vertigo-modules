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

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;

public class SimpleComputedFieldType extends SimpleFieldType {

	public SimpleComputedFieldType(final Enum<?> smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
		super(smartType, uiComponent);
	}

	public SimpleComputedFieldType(final String category, final Enum<?> smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
		super(category, smartType, uiComponent);
	}

	@Override
	public boolean isComputed() {
		return true;
	}

}
