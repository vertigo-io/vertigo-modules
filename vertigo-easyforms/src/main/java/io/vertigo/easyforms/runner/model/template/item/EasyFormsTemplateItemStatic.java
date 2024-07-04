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
