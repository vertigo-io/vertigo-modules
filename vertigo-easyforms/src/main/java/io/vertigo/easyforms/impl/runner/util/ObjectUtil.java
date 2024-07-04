package io.vertigo.easyforms.impl.runner.util;

import java.io.Serializable;
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
	public static Object resolveDefaultValue(final Object defaultValueIn, final Map<String, Serializable> contextData) {
		if (defaultValueIn instanceof final String defaultStr && defaultStr.startsWith("#ctx.") && defaultStr.endsWith("#")) {
			final String defaultValueCode = defaultStr.substring(5, defaultStr.length() - 1);
			return ObjectUtil.extractValueFromData(defaultValueCode, contextData);
		}
		return defaultValueIn;
	}
}
