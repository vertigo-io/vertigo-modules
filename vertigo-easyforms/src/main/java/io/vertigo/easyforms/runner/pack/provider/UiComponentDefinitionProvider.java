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
package io.vertigo.easyforms.runner.pack.provider;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.pack.provider.uicomponent.FileUiComponent;
import io.vertigo.easyforms.runner.pack.provider.uicomponent.RadioCheckUiComponent;
import io.vertigo.easyforms.runner.pack.provider.uicomponent.SelectUiComponent;
import io.vertigo.easyforms.runner.pack.provider.uicomponent.TextAreaUiComponent;
import io.vertigo.easyforms.runner.pack.provider.uicomponent.TextFieldUiComponent;
import io.vertigo.easyforms.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;

public final class UiComponentDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsUiComponentDefinition> {

	public enum UiComponentEnum implements EnumDefinition<EasyFormsUiComponentDefinition, UiComponentEnum> {

		TEXT_FIELD(new TextFieldUiComponent()), //
		TEXT_AREA(new TextAreaUiComponent()), //

		CHECKBOX(new RadioCheckUiComponent()), //
		RADIO(new RadioCheckUiComponent()), //

		NUMBER, //

		DATE, //
		DATE_TIME, //

		SLIDER, //

		SELECT(new SelectUiComponent()), //

		FILE(new FileUiComponent()), //

		// Internal use
		INTERNAL_MAP,//
		;

		// ---

		private final String definitionName;
		private final IEasyFormsUiComponentDefinitionSupplier componentSupplier;

		UiComponentEnum() {
			this(IEasyFormsUiComponentDefinitionSupplier.NO_PARAM);
		}

		UiComponentEnum(final IEasyFormsUiComponentDefinitionSupplier componentSupplier) {
			definitionName = EasyFormsUiComponentDefinition.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.componentSupplier = componentSupplier;
		}

		@Override
		public EasyFormsUiComponentDefinition buildDefinition(final DefinitionSpace definitionSpace) {
			return componentSupplier.get(definitionName);
		}

		@Override
		public EasyFormsUiComponentDefinition get() {
			return EasyFormsUiComponentDefinition.resolve(definitionName);
		}

		@Override
		public String getDefinitionName() {
			return definitionName;
		}
	}

	@Override
	public Class<UiComponentEnum> getEnumClass() {
		return UiComponentEnum.class;
	}

}
