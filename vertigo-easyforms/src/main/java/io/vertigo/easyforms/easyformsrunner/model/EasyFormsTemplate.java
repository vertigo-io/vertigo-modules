package io.vertigo.easyforms.easyformsrunner.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;

public class EasyFormsTemplate implements Serializable {

	private final List<Field> fields;

	private static final long serialVersionUID = 1L;

	public EasyFormsTemplate(final List<Field> fields) {
		this.fields = Collections.unmodifiableList(fields);
	}

	public List<Field> getFields() {
		return fields.stream().sorted(Comparator.comparing(Field::getOrder)).toList();
	}

	public static class Field implements Serializable {
		private static final long serialVersionUID = 1L;

		private final String code;
		private final String fieldTypeName;
		private String label;
		private String tooltip;
		private Integer order;
		private boolean isDefault;
		private boolean isMandatory;
		private EasyFormsData parameters;
		private List<FieldValidator> validators;

		public Field(final String code, final String fieldTypeName) {
			this.code = code;
			this.fieldTypeName = fieldTypeName;
			label = code;
		}

		public Field(final String code, final EasyFormsFieldType fieldType) {
			this(code, fieldType.getName());
		}

		public Field(final String code, final EnumDefinition<EasyFormsFieldType, ?> fieldTypeEnun) {
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

		public Field withLabel(final String label) {
			this.label = label;
			return this;
		}

		public String getTooltip() {
			return tooltip;
		}

		public Field withTooltip(final String tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public Integer getOrder() {
			return order;
		}

		public Field withOrder(final Integer order) {
			this.order = order;
			return this;
		}

		public boolean isDefault() {
			return isDefault;
		}

		public Field withDefault() {
			return withDefault(true);
		}

		public Field withDefault(final boolean isDefault) {
			this.isDefault = true;
			return this;
		}

		public boolean isMandatory() {
			return isMandatory;
		}

		public Field withMandatory() {
			return withMandatory(true);
		}

		public Field withMandatory(final boolean isMandatory) {
			this.isMandatory = isMandatory;
			return this;
		}

		public EasyFormsData getParameters() {
			return parameters;
		}

		public Field withParameters(final Map<String, Object> parameters) {
			return withParameters(new EasyFormsData(parameters));
		}

		public Field withParameters(final EasyFormsData parameters) {
			this.parameters = parameters;
			return this;
		}

		public List<FieldValidator> getValidators() {
			return validators;
		}

		public Field withValidators(final List<FieldValidator> validators) {
			this.validators = validators;
			return this;
		}

	}

	public static class FieldValidator implements Serializable {
		private static final long serialVersionUID = 1L;

		private String name;
		private Map<String, Serializable> parameters;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public Map<String, Serializable> getParameters() {
			return parameters;
		}

		public void setParameters(final Map<String, Serializable> parameters) {
			this.parameters = parameters;
		}

	}

}
