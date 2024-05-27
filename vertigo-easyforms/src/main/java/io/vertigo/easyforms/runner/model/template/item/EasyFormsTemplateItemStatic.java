package io.vertigo.easyforms.runner.model.template.item;

import java.util.Map;

import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;

public final class EasyFormsTemplateItemStatic extends AbstractEasyFormsTemplateItem {
	private static final long serialVersionUID = 1L;

	private Map<String, String> text;

	public EasyFormsTemplateItemStatic() {
		// empty constructor needed
	}

	public EasyFormsTemplateItemStatic(final Map<String, String> text) {
		this.text = text;
	}

	public Map<String, String> getText() {
		return text;
	}

	public String getUserText(final String lang) {
		var value = text.get(lang);
		if (value == null) {
			value = text.get("fr");
		}
		if (value == null) {
			value = text.get("i18n");
		}
		return value;
	}

	public void setText(final Map<String, String> text) {
		this.text = text;
	}

}
