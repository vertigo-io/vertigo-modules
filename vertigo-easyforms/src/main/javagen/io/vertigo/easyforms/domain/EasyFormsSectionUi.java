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
public final class EasyFormsSectionUi implements DataObject {
	private static final long serialVersionUID = 1L;

	private String code;
	private String condition;
	private Boolean haveSystemField;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Section code'.
	 * @return String code <b>Obligatoire</b>
	 */
	@Field(smartType = "STyEfCode", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Section code")
	public String getCode() {
		return code;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Section code'.
	 * @param code String <b>Obligatoire</b>
	 */
	public void setCode(final String code) {
		this.code = code;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Display condition'.
	 * @return String condition
	 */
	@Field(smartType = "STyEfLongLabel", label = "Display condition")
	public String getCondition() {
		return condition;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Display condition'.
	 * @param condition String
	 */
	public void setCondition(final String condition) {
		this.condition = condition;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Have system field'.
	 * @return Boolean haveSystemField
	 */
	@Field(smartType = "STyEfBoolean", label = "Have system field")
	public Boolean getHaveSystemField() {
		return haveSystemField;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Have system field'.
	 * @param haveSystemField Boolean
	 */
	public void setHaveSystemField(final Boolean haveSystemField) {
		this.haveSystemField = haveSystemField;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
