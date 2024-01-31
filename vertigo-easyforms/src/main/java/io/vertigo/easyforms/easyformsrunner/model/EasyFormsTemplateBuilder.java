package io.vertigo.easyforms.easyformsrunner.model;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.lang.Builder;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate.Field;

public class EasyFormsTemplateBuilder implements Builder<EasyFormsTemplate> {

	//TODO: comme treeBuilder dans gaz ?

	private final List<Field> fields = new ArrayList<>();

	public EasyFormsTemplateBuilder addField(
			final String fieldCode,
			final EasyFormsFieldType fieldType,
			final String label,
			final String tooltip,
			final boolean isDefault,
			final boolean isMandatory,
			final EasyFormsParameterData parameters,
			final List<String> fieldValidators) {
		final var field = new Field();
		field.setCode(fieldCode);
		field.setFieldTypeName(fieldType.getName());
		field.setLabel(label);
		field.setTooltip(tooltip);
		field.setOrder(fields.size() + 1);
		field.setDefault(isDefault);
		field.setParameters(parameters);
		field.setMandatory(isMandatory);
		//	field.setFieldValidators(fieldValidators);
		//-- add to list
		fields.add(field);
		return this;
	}

	public EasyFormsTemplateBuilder addField(
			final String fieldCode,
			final EnumDefinition<EasyFormsFieldType, ?> fieldTypeEnum,
			final String label,
			final String tooltip,
			final boolean isDefault,
			final boolean isMandatory,
			EasyFormsParameterData parameters, final List<String> fieldValidators) {
		final var field = new Field();
		field.setCode(fieldCode);
		field.setFieldTypeName(fieldTypeEnum.getDefinitionName());
		field.setLabel(label);
		field.setTooltip(tooltip);
		field.setOrder(fields.size() + 1);
		field.setDefault(isDefault);
		field.setMandatory(isMandatory);
		//	field.setFieldValidators(fieldValidators);
		//-- add to list
		fields.add(field);
		return this;
	}

	@Override
	public EasyFormsTemplate build() {
		return new EasyFormsTemplate(fields);
	}

}
