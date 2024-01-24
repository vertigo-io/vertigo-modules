package io.vertigo.easyforms.easyformsrunner.model;

import com.google.gson.Gson;

import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;

public class EasyFormsTemplateAdapter implements BasicTypeAdapter<EasyFormsTemplate, String> {

	private static final Gson GSON = new Gson();

	@Override
	public EasyFormsTemplate toJava(final String modelAsString, final Class<EasyFormsTemplate> type) {
		// H2 wraps json in a String by default (http://www.h2database.com/html/datatypes.html#json_type)
		// temporary fix to unescape json, it should have been inserted as json value not json string
		if (modelAsString.startsWith("\"")) {
			final var resolvedJson = modelAsString
					.substring(1, modelAsString.length() - 1)
					.replace("\\\"", "\"")
					.replace("\\\\", "\\");
			return GSON.fromJson(resolvedJson, EasyFormsTemplate.class);
		}
		return GSON.fromJson(modelAsString, EasyFormsTemplate.class);
	}

	@Override
	public String toBasic(final EasyFormsTemplate modele) {
		return GSON.toJson(modele);
	}

	@Override
	public BasicType getBasicType() {
		return BasicType.String;
	}

}
