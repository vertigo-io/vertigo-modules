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

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.commons.peg.PegParsingValueException;
import io.vertigo.commons.peg.rule.PegComparisonRuleSolver;
import io.vertigo.commons.peg.rule.PegDelayedOperationSolver;
import io.vertigo.commons.peg.rule.PegRule;
import io.vertigo.commons.peg.rule.PegRules;
import io.vertigo.commons.peg.term.PegBoolOperatorTerm;
import io.vertigo.easyforms.runner.util.ObjectUtil;

public class EasyFormsRuleParser {

	private EasyFormsRuleParser() {
		// only statics
	}

	private static final PegRule<PegDelayedOperationSolver<PegComparisonRuleSolver, PegBoolOperatorTerm, Boolean>> MAIN_RULE = PegRules.delayedOperation(PegRules.comparison(new ValueRule()),
			PegBoolOperatorTerm.class,
			true);

	private static final Function<String, Object> STATIC_RESOLVE_FUNCTION = s -> {
		if (s.startsWith("\"")) {
			return s.substring(1, s.length() - 1);
		} else if (s.contains(".")) {
			return new BigDecimal(s);
		} else if (s.equalsIgnoreCase("true")) {
			return true;
		} else if (s.equalsIgnoreCase("false")) {
			return false;
		} else if (s.equalsIgnoreCase("null")) {
			return null;
		}
		return Integer.parseInt(s);
	};

	public static ParseResult parse(final String s, final Map<String, Object> context) {
		try {
			return new ParseResult(MAIN_RULE.parse(s).getValue(), getResolveFunction(context));
		} catch (final PegNoMatchFoundException e) {
			return new ParseResult(e);
		}
	}

	public static ParseResult parseTest(final String s, final FormContextDescription context) {
		try {
			return new ParseResult(MAIN_RULE.parse(s).getValue(), getTestResolveFunction(context));
		} catch (final PegNoMatchFoundException e) {
			return new ParseResult(e);
		}
	}

	/**
	 * Get a function that resolves a string to an object, using the context.
	 *
	 * @param context Context to use
	 * @return The function
	 */
	private static Function<PegComparisonRuleSolver, Boolean> getTestResolveFunction(final FormContextDescription context) {
		return f -> f.apply(s -> {
			if (s.startsWith("#")) {
				final var key = s.substring(1, s.length() - 1);
				if (!context.contains(key)) {
					throw new PegParsingValueException("Variable #" + key + "# not found (" + context.getLastCorrespondingKey(key) + " is not defined)");
				}
				return context.getDummyValue(key);
			}
			return STATIC_RESOLVE_FUNCTION.apply(s);
		});
	}

	/**
	 * Get a function that resolves a string to an object, using the context.
	 *
	 * @param context Context to use
	 * @return The function
	 */
	private static Function<PegComparisonRuleSolver, Boolean> getResolveFunction(final Map<String, Object> context) {
		return f -> f.apply(s -> {
			if (s.startsWith("#")) {
				final var key = s.substring(1, s.length() - 1);
				return ObjectUtil.extractValueFromData(key, context);
			}
			return STATIC_RESOLVE_FUNCTION.apply(s);
		});
	}

	public static class ParseResult {
		private final boolean isValid;
		private final boolean result;
		private final String errorMessage;

		private ParseResult(final PegDelayedOperationSolver<PegComparisonRuleSolver, PegBoolOperatorTerm, Boolean> solver, final Function<PegComparisonRuleSolver, Boolean> resolveFunction) {
			boolean tmpIsValid;
			boolean tmpResult;
			String tmpErrorMessage;
			try {
				tmpResult = solver.solve(resolveFunction);
				tmpIsValid = true;
				tmpErrorMessage = null;
			} catch (final PegParsingValueException e) {
				tmpResult = false;
				tmpIsValid = false;
				tmpErrorMessage = e.getMessage();
			}
			isValid = tmpIsValid;
			result = tmpResult;
			errorMessage = tmpErrorMessage;
		}

		private ParseResult(final PegNoMatchFoundException e) {
			isValid = false;
			result = false;
			errorMessage = e.getRootMessage();
		}

		public boolean isValid() {
			return isValid;
		}

		public boolean getResult() {
			return result;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

	}
}
