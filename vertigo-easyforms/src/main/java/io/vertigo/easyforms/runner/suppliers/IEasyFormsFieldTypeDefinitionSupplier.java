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
package io.vertigo.easyforms.runner.suppliers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.easyforms.runner.Resources;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateSection;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public interface IEasyFormsFieldTypeDefinitionSupplier {

	public static final String DEFAULT_CATEGORY = "default";

	public default EasyFormsFieldTypeDefinition get(final String definitionName) {
		final var uiComponentParams = getExposedComponentParams();

		final EasyFormsTemplate template;
		if (uiComponentParams == null || uiComponentParams.isEmpty()) {
			template = null;
		} else {
			for (final var uiComponentParam : uiComponentParams) {
				if (uiComponentParam instanceof final EasyFormsTemplateItemField field) {
					// default i18n label for fieldType
					field
							.withLabel(Map.of("i18n", definitionName + '$' + field.getCode() + "Label"));
				}
			}
			template = new EasyFormsTemplate(List.of(new EasyFormsTemplateSection(null, null, null, uiComponentParams))); // TODO
		}

		return EasyFormsFieldTypeDefinition.of(definitionName, getCategory(), getSmartType().name(), getUiComponent().getDefinitionName(), getDefaultValue(), getUiParams(), template,
				isList(), getMinListSizeResource(), getMaxListSizeResource(),
				getConstraintsProvider());
	}

	public default String getCategory() {
		return DEFAULT_CATEGORY;
	}

	public abstract Enum<?> getSmartType();

	public abstract EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent();

	public default Map<String, Object> getUiParams() {
		return Map.of();
	}

	public default List<AbstractEasyFormsTemplateItem> getExposedComponentParams() {
		return List.of();
	}

	public default Object getDefaultValue() {
		return null;
	}

	public default boolean isList() {
		return false;
	}

	public default Resources getMinListSizeResource() {
		return Resources.EfMinListSize;
	}

	public default Resources getMaxListSizeResource() {
		return Resources.EfMaxListSize;
	}

	public default Function<EasyFormsTemplateItemField, List<Constraint>> getConstraintsProvider() {
		return null;
	}

}
