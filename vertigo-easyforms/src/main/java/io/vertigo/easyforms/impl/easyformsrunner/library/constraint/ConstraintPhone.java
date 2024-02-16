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
package io.vertigo.easyforms.impl.easyformsrunner.library.constraint;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.vertigo.basics.constraint.ConstraintUtil;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.datamodel.smarttype.definitions.Property;

/**
 * Phone white lists.
 *
 * @author skerdudou
 */
public final class ConstraintPhone implements Constraint<String, String> {

	private final Pattern pattern;
	private final LocaleMessageText errorMessage;

	public static ConstraintPhone ofEnum(final ConstraintPhoneEnum phonePatterns, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		return new ConstraintPhone(List.of(phonePatterns.getPattern()), overrideMessageOpt, overrideResourceMessageOpt);
	}

	public static ConstraintPhone ofEnums(final List<ConstraintPhoneEnum> phonePatterns, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		final List<PhonePattern> list = phonePatterns.stream().map(ConstraintPhoneEnum::getPattern).toList();
		return new ConstraintPhone(list, overrideMessageOpt, overrideResourceMessageOpt);
	}

	public ConstraintPhone(final List<PhonePattern> phonePatterns, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		Assertion.check()
				.isNotNull(phonePatterns)
				.isFalse(phonePatterns.isEmpty(), "Patterns must be passed in parameter");
		//---
		final var patternString = phonePatterns.stream()
				.map(PhonePattern::getRegex)
				.collect(Collectors.joining("|", "^(", ")$"));

		pattern = Pattern.compile(patternString);
		errorMessage = ConstraintUtil.resolveMessage(overrideMessageOpt, overrideResourceMessageOpt,
				() -> LocaleMessageText.of("Téléphone invalide"));
	}

	/**
	 * @param patternsOrCodes List of codes from ConstraintPhoneEnum or pattern in xxx:yy format for a new PhonePattern(xxx, yy). All separated by ';'.
	 */
	public ConstraintPhone(final String patternsOrCodes, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		this(resolvePhonePattern(patternsOrCodes), overrideMessageOpt, overrideResourceMessageOpt);
	}

	private static List<PhonePattern> resolvePhonePattern(final String patternsOrCodes) {
		Assertion.check()
				.isNotBlank(patternsOrCodes);
		//---
		return Arrays.stream(patternsOrCodes.split(";"))
				.map(ConstraintPhone::doResolvePhonePattern)
				.toList();

	}

	private static PhonePattern doResolvePhonePattern(final String patternOrCode) {
		if (patternOrCode.contains(":")) {
			final var params = patternOrCode.split(":");
			return new PhonePattern(params[0], Integer.parseInt(params[1]));
		}
		return ConstraintPhoneEnum.valueOf(patternOrCode).getPattern();
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkConstraint(final String value) {
		return value == null
				|| pattern.matcher(value)
						.matches();
	}

	/** {@inheritDoc} */
	@Override
	public LocaleMessageText getErrorMessage() {
		return errorMessage;
	}

	/** {@inheritDoc} */
	@Override
	public Property getProperty() {
		return DtProperty.REGEX;
	}

	/** {@inheritDoc} */
	@Override
	public String getPropertyValue() {
		return pattern.pattern();
	}

	/**
	 * @return Expression régulière testée par la contrainte
	 */
	public String getRegex() {
		//regex ==>
		return pattern.pattern();
	}

	public static enum ConstraintPhoneEnum {
		FR_METRO(new PhonePattern(List.of("+33", "0"), 9)),
		FR_FIXE_METRO(new PhonePattern(List.of("+33 [1-5]", "0[1-5]"), 8)),
		FR_FIXE_METRO_SPECIAL(new PhonePattern(List.of("+33 [89]", "0[89]"), 8)),
		FR_MOBILE_METRO(new PhonePattern(List.of("+33 [67]", "0[67]"), 8)),

		FR_FIXE_GUADELOUPE(new PhonePattern("+590 5 9 0", 6)),
		FR_MOBILE_GUADELOUPE(new PhonePattern("+590 6 9 [01]", 6)),

		FR_FIXE_GUYANE(new PhonePattern("+594 5 9 4", 6)),
		FR_MOBILE_GUYANE(new PhonePattern("+594 6 9 4", 6)),

		FR_FIXE_MARTINIQUE(new PhonePattern("+596 5 9 6", 6)),
		FR_MOBILE_MARTINIQUE(new PhonePattern("+596 6 9 [67]", 6)),

		FR_FIXE_MAYOTTE(new PhonePattern("+262 2 6 9", 6)),
		FR_MOBILE_MAYOTTE(new PhonePattern("+262 6 3 9", 6)),

		FR_FIXE_REUNION(new PhonePattern("+262 2 6 [239]", 6)),
		FR_MOBILE_REUNION(new PhonePattern("+262 6 9 [23]", 6)),

		FR_FIXE_ST_PIER(new PhonePattern("+508 4 1", 4)),
		FR_MOBILE_ST_PIER(new PhonePattern("+508 5 5", 4)),

		FR_FIXE_POLYNESIE(new PhonePattern("+689 4 [09]", 6)),
		FR_MOBILE_POLYNESIE(new PhonePattern("+689 8 [7-9]", 6)),

		FR_FIXE_WALLIS(new PhonePattern("+681 7 2", 4)),
		FR_MOBILE_WALLIS(new PhonePattern("+681 [48]", 5)),

		FR_FIXE_CALEDONIE(new PhonePattern("+687 [234]", 5)),
		FR_MOBILE_CALEDONIE(new PhonePattern(List.of("+687 [79] [0-9]", "+687 8 [0-79]"), 4)),

		FR_FIXE_OM(new PhonePattern(FR_FIXE_GUADELOUPE, FR_FIXE_GUYANE, FR_FIXE_MARTINIQUE, FR_FIXE_MAYOTTE, FR_FIXE_REUNION, FR_FIXE_ST_PIER, FR_FIXE_POLYNESIE, FR_FIXE_WALLIS, FR_FIXE_CALEDONIE)),
		FR_MOBILE_OM(new PhonePattern(FR_MOBILE_GUADELOUPE, FR_MOBILE_GUYANE, FR_MOBILE_MARTINIQUE, FR_MOBILE_MAYOTTE, FR_MOBILE_REUNION, FR_MOBILE_ST_PIER, FR_MOBILE_POLYNESIE, FR_MOBILE_WALLIS,
				FR_MOBILE_CALEDONIE)),

		FR_FIXE(new PhonePattern(FR_FIXE_METRO, FR_FIXE_OM)),
		FR_MOBILE(new PhonePattern(FR_MOBILE_METRO, FR_MOBILE_OM)),
		;

		private final PhonePattern pattern;

		private ConstraintPhoneEnum(final PhonePattern pattern) {
			this.pattern = pattern;
		}

		public PhonePattern getPattern() {
			return pattern;
		}

	}

	private static class PhonePattern {

		private final String regex;

		public PhonePattern(final String prefix, final int digitsAfterPrefix) {
			this(List.of(prefix), digitsAfterPrefix);
		}

		/**
		 * + prefix also provides 00.
		 * space is an unlimited permissive separator.
		 */
		public PhonePattern(final List<String> prefixes, final int digitsAfterPrefix) {
			// use of \W to handle different types of separators (eg : '(', ')', '.', '-' and space)
			// must ends with 2 digits without separator
			regex = prefixes.stream()
					.map(p -> p.replace("+", "(\\+|00)").replace(" ", "\\W*"))
					.map(p -> "(" + p + "(\\W*\\d){" + (digitsAfterPrefix - 1) + "}\\d)")
					.collect(Collectors.joining("|"));
		}

		public PhonePattern(final ConstraintPhoneEnum... others) {
			regex = Arrays.stream(others)
					.map(c -> c.getPattern().getRegex())
					.collect(Collectors.joining("|"));
		}

		public String getRegex() {
			return regex;
		}

	}
}
