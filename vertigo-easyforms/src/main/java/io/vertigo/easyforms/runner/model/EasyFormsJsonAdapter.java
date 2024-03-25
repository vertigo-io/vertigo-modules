package io.vertigo.easyforms.runner.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;
import io.vertigo.core.lang.json.CoreJsonAdapters;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;

public final class EasyFormsJsonAdapter<C> implements BasicTypeAdapter<C, String> {

	static {
		final var gsonBuilder = new GsonBuilder()
				.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE) // later, integer formatter don't support conversion to double
				.registerTypeAdapter(AbstractEasyFormsTemplateItem.class, new AbstractEasyFormsTemplateItem.GsonDeserializer());
		CoreJsonAdapters.addCoreGsonConfig(gsonBuilder, false);

		GSON = gsonBuilder.create();
	}
	private static final Gson GSON;

	@Override
	public C toJava(final String formAsString, final Class<C> type) {
		if (formAsString == null) {
			return null;
		}

		// H2 wraps json in a String by default (http://www.h2database.com/html/datatypes.html#json_type)
		// temporary fix to unescape json, it should have been inserted as json value not json string
		if (formAsString.startsWith("\"")) {
			final var resolvedJson = formAsString
					.substring(1, formAsString.length() - 1)
					.replace("\\\"", "\"")
					.replace("\\\\", "\\")
					.replace("\\n", "\n");
			return GSON.fromJson(resolvedJson, type);
		}
		return GSON.fromJson(formAsString, type);

	}

	@Override
	public String toBasic(final C form) {
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
