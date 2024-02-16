package io.vertigo.easyforms.easyformsrunner.model.template;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.lang.Builder;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldType;

public class EasyFormsTemplateBuilder implements Builder<EasyFormsTemplate> {

	//TODO: comme treeBuilder dans gaz ?

	private final List<EasyFormsTemplateField> fields = new ArrayList<>();

	public EasyFormsTemplateBuilder addField(
			final String fieldCode,
			final EasyFormsFieldType fieldType,
			final String label,
			final String tooltip,
			final boolean isDefault,
			final boolean isMandatory,
			final EasyFormsData parameters,
			final List<EasyFormsTemplateFieldValidator> fieldValidators) {

		final var field = new EasyFormsTemplateField(fieldCode, fieldType)
				.withLabel(label)
				.withTooltip(tooltip)
				.withDefault(isDefault)
				.withParameters(parameters)
				.withMandatory(isMandatory)
				.withValidators(fieldValidators);
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
			final EasyFormsData parameters,
			final List<String> fieldValidators) {

		final var field = new EasyFormsTemplateField(fieldCode, fieldTypeEnum)
				.withLabel(label)
				.withTooltip(tooltip)
				.withDefault(isDefault)
				.withMandatory(isMandatory)
				.withValidators(fieldValidators.stream().map(EasyFormsTemplateFieldValidator::new).toList());
		//-- add to list
		fields.add(field);
		return this;
	}

	@Override
	public EasyFormsTemplate build() {
		return new EasyFormsTemplate(fields);
	}

}
