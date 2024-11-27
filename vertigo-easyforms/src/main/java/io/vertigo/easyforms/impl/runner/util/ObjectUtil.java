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
package io.vertigo.easyforms.impl.runner.util;

import java.util.Map;

import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.util.BeanUtil;

public class ObjectUtil {

	private ObjectUtil() {
		//private constructor
	}

	public static Object extractValueFromData(final String key, final Object data) {
		return extractValueFromData(key, "", key, data);
	}

	private static Object extractValueFromData(final String initialKey, final String parsedKey, final String key, final Object data) {
		final String[] keys = key.split("\\.", 2);
		final String prefix = keys[0];
		final var currentGlobalKey = (parsedKey == "" ? "" : parsedKey + ".") + prefix;
		if (!containsKey(data, prefix)) {
			return null;
		}

		if (keys.length == 1) {
			return getValue(data, prefix);
		} else {
			final String restOfKey = keys[1];
			return extractValueFromData(initialKey, currentGlobalKey, restOfKey, getValue(data, prefix));
		}
	}

	private static boolean containsKey(final Object o, final String key) {
		if (o instanceof final Map map) {
			return map.containsKey(key);
		}
		try {
			BeanUtil.getPropertyDescriptor(key, o.getClass());
			return true;
		} catch (final VSystemException e) {
			return false;
		}
	}

	private static Object getValue(final Object o, final String key) {
		if (o instanceof final Map map) {
			return map.get(key);
		}
		return BeanUtil.getValue(o, key);
	}

	/**
	 * Resolve a default value for a given expression.
	 *
	 * @param defaultValueIn the default value
	 * @param contextData the context data
	 * @return the resolved default value
	 */
	public static Object resolveDefaultValue(final Object defaultValueIn, final Map<String, ?> contextData) {
		if (defaultValueIn instanceof final String defaultStr && defaultStr.startsWith("#ctx.") && defaultStr.endsWith("#")) {
			final String defaultValueCode = defaultStr.substring(5, defaultStr.length() - 1);
			return ObjectUtil.extractValueFromData(defaultValueCode, contextData);
		}
		return defaultValueIn;
	}
}
