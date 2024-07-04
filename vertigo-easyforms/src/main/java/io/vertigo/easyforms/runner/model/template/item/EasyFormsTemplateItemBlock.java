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

	public boolean haveSystemField() {
		for (final var item : items) {
			if (item instanceof final EasyFormsTemplateItemField field && field.isSystem()) {
				return true;
			}
		}
		return false;
	}
}
