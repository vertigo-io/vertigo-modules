package io.vertigo.easyforms.runner.model.template.item;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;

public final class EasyFormsTemplateItemBlock extends AbstractEasyFormsTemplateItem {
	private static final long serialVersionUID = 1L;

	private String condition;
	// ideas : handle repetition ? (code for nested data + cardinality)

	private List<AbstractEasyFormsTemplateItem> items;

	public EasyFormsTemplateItemBlock() {
		items = new ArrayList<>();
		// empty constructor needed
	}

	public EasyFormsTemplateItemBlock(final String condition, final List<AbstractEasyFormsTemplateItem> items) {
		this.condition = condition;
		this.items = items;
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

	public void setItems(final List<AbstractEasyFormsTemplateItem> items) {
		this.items = items;
	}

}
