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
public final class EasyFormsFieldTypeUi implements DataObject {
	private static final long serialVersionUID = 1L;

	private String name;
	private String category;
	private String label;
	private String uiComponentName;
	private io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData uiParameters;
	private io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate paramTemplate;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Field type'.
	 * @return String name
	 */
	@Field(smartType = "STyEfLabel", label = "Field type")
	@io.vertigo.datamodel.data.stereotype.SortField
	public String getName() {
		return name;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Field type'.
	 * @param name String
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Field type category'.
	 * @return String category
	 */
	@Field(smartType = "STyEfLabel", label = "Field type category")
	public String getCategory() {
		return category;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Field type category'.
	 * @param category String
	 */
	public void setCategory(final String category) {
		this.category = category;
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
	 * Récupère la valeur de la propriété 'UI component name'.
	 * @return String uiComponentName
	 */
	@Field(smartType = "STyEfLabel", label = "UI component name")
	public String getUiComponentName() {
		return uiComponentName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'UI component name'.
	 * @param uiComponentName String
	 */
	public void setUiComponentName(final String uiComponentName) {
		this.uiComponentName = uiComponentName;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'UI parameters'.
	 * @return EasyFormsData uiParameters
	 */
	@Field(smartType = "STyEfFormData", label = "UI parameters")
	public io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData getUiParameters() {
		return uiParameters;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'UI parameters'.
	 * @param uiParameters EasyFormsData
	 */
	public void setUiParameters(final io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData uiParameters) {
		this.uiParameters = uiParameters;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'UI configuration template'.
	 * @return EasyFormsTemplate paramTemplate
	 */
	@Field(smartType = "STyEfFormTemplate", label = "UI configuration template")
	public io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate getParamTemplate() {
		return paramTemplate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'UI configuration template'.
	 * @param paramTemplate EasyFormsTemplate
	 */
	public void setParamTemplate(final io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate paramTemplate) {
		this.paramTemplate = paramTemplate;
	}
	
	/**
	 * Champ : COMPUTED.
	 * Récupère la valeur de la propriété calculée 'Have configuration'.
	 * @return Boolean hasTemplate
	 */
	@Field(smartType = "STyEfBooleen", type = "COMPUTED", persistent = false, label = "Have configuration")
	public Boolean getHasTemplate() {
		return getParamTemplate() != null && !getParamTemplate().getFields().isEmpty();
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
