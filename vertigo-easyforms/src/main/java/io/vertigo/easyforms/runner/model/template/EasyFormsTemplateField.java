package io.vertigo.easyforms.runner.model.template;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;

public class EasyFormsTemplateField implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String code;
	private final String fieldTypeName;
	private String _label;
	private String _tooltip;
	private boolean _isDefault;
	private boolean _isMandatory;
	private EasyFormsData _parameters; // Field type parameters
	private List<EasyFormsTemplateFieldValidator> _validators;

	public EasyFormsTemplateField(final String code, final String fieldTypeName) {
		this.code = code;
		this.fieldTypeName = fieldTypeName;
		_label = code;
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
		return _label;
	}

	public EasyFormsTemplateField withLabel(final String label) {
		this._label = label;
		return this;
	}

	public String getTooltip() {
		return _tooltip;
	}

	public EasyFormsTemplateField withTooltip(final String tooltip) {
		this._tooltip = tooltip;
		return this;
	}

	public boolean isDefault() {
		return _isDefault;
	}

	public EasyFormsTemplateField withDefault() {
		return withDefault(true);
	}

	public EasyFormsTemplateField withDefault(final boolean isDefault) {
		this._isDefault = isDefault;
		return this;
	}

	public boolean isMandatory() {
		return _isMandatory;
	}

	public EasyFormsTemplateField withMandatory() {
		return withMandatory(true);
	}

	public EasyFormsTemplateField withMandatory(final boolean isMandatory) {
		this._isMandatory = isMandatory;
		return this;
	}

	public EasyFormsData getParameters() {
		return _parameters;
	}

	public EasyFormsTemplateField withParameters(final Map<String, Object> parameters) {
		return withParameters(new EasyFormsData(parameters));
	}

	public EasyFormsTemplateField withParameters(final EasyFormsData parameters) {
		this._parameters = parameters;
		return this;
	}

	public List<EasyFormsTemplateFieldValidator> getValidators() {
		if (_validators == null) {
			return List.of();
		}
		return _validators;
	}

	public EasyFormsTemplateField withValidators(final List<EasyFormsTemplateFieldValidator> validators) {
		this._validators = validators;
		return this;
	}

}
