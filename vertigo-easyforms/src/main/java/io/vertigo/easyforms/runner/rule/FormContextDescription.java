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
package io.vertigo.easyforms.runner.rule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.vertigo.core.util.ClassUtil;

public class FormContextDescription implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Map<String, Class<?>> contextMap = new HashMap<>();

	public FormContextDescription add(final String name, final Class<?> targetClass) {
		contextMap.put(name, targetClass);
		return this;
	}

	public boolean contains(final String name) {
		return contextMap.containsKey(name);
	}

	public Object getDummyValue(final String name) {
		final var targetClass = contextMap.get(name);

		if (targetClass == Integer.class) {
			return Integer.valueOf(0);
		} else if (targetClass == Double.class) {
			return Double.valueOf(0.0);
		} else if (targetClass == Boolean.class) {
			return Boolean.FALSE;
		} else if (targetClass == String.class) {
			return name;
		} else if (targetClass == LocalDate.class) {
			return LocalDate.now();
		} else if (targetClass == Instant.class) {
			return Instant.now();
		} else if (targetClass == BigDecimal.class) {
			return BigDecimal.ZERO;
		} else if (targetClass == Long.class) {
			return 0L;
		}
		return ClassUtil.newInstance(targetClass);
	}

	public String getLastCorrespondingKey(final String name) {
		if (!name.contains(".")) {
			return name;
		}
		final var parent = name.substring(0, name.lastIndexOf('.'));
		if (contextMap.keySet().stream().anyMatch(k -> k.startsWith(parent))) {
			return name;
		}
		return getLastCorrespondingKey(parent);
	}

	public Map<String, Class<?>> getContextMap() {
		return Collections.unmodifiableMap(contextMap);
	}

}
