package io.vertigo.easyforms.runner.model.adapter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;
import io.vertigo.core.lang.json.CoreJsonAdapters;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;

public final class EasyFormsJsonAdapter<C> implements BasicTypeAdapter<C, String> {

	static {
		final var gsonBuilder = new GsonBuilder()
				.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE) // later, integer formatter don't support conversion to double
				.registerTypeAdapter(AbstractEasyFormsTemplateItem.class, new AbstractEasyFormsTemplateItem.GsonDeserializer())
				.registerTypeAdapter(FileInfoURI.class, new FileInfoURIAdapter());
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
		final C result;
		if (formAsString.startsWith("\"")) {
			final var resolvedJson = formAsString
					.substring(1, formAsString.length() - 1)
					.replace("\\\"", "\"")
					.replace("\\\\", "\\")
					.replace("\\n", "\n");
			result = GSON.fromJson(resolvedJson, type);
		} else {
			result = GSON.fromJson(formAsString, type);
		}

		if (result instanceof final EasyFormsData data) {
			// fix FileInfoURI deserialization, Gson can't handle it because target is Object and we can't read in advance to infer target type
			postProcessFileInfoURI(data);
		}

		return result;
	}

	private void postProcessFileInfoURI(final Map<String, Object> data) {
		for (final var entry : data.entrySet()) {
			if (entry.getValue() instanceof final Map mapValue) {
				if (isFileInfoURI(mapValue)) {
					final var fileInfo = FileInfoURI.fromURN(String.valueOf(mapValue.get("urn")));
					entry.setValue(fileInfo);
				} else {
					postProcessFileInfoURI(mapValue);
				}
			} else if (entry.getValue() instanceof final List list) {
				final var it = list.listIterator();
				while (it.hasNext()) {
					final var item = it.next();
					if (item instanceof final Map mapItem) {
						if (isFileInfoURI(mapItem)) {
							final var fileInfo = FileInfoURI.fromURN(String.valueOf(mapItem.get("urn")));
							it.set(fileInfo);
						} else {
							postProcessFileInfoURI(mapItem);
						}
					}
				}
			}
		}
	}

	private boolean isFileInfoURI(final Map<String, Object> entry) {
		return "FILE".equals(entry.get("type")) && entry.containsKey("urn");
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

	private static class FileInfoURIAdapter extends TypeAdapter<FileInfoURI> {

		@Override
		public void write(final JsonWriter out, final FileInfoURI value) throws IOException {
			out.beginObject()
					.name("type")
					.value("FILE")
					.name("urn")
					.value(value.toURN())
					.endObject();
		}

		@Override
		public FileInfoURI read(final JsonReader in) throws IOException {
			throw new UnsupportedOperationException("Not implemented");
		}

	}

}
