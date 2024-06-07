/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.easyforms.impl.runner.pack.formatter;

import java.util.LinkedHashSet;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import io.vertigo.core.lang.BasicType;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.definitions.Formatter;
import io.vertigo.datamodel.smarttype.definitions.FormatterException;

/**
 * Formatter for extension list.
 */
public final class FormatterExtensions implements Formatter {

	private final static Predicate<String> PATTERN_MATCHER = Pattern.compile("^\\.?[a-zA-Z0-9]+$").asMatchPredicate();

	/**
	 * Constructor.
	 */
	public FormatterExtensions(final String args) {
	}

	@Override
	public String valueToString(final Object objValue, final BasicType dataType) {
		return (String) objValue;
	}

	@Override
	public Object stringToValue(final String strValue, final BasicType dataType) throws FormatterException {
		if (StringUtil.isBlank(strValue)) {
			return null;
		}

		final var extSet = new LinkedHashSet<String>();
		for (final var arg : strValue.trim().split("\\s*,\\s*")) {
			if (!PATTERN_MATCHER.test(arg)) {
				throw new FormatterException(EfFormatterResources.EfFmtExt, arg);
			}
			if (arg.startsWith(".")) {
				extSet.add(arg.toLowerCase());
			} else {
				extSet.add("." + arg.toLowerCase());
			}
		}

		return String.join(", ", extSet);
	}

}
