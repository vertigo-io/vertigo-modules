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
public final class EasyFormsTemplateFieldValidatorUi implements DataObject {
	private static final long serialVersionUID = 1L;

	private String validatorTypeName;
	private String label;
	private String parameterizedLabel;
	private String description;
	private io.vertigo.easyforms.runner.model.template.EasyFormsData parameters;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Validator type name'.
	 * @return String validatorTypeName
	 */
	@Field(smartType = "STyEfLabel", label = "Validator type name")
	public String getValidatorTypeName() {
		return validatorTypeName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Validator type name'.
	 * @param validatorTypeName String
	 */
	public void setValidatorTypeName(final String validatorTypeName) {
		this.validatorTypeName = validatorTypeName;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Label'.
	 * @return String label
	 */
	@Field(smartType = "STyEfLabel", label = "Label")
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
	 * Récupère la valeur de la propriété 'Label'.
	 * @return String parameterizedLabel
	 */
	@Field(smartType = "STyEfLabel", label = "Label")
	public String getParameterizedLabel() {
		return parameterizedLabel;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Label'.
	 * @param parameterizedLabel String
	 */
	public void setParameterizedLabel(final String parameterizedLabel) {
		this.parameterizedLabel = parameterizedLabel;
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
	 * Récupère la valeur de la propriété 'Parameters JSON'.
	 * @return EasyFormsData parameters
	 */
	@Field(smartType = "STyEfFormData", label = "Parameters JSON")
	public io.vertigo.easyforms.runner.model.template.EasyFormsData getParameters() {
		return parameters;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Parameters JSON'.
	 * @param parameters EasyFormsData
	 */
	public void setParameters(final io.vertigo.easyforms.runner.model.template.EasyFormsData parameters) {
		this.parameters = parameters;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
