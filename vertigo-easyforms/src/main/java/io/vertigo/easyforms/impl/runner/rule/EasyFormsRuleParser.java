package io.vertigo.easyforms.impl.runner.rule;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.easyforms.rules.DelayedOperationRule;
import io.vertigo.easyforms.rules.OperationSolver;
import io.vertigo.easyforms.rules.term.BoolOperator;
import io.vertigo.easyforms.rules.term.ParsingValueException;

public class EasyFormsRuleParser {

	private static final DelayedOperationRule<Function<Function<String, Object>, Boolean>, BoolOperator, Boolean> MAIN_RULE = new DelayedOperationRule<>(new ComparisonRule(), BoolOperator.class, true,
			true);

	private static final BiFunction<Function<Function<String, Object>, Boolean>, Map<String, Object>, Boolean> GENERIC_RESOLVE_FUNCTION = (r, m) -> r.apply(s -> {
		if (s.startsWith("\"")) {
			return s.substring(1, s.length() - 1);
		} else if (s.startsWith("#")) {
			final var key = s.substring(1, s.length() - 1);

			return extractValue(key, "", key, m);
		} else if (s.contains(".")) {
			return new BigDecimal(s);
		} else if (s.equalsIgnoreCase("true")) {
			return true;
		} else if (s.equalsIgnoreCase("false")) {
			return false;
		}
		return Integer.parseInt(s);
	});

	/**
	 * Get a function that resolves a string to an object, using the context.
	 *
	 * @param context Context to use
	 * @return The function
	 */
	private static Function<Function<Function<String, Object>, Boolean>, Boolean> getResolveFunction(final Map<String, Object> context) {
		return r -> GENERIC_RESOLVE_FUNCTION.apply(r, context);
	}

	public static ParseResult parse(final String s, final Map<String, Object> context) {
		try {
			return new ParseResult(MAIN_RULE.parse(s).getValue(), context);
		} catch (final PegNoMatchFoundException e) {
			return new ParseResult(e);
		}
	}

	private static Object extractValue(final String initialKey, final String parsedKey, final String key, final Map<String, Object> map) {
		final String[] keys = key.split("\\.", 2);
		final String prefix = keys[0];
		final var currentGlobalKey = (parsedKey == "" ? "" : parsedKey + ".") + prefix;
		if (!map.containsKey(prefix)) {
			throw new ParsingValueException("Variable #" + initialKey + "# not found (" + currentGlobalKey + " is not defined)");
		}

		if (keys.length == 1) {
			return map.get(prefix);
		} else {
			final String restOfKey = keys[1];
			if (map.get(prefix) instanceof final Map subMap) {
				return extractValue(initialKey, currentGlobalKey, restOfKey, subMap);
			}
			throw new ParsingValueException("Variable #" + initialKey + "# is incorrect, " + currentGlobalKey + " is not an object.");
		}
	}

	public static class ParseResult {
		private final boolean isValid;
		private final boolean result;
		private final String errorMessage;

		private ParseResult(final OperationSolver<Function<Function<String, Object>, Boolean>, BoolOperator, Boolean> solver, final Map<String, Object> context) {
			boolean tmpIsValid;
			boolean tmpResult;
			String tmpErrorMessage;
			try {
				tmpResult = solver.solve(getResolveFunction(context));
				tmpIsValid = true;
				tmpErrorMessage = null;
			} catch (final ParsingValueException e) {
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
