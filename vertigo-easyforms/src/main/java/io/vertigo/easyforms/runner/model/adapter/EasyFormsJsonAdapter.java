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
package io.vertigo.easyforms.runner.model.adapter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import io.vertigo.core.lang.json.UTCDateUtil;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;

public final class EasyFormsJsonAdapter<C> implements BasicTypeAdapter<C, String> {

	static {
		GSON = CoreJsonAdapters.addCoreGsonConfig(new GsonBuilder(), false)
				.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE) // later, integer formatter don't support conversion to double
				.registerTypeAdapter(AbstractEasyFormsTemplateItem.class, new AbstractEasyFormsTemplateItem.GsonDeserializer())
				.registerTypeAdapter(FileInfoURI.class, new FileInfoURIAdapter())
				.registerTypeAdapter(Date.class, new UTCDateAdapter())
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
				.registerTypeAdapter(Instant.class, new InstantAdapter())

				.create();
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
			// fix FileInfoURI and Date deserialization, Gson can't handle it because target is Object. We have no access to the EasyForm template and we can't read in advance to infer target type.
			postProcessObjects(data);
		}

		return result;
	}

	private void postProcessObjects(final Map<String, Object> data) {
		for (final var entry : data.entrySet()) {
			if (entry.getValue() instanceof final Map mapValue) {
				if (isObject(mapValue)) {
					entry.setValue(resolveObject(mapValue));
				} else {
					postProcessObjects(mapValue);
				}
			} else if (entry.getValue() instanceof final List list) {
				final var it = list.listIterator();
				while (it.hasNext()) {
					final var item = it.next();
					if (item instanceof final Map mapItem) {
						if (isObject(mapItem)) {
							it.set(resolveObject(mapItem));
						} else {
							postProcessObjects(mapItem);
						}
					}
				}
			}
		}
	}

	private boolean isObject(final Map<String, Object> entry) {
		return ("FILE".equals(entry.get("type")) && entry.containsKey("urn")) // legacy, deprecated
				|| entry.containsKey("_type");
	}

	private Object resolveObject(final Map<String, Object> entry) {
		final String key = (String) entry.getOrDefault("_type", entry.get("type"));
		switch (key) {
			case "FILE":
				return FileInfoURI.fromURN(String.valueOf(entry.get("urn")));
			case "DATE":
				return UTCDateUtil.parse((String) entry.get("value"));
			case "LOCAL_DATE":
				return LocalDate.parse((String) entry.get("value"), DateTimeFormatter.ISO_LOCAL_DATE);
			case "ZONED_DATE_TIME":
				return ZonedDateTime.parse((String) entry.get("value"), DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")));
			case "INSTANT":
				return UTCDateUtil.parseInstant((String) entry.get("value"));
			default:
				throw new IllegalArgumentException("Unsupported type " + key);
		}
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
					.name("_type")
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

	private static class UTCDateAdapter extends TypeAdapter<Date> {

		@Override
		public void write(final JsonWriter out, final Date date) throws IOException {
			out.beginObject()
					.name("_type")
					.value("DATE")
					.name("value")
					.value(UTCDateUtil.formatISO8601(date))
					.endObject();
		}

		@Override
		public Date read(final JsonReader in) throws IOException {
			throw new UnsupportedOperationException("Not implemented");
		}
	}

	private static class LocalDateAdapter extends TypeAdapter<LocalDate> {

		@Override
		public void write(final JsonWriter out, final LocalDate date) throws IOException {
			out.beginObject()
					.name("_type")
					.value("LOCAL_DATE")
					.name("value")
					.value(date.format(DateTimeFormatter.ISO_LOCAL_DATE)) // "yyyy-mm-dd"
					.endObject();
		}

		@Override
		public LocalDate read(final JsonReader in) throws IOException {
			throw new UnsupportedOperationException("Not implemented");
		}
	}

	private static class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {

		@Override
		public void write(final JsonWriter out, final ZonedDateTime date) throws IOException {
			out.beginObject()
					.name("_type")
					.value("ZONED_DATE_TIME")
					.name("value")
					.value(date.format(DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")))) // "yyyy-mm-ddTHH:MI:SSZ"
					.endObject();
		}

		@Override
		public ZonedDateTime read(final JsonReader in) throws IOException {
			throw new UnsupportedOperationException("Not implemented");
		}
	}

	private static class InstantAdapter extends TypeAdapter<Instant> {

		@Override
		public void write(final JsonWriter out, final Instant date) throws IOException {
			out.beginObject()
					.name("_type")
					.value("INSTANT")
					.name("value")
					.value(UTCDateUtil.formatInstantISO8601(date)) // "yyyy-mm-ddTHH:MI:SSZ"
					.endObject();
		}

		@Override
		public Instant read(final JsonReader in) throws IOException {
			throw new UnsupportedOperationException("Not implemented");
		}
	}

}
