package io.vertigo.easyforms.runner.model.data;

import java.util.HashMap;
import java.util.Map;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.util.StringUtil;

public class EasyFormsContext extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public Object put(final String prefix, final String key, final Object value) {
		if (StringUtil.isBlank(prefix)) {
			return put(key, value);
		}
		return put(prefix + "." + key, value);
	}

	public void putAll(final String prefix, final Map<String, Object> values) {
		Assertion.check().isNotNull(values);

		values.forEach((k, v) -> {
			put(prefix, k, v);
		});
	}

	public boolean containsKey(final String prefix, final String key) {
		if (StringUtil.isBlank(prefix)) {
			return containsKey(key);
		}
		return containsKey(prefix + "." + key);
	}
}
