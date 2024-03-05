package io.vertigo.easyforms.runner.model.template;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;

public final class EasyFormsTemplateField implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String code;
	private final String fieldTypeName;
	private String label;
	private String tooltip;
	private boolean isDefault;
	private boolean isMandatory;
	private EasyFormsData parameters; // Field type parameters
	private List<EasyFormsTemplateFieldValidator> validators;

	public EasyFormsTemplateField(final String code, final String fieldTypeName) {
		this.code = code;
		this.fieldTypeName = fieldTypeName;
		label = code;
	}

	public EasyFormsTemplateField(final String code, final EasyFormsFieldTypeDefinition fieldType) {
		this(code, fieldType.getName());
	}

	public EasyFormsTemplateField(final String code, final EnumDefinition<EasyFormsFieldTypeDefinition, ?> fieldTypeEnun) {
		this(code, fieldTypeEnun.getDefinitionName());
	}

	public String getCode() {
		return code;
	}

	public String getFieldTypeName() {
		return fieldTypeName;
	}

	public String getLabel() {
		return label;
	}

	public EasyFormsTemplateField withLabel(final String myLabel) {
		label = myLabel;
		return this;
	}

	public String getTooltip() {
		return tooltip;
	}

	public EasyFormsTemplateField withTooltip(final String myTooltip) {
		tooltip = myTooltip;
		return this;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public EasyFormsTemplateField withDefault() {
		return withDefault(true);
	}

	public EasyFormsTemplateField withDefault(final boolean myDefault) {
		isDefault = myDefault;
		return this;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public EasyFormsTemplateField withMandatory() {
		return withMandatory(true);
	}

	public EasyFormsTemplateField withMandatory(final boolean myMandatory) {
		isMandatory = myMandatory;
		return this;
	}

	public EasyFormsData getParameters() {
		return parameters;
	}

	public EasyFormsTemplateField withParameters(final Map<String, Object> parameters) {
		return withParameters(new EasyFormsData(parameters));
	}

	public EasyFormsTemplateField withParameters(final EasyFormsData myParameters) {
		parameters = myParameters;
		return this;
	}

	public List<EasyFormsTemplateFieldValidator> getValidators() {
		if (validators == null) {
			return List.of();
		}
		return validators;
	}

	public EasyFormsTemplateField withValidators(final List<EasyFormsTemplateFieldValidator> myValidators) {
		validators = myValidators;
		return this;
	}

}
