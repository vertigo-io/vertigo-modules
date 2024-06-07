package io.vertigo.easyforms.impl.runner.suppliers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
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

		return EasyFormsFieldTypeDefinition.of(definitionName, getCategory(), getSmartType().name(), getUiComponent().getDefinitionName(), getDefaultValue(), getUiParams(), template, isList(),
				getConstraintsProvider());
	}

	public default String getCategory() {
		return DEFAULT_CATEGORY;
	}

	public abstract EasyFormsSmartTypes getSmartType();

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

	public default Function<EasyFormsTemplateItemField, List<Constraint>> getConstraintsProvider() {
		return null;
	}

}
