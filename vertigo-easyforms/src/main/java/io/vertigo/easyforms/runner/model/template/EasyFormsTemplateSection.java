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
package io.vertigo.easyforms.runner.model.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemBlock;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.rule.EasyFormsRuleParser;

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

	public List<EasyFormsTemplateItemField> getAllFields() {
		final List<EasyFormsTemplateItemField> list = new ArrayList<>();
		for (final var item : getItems()) {
			addFieldsForItem(list, item);
		}
		return list;
	}

	private static void addFieldsForItem(final List<EasyFormsTemplateItemField> list, final AbstractEasyFormsTemplateItem item) {
		if (item instanceof final EasyFormsTemplateItemField field) {
			list.add(field);
		} else if (item instanceof final EasyFormsTemplateItemBlock block) {
			for (final var blockElem : block.getItems()) {
				addFieldsForItem(list, blockElem);
			}
		}
	}

	public List<EasyFormsTemplateItemField> getAllDisplayedFields(final EasyFormsData data) {
		final List<EasyFormsTemplateItemField> list = new ArrayList<>();
		for (final var item : getItems()) {
			addDisplayedFieldsForItem(list, item, data);
		}
		return list;
	}

	private static void addDisplayedFieldsForItem(final List<EasyFormsTemplateItemField> list, final AbstractEasyFormsTemplateItem item, final EasyFormsData data) {
		if (item instanceof final EasyFormsTemplateItemField field) {
			list.add(field);
		} else if (item instanceof final EasyFormsTemplateItemBlock block) {
			if (!StringUtil.isBlank(block.getCondition())) {
				final var result = EasyFormsRuleParser.parse(block.getCondition(), data);
				if (!result.isValid() || !result.getResult()) {
					return;
				}
			}
			for (final var blockElem : block.getItems()) {
				addDisplayedFieldsForItem(list, blockElem, data);
			}
		}
	}

	public boolean haveSystemField() {
		return getAllFields().stream()
				.anyMatch(EasyFormsTemplateItemField::isSystem);
	}

}
