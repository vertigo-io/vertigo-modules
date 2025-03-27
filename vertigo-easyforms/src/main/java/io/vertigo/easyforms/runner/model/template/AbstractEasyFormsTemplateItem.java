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
package io.vertigo.easyforms.runner.model.template;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import io.vertigo.core.lang.VSystemException;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemBlock;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemStatic;

public abstract class AbstractEasyFormsTemplateItem implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum ItemType {
		STATIC(EasyFormsTemplateItemStatic.class),
		FIELD(EasyFormsTemplateItemField.class),
		BLOCK(EasyFormsTemplateItemBlock.class),
		;

		private final Class<? extends AbstractEasyFormsTemplateItem> clazz;

		private ItemType(final Class<? extends AbstractEasyFormsTemplateItem> clazz) {
			this.clazz = clazz;
		}

		public Class<? extends AbstractEasyFormsTemplateItem> getTargetClass() {
			return clazz;
		}

		public static Class<? extends AbstractEasyFormsTemplateItem> getClassFromString(final String type) {
			return ItemType.valueOf(type).getTargetClass();
		}

		public static ItemType getTypeFromClass(final Class<? extends AbstractEasyFormsTemplateItem> clazz) {
			for (final var type : ItemType.values()) {
				if (type.getTargetClass().equals(clazz)) {
					return type;
				}
			}
			throw new VSystemException("Class " + clazz.getName() + " is not supported.");
		}
	}

	private final String type = ItemType.getTypeFromClass(getClass()).name(); // Usefull for JSON deserialization

	public static class GsonDeserializer implements JsonDeserializer<AbstractEasyFormsTemplateItem> {

		@Override
		public AbstractEasyFormsTemplateItem deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext deserializationContext) throws JsonParseException {

			final JsonObject jsonObject = jsonElement.getAsJsonObject();
			final JsonPrimitive prim = (JsonPrimitive) jsonObject.get("type");
			final String objectType = prim.getAsString();

			final Class<? extends AbstractEasyFormsTemplateItem> clazz = ItemType.getClassFromString(objectType);
			return deserializationContext.deserialize(jsonObject, clazz);
		}

	}

	public String getType() {
		return type;
	}

}
