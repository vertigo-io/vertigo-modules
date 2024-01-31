package io.vertigo.easyforms.easyformsrunner.model;

import com.google.gson.Gson;

import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;

public class EasyFormsParameterDataAdapter implements BasicTypeAdapter<EasyFormsParameterData, String> {

	private static final Gson GSON = new Gson();

	@Override
	public EasyFormsParameterData toJava(final String formAsString, final Class<EasyFormsParameterData> type) {
		if (formAsString == null) {
			return null;
		}

		// H2 wraps json in a String by default (http://www.h2database.com/html/datatypes.html#json_type)
		// temporary fix to unescape json, it should have been inserted as json value not json string
		if (formAsString.startsWith("\"")) {
			final var resolvedJson = formAsString
					.substring(1, formAsString.length() - 1)
					.replace("\\\"", "\"")
					.replace("\\\\", "\\");
			return GSON.fromJson(resolvedJson, EasyFormsParameterData.class);
		}
		return GSON.fromJson(formAsString, EasyFormsParameterData.class);

	}

	@Override
	public String toBasic(final EasyFormsParameterData form) {
		if (form == null) {
			return null;
		}
		return GSON.toJson(form);
	}

	@Override
	public BasicType getBasicType() {
		return BasicType.String;
	}

}
