package io.vertigo.easyforms.impl.easyformsrunner.suppliers;

import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateField;
import io.vertigo.easyforms.impl.easyformsrunner.library.EasyFormsSmartTypes;

public interface IEasyFormsFieldTypeDefinitionSupplier {

	public default EasyFormsFieldTypeDefinition get(final String definitionName) {
		final var uiComponentParams = getExposedComponentParams();

		final EasyFormsTemplate template;
		if (uiComponentParams == null || uiComponentParams.isEmpty()) {
			template = null;
		} else {
			for (final var uiComponentParam : uiComponentParams) {
				// default i18n label for fieldType
				uiComponentParam
						.withLabel(definitionName + '$' + uiComponentParam.getCode() + "Label");
			}
			template = new EasyFormsTemplate(uiComponentParams);
		}

		return EasyFormsFieldTypeDefinition.of(definitionName, getSmartType().name(), getUiComponent().getDefinitionName(), getDefaultValue(), getUiParams(), template, isList());
	}

	public abstract EasyFormsSmartTypes getSmartType();

	public abstract EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent();

	public default Map<String, Object> getUiParams() {
		return Map.of();
	}

	public default List<EasyFormsTemplateField> getExposedComponentParams() {
		return List.of();
	}

	public default Object getDefaultValue() {
		return null;
	}

	public default boolean isList() {
		return false;
	}

}
