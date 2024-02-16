package io.vertigo.easyforms.easyformsrunner.model.definitions;

import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateField;
import io.vertigo.easyforms.impl.easyformsrunner.library.EasyFormsSmartTypes;

public interface IEasyFormsFieldTypeSupplier {

	public default EasyFormsFieldType get(final String definitionName) {
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

		return EasyFormsFieldType.of(definitionName, getSmartType().name(), getUiComponent().getDefinitionName(), getDefaultValue(), getUiParams(), template);
	}

	public abstract EasyFormsSmartTypes getSmartType();

	public abstract EnumDefinition<EasyFormsUiComponent, ?> getUiComponent();

	public default Map<String, Object> getUiParams() {
		return Map.of();
	}

	public default List<EasyFormsTemplateField> getExposedComponentParams() {
		return List.of();
	}

	public default Object getDefaultValue() {
		return null;
	}

}
