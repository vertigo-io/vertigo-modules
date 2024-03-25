package io.vertigo.easyforms.runner.model.template;

import java.io.Serializable;
import java.util.List;

public final class EasyFormsTemplateSection implements Serializable {

	private final String code;
	private final String label;
	private final String condition;

	private final List<AbstractEasyFormsTemplateItem> items;

	private static final long serialVersionUID = 1L;

	public EasyFormsTemplateSection(final String code, final String label, final String condition, final List<AbstractEasyFormsTemplateItem> items) {
		this.code = code;
		this.label = label;
		this.condition = condition;
		this.items = items;
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	public String getCondition() {
		return condition;
	}

	public List<AbstractEasyFormsTemplateItem> getItems() {
		return items;
	}

}
