package io.vertigo.easyforms.easyformsrunner.model.template;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldType;

public class EasyFormsTemplateField implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String code;
	private final String fieldTypeName;
	private String label;
	private String tooltip;
	private Integer order;
	private boolean isDefault;
	private boolean isMandatory;
	private EasyFormsData parameters; // Field type parameters
	private List<EasyFormsTemplateFieldValidator> validators;

	public EasyFormsTemplateField(final String code, final String fieldTypeName) {
		this.code = code;
		this.fieldTypeName = fieldTypeName;
		label = code;
	}

	public EasyFormsTemplateField(final String code, final EasyFormsFieldType fieldType) {
		this(code, fieldType.getName());
	}

	public EasyFormsTemplateField(final String code, final EnumDefinition<EasyFormsFieldType, ?> fieldTypeEnun) {
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

	public EasyFormsTemplateField withLabel(final String label) {
		this.label = label;
		return this;
	}

	public String getTooltip() {
		return tooltip;
	}

	public EasyFormsTemplateField withTooltip(final String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public Integer getOrder() {
		return order;
	}

	public EasyFormsTemplateField withOrder(final Integer order) {
		this.order = order;
		return this;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public EasyFormsTemplateField withDefault() {
		return withDefault(true);
	}

	public EasyFormsTemplateField withDefault(final boolean isDefault) {
		this.isDefault = isDefault;
		return this;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public EasyFormsTemplateField withMandatory() {
		return withMandatory(true);
	}

	public EasyFormsTemplateField withMandatory(final boolean isMandatory) {
		this.isMandatory = isMandatory;
		return this;
	}

	public EasyFormsData getParameters() {
		return parameters;
	}

	public EasyFormsTemplateField withParameters(final Map<String, Object> parameters) {
		return withParameters(new EasyFormsData(parameters));
	}

	public EasyFormsTemplateField withParameters(final EasyFormsData parameters) {
		this.parameters = parameters;
		return this;
	}

	public List<EasyFormsTemplateFieldValidator> getValidators() {
		if (validators == null) {
			return List.of();
		}
		return validators;
	}

	public EasyFormsTemplateField withValidators(final List<EasyFormsTemplateFieldValidator> validators) {
		this.validators = validators;
		return this;
	}

}
