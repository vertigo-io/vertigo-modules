package io.vertigo.easyforms.easyformsrunner.model;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.lang.Builder;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate.Field;

public class EasyFormsTemplateBuilder implements Builder<EasyFormsTemplate> {

	private final List<Field> fields = new ArrayList<>();

	public EasyFormsTemplateBuilder addField(
			final String fieldCode,
			final EasyFormsFieldType fieldType,
			final String label,
			final String tooltip,
			final boolean isDefault,
			final boolean isMandatory,
			final List<String> fieldValidators) {
		final var field = new Field();
		field.setFieldCode(fieldCode);
		field.setFieldType(fieldType.getName());
		field.setLabel(label);
		field.setTooltip(tooltip);
		field.setOrder(fields.size() + 1);
		field.setDefault(isDefault);
		field.setMandatory(isMandatory);
		field.setFieldValidators(fieldValidators);
		//-- ajouter à la list
		fields.add(field);
		return this;
	}

	@Override
	public EasyFormsTemplate build() {
		return new EasyFormsTemplate(fields);
	}

}
