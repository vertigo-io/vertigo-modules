package io.vertigo.easyforms.runner.model.template.item;

import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateFieldValidator;

public final class EasyFormsTemplateItemField extends AbstractEasyFormsTemplateItem {
	private static final long serialVersionUID = 1L;

	private String code;
	private String fieldTypeName;
	private Map<String, String> label;
	private String tooltip;
	private boolean isDefault;
	private boolean isMandatory;
	private EasyFormsData parameters; // Field type parameters
	private List<EasyFormsTemplateFieldValidator> validators;
	private Integer maxItems; // When is list, provide maximum number of elements

	public EasyFormsTemplateItemField() {
		// empty constructor needed for json deserialization
	}

	public EasyFormsTemplateItemField(final String code, final String fieldTypeName) {
		this.code = code;
		this.fieldTypeName = fieldTypeName;
		label = Map.of("fr", code);
	}

	public EasyFormsTemplateItemField(final String code, final EasyFormsFieldTypeDefinition fieldType) {
		this(code, fieldType.getName());
	}

	public EasyFormsTemplateItemField(final String code, final EnumDefinition<EasyFormsFieldTypeDefinition, ?> fieldTypeEnun) {
		this(code, fieldTypeEnun.getDefinitionName());
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getFieldTypeName() {
		return fieldTypeName;
	}

	public void setFieldTypeName(final String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	public Map<String, String> getLabel() {
		return label;
	}

	public String getUserLabel(final String lang) {
		var value = label.get(lang);
		if (value == null) {
			value = label.get("fr");
		}
		if (value == null) {
			value = label.get("i18n");
		}
		return value;
	}

	public void setLabel(final Map<String, String> label) {
		this.label = label;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(final boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(final boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public EasyFormsData getParameters() {
		return parameters;
	}

	public void setParameters(final EasyFormsData parameters) {
		this.parameters = parameters;
	}

	public List<EasyFormsTemplateFieldValidator> getValidators() {
		return validators;
	}

	public void setValidators(final List<EasyFormsTemplateFieldValidator> validators) {
		this.validators = validators;
	}

	public Integer getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(final Integer maxItems) {
		this.maxItems = maxItems;
	}

	// below shoud be refactored into a proper builder
	public EasyFormsTemplateItemField withLabel(final Map<String, String> myLabel) {
		label = myLabel;
		return this;
	}

	public EasyFormsTemplateItemField withTooltip(final String myTooltip) {
		tooltip = myTooltip;
		return this;
	}

	public EasyFormsTemplateItemField withDefault() {
		return withDefault(true);
	}

	public EasyFormsTemplateItemField withDefault(final boolean myDefault) {
		isDefault = myDefault;
		return this;
	}

	public EasyFormsTemplateItemField withMandatory() {
		return withMandatory(true);
	}

	public EasyFormsTemplateItemField withMandatory(final boolean myMandatory) {
		isMandatory = myMandatory;
		return this;
	}

	public EasyFormsTemplateItemField withParameters(final Map<String, Object> myParameters) {
		return withParameters(new EasyFormsData(myParameters));
	}

	public EasyFormsTemplateItemField withParameters(final EasyFormsData myParameters) {
		parameters = myParameters;
		return this;
	}

	public EasyFormsTemplateItemField withValidators(final List<EasyFormsTemplateFieldValidator> myValidators) {
		validators = myValidators;
		return this;
	}

	public EasyFormsTemplateItemField withMaxItems(final Integer myMaxItems) {
		maxItems = myMaxItems;
		return this;
	}

}
