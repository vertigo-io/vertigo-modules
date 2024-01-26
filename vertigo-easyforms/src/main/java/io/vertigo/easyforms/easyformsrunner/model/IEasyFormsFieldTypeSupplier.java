package io.vertigo.easyforms.easyformsrunner.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsUiComponent.UiComponentParam;
import io.vertigo.easyforms.impl.easyformsrunner.library.EasyFormsSmartTypes;

public interface IEasyFormsFieldTypeSupplier {

	public default EasyFormsFieldType get(final String definitionName) {
		final var uiComponentParams = getUiComponentParams();

		final EasyFormsTemplate template;
		if (uiComponentParams == null || uiComponentParams.isEmpty()) {
			template = null;
		} else {
			final var easyFormsTemplateBuilder = new EasyFormsTemplateBuilder();
			for (final var uiComponentParam : uiComponentParams) {
				easyFormsTemplateBuilder.addField(
						uiComponentParam.fieldCode(),
						uiComponentParam.fieldTypeEnum(),
						uiComponentParam.fieldCode(),
						uiComponentParam.tooltip(),
						true,
						uiComponentParam.isMandatory(),
						uiComponentParam.fieldValidators());
			}
			template = easyFormsTemplateBuilder.build();
		}

		return EasyFormsFieldType.of(definitionName, getSmartType().name(), getUiComponent().getDefinitionName(), getUiParams(), template);
	}

	public abstract EasyFormsSmartTypes getSmartType();

	public abstract EnumDefinition<EasyFormsUiComponent, ?> getUiComponent();

	public default Map<String, Serializable> getUiParams() {
		return Map.of();
	}

	public default List<UiComponentParam> getUiComponentParams() {
		return List.of();
	}

}
