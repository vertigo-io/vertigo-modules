package io.vertigo.easyforms.easyformsrunner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EasyFormsTemplate implements Serializable {

	private List<Field> fields = new ArrayList<>();

	private static final long serialVersionUID = 1L;

	public EasyFormsTemplate(final List<Field> fields) {
		this.fields = Collections.unmodifiableList(fields);
	}

	public List<Field> getFields() {
		return fields.stream().sorted(Comparator.comparing(Field::getOrder)).toList();
	}

	public static class Field implements Serializable {
		private static final long serialVersionUID = 1L;

		private String code;
		private String fieldTypeName;
		private String label;
		private String tooltip;
		private Integer order;
		private boolean isDefault;
		private boolean isMandatory;
		private Map<String, Serializable> parameters;
		private List<FieldValidator> validators;

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

		public String getLabel() {
			return label;
		}

		public void setLabel(final String label) {
			this.label = label;
		}

		public String getTooltip() {
			return tooltip;
		}

		public void setTooltip(final String tooltip) {
			this.tooltip = tooltip;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(final Integer order) {
			this.order = order;
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

		public Map<String, Serializable> getParameters() {
			return parameters;
		}

		public void setParameters(final Map<String, Serializable> parameters) {
			this.parameters = parameters;
		}

		public List<FieldValidator> getValidators() {
			return validators;
		}

		public void setValidators(final List<FieldValidator> validators) {
			this.validators = validators;
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
