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
			final List<String> fieldConstraints) {
		final var champ = new Field();
		champ.setFieldCode(fieldCode);
		champ.setFieldType(fieldType.getName());
		champ.setLabel(label);
		champ.setTooltip(tooltip);
		champ.setOrder(fields.size() + 1);
		champ.setDefault(isDefault);
		champ.setMandatory(isMandatory);
		champ.setFieldConstraints(fieldConstraints);
		//-- ajouter à la list
		fields.add(champ);
		return this;
	}

	@Override
	public EasyFormsTemplate build() {
		return new EasyFormsTemplate(fields);
	}

}
