package io.vertigo.easyforms.easyformsrunner.model;

import com.google.gson.Gson;

import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;

public class EasyFormsDataAdapter implements BasicTypeAdapter<EasyFormsData, String> {

	private static final Gson GSON = new Gson();

	@Override
	public EasyFormsData toJava(final String formulaireAsString, final Class<EasyFormsData> type) {
		if (formulaireAsString == null) {
			return null;
		}

		// H2 wraps json in a String by default (http://www.h2database.com/html/datatypes.html#json_type)
		// temporary fix to unescape json, it should have been inserted as json value not json string
		if (formulaireAsString.startsWith("\"")) {
			final var resolvedJson = formulaireAsString
					.substring(1, formulaireAsString.length() - 1)
					.replace("\\\"", "\"")
					.replace("\\\\", "\\");
			return GSON.fromJson(resolvedJson, EasyFormsData.class);
		}
		return GSON.fromJson(formulaireAsString, EasyFormsData.class);

	}

	@Override
	public String toBasic(final EasyFormsData formulaire) {
		if (formulaire == null) {
			return null;
		}
		return GSON.toJson(formulaire);
	}

	@Override
	public BasicType getBasicType() {
		return BasicType.String;
	}

}
