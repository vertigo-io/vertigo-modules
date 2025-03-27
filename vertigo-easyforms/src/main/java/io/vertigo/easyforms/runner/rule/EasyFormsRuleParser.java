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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.commons.peg.PegParsingValueException;
import io.vertigo.commons.peg.PegSolver;
import io.vertigo.commons.peg.PegSolver.PegSolverFunction;
import io.vertigo.commons.peg.rule.PegRule;
import io.vertigo.commons.peg.rule.PegRules;
import io.vertigo.commons.peg.term.PegArithmeticsOperatorTerm;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.util.ObjectUtil;
import io.vertigo.vega.webservice.model.UiObject;

public class EasyFormsRuleParser {

	private EasyFormsRuleParser() {
		// only statics
	}

	private static final EasyFormsRule MAIN_RULE_COMPARISON = new EasyFormsRule();
	private static final PegRule<PegSolver<String, Object, Object>> MAIN_RULE_COMPUTE = PegRules.delayedOperation(new ValueRule(), PegArithmeticsOperatorTerm.class, false);

	private static final Function<String, Object> STATIC_RESOLVE_FUNCTION = s -> {
		if (s.startsWith("\"")) {
			return s.substring(1, s.length() - 1);
		} else if (s.contains(".")) {
			return Double.valueOf(s);
		} else if (s.equalsIgnoreCase("true")) {
			return true;
		} else if (s.equalsIgnoreCase("false")) {
			return false;
		} else if (s.equalsIgnoreCase("null")) {
			return null;
		}
		return Integer.parseInt(s);
	};

	public static ParseResult<Object> parseCompute(final String s, final EasyFormsData data, final Map<String, Serializable> context) {
		try {
			return new ParseResult<>(MAIN_RULE_COMPUTE.parse(s).getValue(), getResolveFunction(buildFullContext(data, context)));
		} catch (final PegNoMatchFoundException e) {
			return new ParseResult<>(e);
		}
	}

	public static ParseResult<Object> parseComputeTest(final String s, final FormContextDescription context) {
		try {
			return new ParseResult<>(MAIN_RULE_COMPUTE.parse(s).getValue(), getTestResolveFunction(context));
		} catch (final PegNoMatchFoundException e) {
			return new ParseResult<>(e);
		}
	}

	public static ParseResult<Boolean> parseComparison(final String s, final EasyFormsData data, final Map<String, Serializable> context) {
		try {
			return new ParseResult<>(MAIN_RULE_COMPARISON.parse(s).getValue(), getResolveFunction(buildFullContext(data, context)));
		} catch (final PegNoMatchFoundException e) {
			return new ParseResult<>(e);
		}
	}

	public static ParseResult<Boolean> parseComparisonTest(final String s, final FormContextDescription context) {
		try {
			return new ParseResult<>(MAIN_RULE_COMPARISON.parse(s).getValue(), getTestResolveFunction(context));
		} catch (final PegNoMatchFoundException e) {
			return new ParseResult<>(e);
		}
	}

	private static Map<String, Object> buildFullContext(final EasyFormsData data, final Map<String, Serializable> context) {
		final Map<String, Object> conditionContext = new HashMap<>();
		if (data != null) {
			conditionContext.putAll(data);
		}
		if (context != null) {
			// resolve UiObject to actual serverSide object (to have typed values and not only String)
			final var resolvedContext = new HashMap<String, Object>();
			for (final var entry : context.entrySet()) {
				if (entry.getValue() instanceof final UiObject<?> uiObject) {
					resolvedContext.put(entry.getKey(), uiObject.getServerSideObject());
				} else {
					resolvedContext.put(entry.getKey(), entry.getValue());
				}
			}
			conditionContext.put("ctx", resolvedContext);
		}
		return conditionContext;
	}

	/**
	 * Get a function that resolves a string to an object, using the context.
	 *
	 * @param context Context to use
	 * @return The function
	 */
	private static PegSolverFunction<String, Object> getTestResolveFunction(final FormContextDescription context) {
		return s -> {
			if (s.startsWith("#")) {
				final var key = s.substring(1, s.length() - 1);
				if (!context.contains(key)) {
					throw new PegParsingValueException("Variable #" + key + "# not found (" + context.getLastCorrespondingKey(key) + " is not defined)");
				}
				return context.getDummyValue(key);
			}
			return STATIC_RESOLVE_FUNCTION.apply(s);
		};
	}

	/**
	 * Get a function that resolves a string to an object, using the context.
	 *
	 * @param context Context to use
	 * @return The function
	 */
	private static PegSolverFunction<String, Object> getResolveFunction(final Map<String, Object> context) {
		return s -> {
			if (s.startsWith("#")) {
				final var key = s.substring(1, s.length() - 1);
				return ObjectUtil.extractValueFromData(key, context);
			}
			return STATIC_RESOLVE_FUNCTION.apply(s);
		};
	}

	public static class ParseResult<R extends Object> {
		private final boolean isValid;
		private final R result;
		private final String errorMessage;

		private ParseResult(final PegSolver<String, Object, R> solver, final PegSolverFunction<String, Object> resolveFunction) {
			boolean tmpIsValid;
			R tmpResult;
			String tmpErrorMessage;
			try {
				tmpResult = solver.apply(resolveFunction);
				tmpIsValid = true;
				tmpErrorMessage = null;
			} catch (final PegParsingValueException e) {
				tmpResult = null;
				tmpIsValid = false;
				tmpErrorMessage = e.getMessage();
			}
			isValid = tmpIsValid;
			result = tmpResult;
			errorMessage = tmpErrorMessage;
		}

		private ParseResult(final PegNoMatchFoundException e) {
			isValid = false;
			result = null;
			errorMessage = e.getRootMessage();
		}

		public boolean isValid() {
			return isValid;
		}

		public R getResult() {
			return result;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

	}
}
