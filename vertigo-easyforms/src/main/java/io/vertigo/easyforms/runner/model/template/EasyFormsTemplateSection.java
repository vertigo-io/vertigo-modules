package io.vertigo.easyforms.runner.model.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EasyFormsTemplateSection implements Serializable {

	private String code;
	private Map<String, String> label;
	private String condition;

	private final List<AbstractEasyFormsTemplateItem> items;

	private static final long serialVersionUID = 1L;

	public EasyFormsTemplateSection() {
		items = new ArrayList<>();
	}

	public EasyFormsTemplateSection(final String code, final Map<String, String> label, final String condition, final List<AbstractEasyFormsTemplateItem> items) {
		this.code = code;
		this.label = label;
		this.condition = condition;
		this.items = items;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Map<String, String> getLabel() {
		return label;
	}

	public void setLabel(final Map<String, String> label) {
		this.label = label;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(final String condition) {
		this.condition = condition;
	}

	public List<AbstractEasyFormsTemplateItem> getItems() {
		return items;
	}

}
