/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2026, Vertigo.io, team@vertigo.io
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
package io.vertigo.easyforms.designer;

import java.util.regex.Pattern;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import io.vertigo.core.util.StringUtil;

public class HtmlSanatizerUtil {

	private static final Pattern TAGS_AND_NBSP_PATTERN = Pattern.compile("(<[^>]*>|&nbsp;|\u00A0)");

	private HtmlSanatizerUtil() {
		// Utility class
	}

	private static final PolicyFactory SANATIZER = Sanitizers.FORMATTING
			.and(Sanitizers.BLOCKS)
			.and(Sanitizers.LINKS)
			.and(Sanitizers.STYLES)
			.and(new HtmlPolicyBuilder()
					.allowElements( // force target _blank https://github.com/OWASP/java-html-sanitizer/issues/147
							(elementName, attrs) -> {
								final int targetIndex = attrs.indexOf("target");
								if (targetIndex < 0) {
									attrs.add("target");
									attrs.add("_blank");
								} else {
									attrs.set(targetIndex + 1, "_blank");
								}
								return elementName;
							},
							"a")
					.allowAttributes("class").globally()
					.toFactory());

	public static String sanatizeHtml(final String in) {
		final var sanitizedValue = SANATIZER.sanitize(in);
		if (isHtmlEmpty(sanitizedValue)) {
			return null;
		}
		return sanitizedValue;
	}

	public static boolean isHtmlEmpty(final String in) {
		// Detects if the string contains only tags, spaces, or non-breaking spaces
		return in == null || StringUtil.isBlank(in) || StringUtil.isBlank(TAGS_AND_NBSP_PATTERN.matcher(in).replaceAll(""));
	}
}
