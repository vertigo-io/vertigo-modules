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
package io.vertigo.easyforms.domain;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.stereotype.Field;
import io.vertigo.datamodel.data.util.DataModelUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class EasyFormsFieldValidatorTypeUi implements DataObject {
	private static final long serialVersionUID = 1L;

	private String name;
	private String label;
	private String description;
	private io.vertigo.easyforms.runner.model.template.EasyFormsTemplate paramTemplate;
	private java.util.List<String> fieldTypes = new java.util.ArrayList<>();
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Validator type name'.
	 * @return String name
	 */
	@Field(smartType = "STyEfLabel", label = "Validator type name")
	@io.vertigo.datamodel.data.stereotype.SortField
	public String getName() {
		return name;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Validator type name'.
	 * @param name String
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Label'.
	 * @return String label
	 */
	@Field(smartType = "STyEfLabel", label = "Label")
	@io.vertigo.datamodel.data.stereotype.DisplayField
	public String getLabel() {
		return label;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Label'.
	 * @param label String
	 */
	public void setLabel(final String label) {
		this.label = label;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Description'.
	 * @return String description
	 */
	@Field(smartType = "STyEfText", label = "Description")
	public String getDescription() {
		return description;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Description'.
	 * @param description String
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'UI configuration template'.
	 * @return EasyFormsTemplate paramTemplate
	 */
	@Field(smartType = "STyEfFormTemplate", label = "UI configuration template")
	public io.vertigo.easyforms.runner.model.template.EasyFormsTemplate getParamTemplate() {
		return paramTemplate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'UI configuration template'.
	 * @param paramTemplate EasyFormsTemplate
	 */
	public void setParamTemplate(final io.vertigo.easyforms.runner.model.template.EasyFormsTemplate paramTemplate) {
		this.paramTemplate = paramTemplate;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Attached to fields'.
	 * @return List de String fieldTypes
	 */
	@Field(smartType = "STyEfText", cardinality = io.vertigo.core.lang.Cardinality.MANY, label = "Attached to fields")
	public java.util.List<String> getFieldTypes() {
		return fieldTypes;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Attached to fields'.
	 * @param fieldTypes List de String
	 */
	public void setFieldTypes(final java.util.List<String> fieldTypes) {
		io.vertigo.core.lang.Assertion.check().isNotNull(fieldTypes);
		//---
		this.fieldTypes = fieldTypes;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
