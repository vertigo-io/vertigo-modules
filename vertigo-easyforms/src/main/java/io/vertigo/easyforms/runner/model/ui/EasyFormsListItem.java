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
package io.vertigo.easyforms.runner.model.ui;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;

public record EasyFormsListItem(
		Object value,
		String label) implements Serializable {

	private static final Gson gson = new GsonBuilder().serializeNulls().create();

	@Override
	public String toString() {
		final var labelDisplay = gson.toJson(getDisplayLabel());
		if (value == null) {
			return "{value:null, label:'" + labelDisplay + "'}";
		}
		return "{value:" + gson.toJson(value) + ", label:" + labelDisplay + "}";
	}

	public static List<EasyFormsListItem> ofCollection(final Object o) {
		if (o instanceof final Collection<?> list) {
			return list.stream()
					.map(EasyFormsListItem::of)
					.filter(Objects::nonNull)
					.toList();
		}
		throw new VSystemException("Argument is not a collection");
	}

	public static EasyFormsListItem of(final Object o) {
		if (o instanceof final EasyFormsListItem easyFormsListItem) {
			return easyFormsListItem;
		} else if (o instanceof final Map<?, ?> map) {
			if (map.isEmpty()) {
				return null;
			} else if (map.size() == 1) {
				final var entry = map.entrySet().iterator().next();
				return new EasyFormsListItem(entry.getKey(), entry.getValue().toString());
			} else if (map.size() == 2 && map.containsKey("label") && map.containsKey("value")) {
				final var easyFormsRunnerManager = Node.getNode().getComponentSpace().resolve(EasyFormsRunnerManager.class);
				final var labels = (Map<String, String>) map.get("label");
				return new EasyFormsListItem(map.get("value"), easyFormsRunnerManager.resolveTextForUserlang(labels));
			}
		}
		throw new VSystemException("Argument is not a valid representation of EasyFormsListItem.");
	}

	public String getDisplayLabel() {
		if (label != null && label.startsWith("#{")) {
			return LocaleMessageText.of(() -> label.substring(2, label.length() - 1)).getDisplay();
		}
		return label;
	}

}
