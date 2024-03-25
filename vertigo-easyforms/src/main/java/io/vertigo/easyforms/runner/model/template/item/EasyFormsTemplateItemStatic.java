package io.vertigo.easyforms.runner.model.template.item;

import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;

public final class EasyFormsTemplateItemStatic extends AbstractEasyFormsTemplateItem {
	private static final long serialVersionUID = 1L;

	private String label;

	public EasyFormsTemplateItemStatic() {
		// empty constructor needed
	}

	public EasyFormsTemplateItemStatic(final String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

}
