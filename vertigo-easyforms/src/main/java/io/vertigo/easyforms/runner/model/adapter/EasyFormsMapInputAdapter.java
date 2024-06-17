package io.vertigo.easyforms.runner.model.adapter;

import java.util.List;
import java.util.Map;

import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;
import io.vertigo.core.util.StringUtil;

public final class EasyFormsMapInputAdapter implements BasicTypeAdapter<List<Map<String, Object>>, List<Map<String, Object>>> {

	@Override
	public List<Map<String, Object>> toJava(final List<Map<String, Object>> uiData, final Class<List<Map<String, Object>>> type) {
		if (uiData == null) {
			return null;
		}

		return formatInternalMap(uiData);
	}

	@Override
	public List<Map<String, Object>> toBasic(final List<Map<String, Object>> jsonData) {
		return jsonData; // already formatted
	}

	@Override
	public BasicType getBasicType() {
		return BasicType.String;
	}

	private static List<Map<String, Object>> formatInternalMap(final List<Map<String, Object>> data) {
		// clear empty values
		data.removeIf(item -> isEmptyCustomValue(item));
		// trim all values
		data.forEach(value -> {
			value.put("value", ((String) value.get("value")).trim());
			final var labels = (Map<String, String>) value.get("label");
			for (final var labelEntry : labels.entrySet()) {
				labelEntry.setValue(labelEntry.getValue().trim());
			}
		});
		return data;
	}

	private static boolean isEmptyCustomValue(final Map<String, Object> entry) {
		if (!StringUtil.isBlank((String) entry.get("value"))) {
			return false;
		}

		final var labels = (Map<String, String>) entry.get("label");
		for (final String label : labels.values()) {
			if (!StringUtil.isBlank(label)) {
				return false;
			}
		}

		return true;
	}

}
