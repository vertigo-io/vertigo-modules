/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	private Map<String, String> tooltip;
	private boolean isSystem; // system field, code not modifiable
	private boolean isMandatory;
	private Object defaultValue;
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

	public void setLabel(final Map<String, String> label) {
		this.label = label;
	}

	public Map<String, String> getTooltip() {
		return tooltip;
	}

	public void setTooltip(final Map<String, String> tooltip) {
		this.tooltip = tooltip;
	}

	public boolean isSystem() {
		return isSystem;
	}

	public void setSystem(final boolean isSystem) {
		this.isSystem = isSystem;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(final boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue;
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

	public EasyFormsTemplateItemField withTooltip(final Map<String, String> myTooltip) {
		tooltip = myTooltip;
		return this;
	}

	public EasyFormsTemplateItemField withSystem() {
		return withSystem(true);
	}

	public EasyFormsTemplateItemField withSystem(final boolean mySystem) {
		isSystem = mySystem;
		return this;
	}

	public EasyFormsTemplateItemField withMandatory() {
		return withMandatory(true);
	}

	public EasyFormsTemplateItemField withMandatory(final boolean myMandatory) {
		isMandatory = myMandatory;
		return this;
	}

	public EasyFormsTemplateItemField withDefaultValue(final Object myDefaultValue) {
		defaultValue = myDefaultValue;
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
