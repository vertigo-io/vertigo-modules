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
public final class EasyFormsLabelUi implements DataObject {
	private static final long serialVersionUID = 1L;

	private String lang;
	private String label;
	private String tooltip;
	private String text;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Language'.
	 * @return String lang
	 */
	@Field(smartType = "STyEfLabel", label = "Language")
	public String getLang() {
		return lang;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Language'.
	 * @param lang String
	 */
	public void setLang(final String lang) {
		this.lang = lang;
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
	 * Récupère la valeur de la propriété 'Tooltip'.
	 * @return String tooltip
	 */
	@Field(smartType = "STyEfLabel", label = "Tooltip")
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Tooltip'.
	 * @param tooltip String
	 */
	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Text'.
	 * @return String text
	 */
	@Field(smartType = "STyEfTextHtml", label = "Text")
	public String getText() {
		return text;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Text'.
	 * @param text String
	 */
	public void setText(final String text) {
		this.text = text;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
