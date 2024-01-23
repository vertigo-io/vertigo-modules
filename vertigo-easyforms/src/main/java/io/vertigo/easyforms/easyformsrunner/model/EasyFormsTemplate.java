package io.vertigo.easyforms.easyformsrunner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

		private String fieldCode;
		private String fieldType;
		private String label;
		private String tooltip;
		private Integer order;
		private boolean isDefault;
		private boolean isMandatory;
		private List<String> fieldValidators;

		public String getFieldCode() {
			return fieldCode;
		}

		public void setFieldCode(final String fieldCode) {
			this.fieldCode = fieldCode;
		}

		public String getFieldType() {
			return fieldType;
		}

		public void setFieldType(final String fieldType) {
			this.fieldType = fieldType;
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

		public List<String> getFieldValidators() {
			return fieldValidators;
		}

		public void setFieldValidators(final List<String> fieldValidators) {
			this.fieldValidators = fieldValidators;
		}

	}

}
