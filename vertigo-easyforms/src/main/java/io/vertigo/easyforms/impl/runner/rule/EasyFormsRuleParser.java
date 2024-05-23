package io.vertigo.easyforms.impl.runner.rule;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.util.BeanUtil;
import io.vertigo.easyforms.impl.runner.rule.ComparisonRule.ComparisonRuleSolver;
import io.vertigo.easyforms.rules.DelayedOperationRule;
import io.vertigo.easyforms.rules.OperationSolver;
import io.vertigo.easyforms.rules.term.BoolOperator;
import io.vertigo.easyforms.rules.term.ParsingValueException;

public class EasyFormsRuleParser {

	private static final DelayedOperationRule<ComparisonRuleSolver, BoolOperator, Boolean> MAIN_RULE = new DelayedOperationRule<>(new ComparisonRule(), BoolOperator.class, true, true);

	private static final Function<String, Object> STATIC_RESOLVE_FUNCTION = s -> {
		if (s.startsWith("\"")) {
			return s.substring(1, s.length() - 1);
		} else if (s.contains(".")) {
			return new BigDecimal(s);
		} else if (s.equalsIgnoreCase("true")) {
			return true;
		} else if (s.equalsIgnoreCase("false")) {
			return false;
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
	private static Function<ComparisonRuleSolver, Boolean> getTestResolveFunction(final FormContextDescription context) {
		return f -> f.apply(s -> {
			if (s.startsWith("#")) {
				final var key = s.substring(1, s.length() - 1);
				if (!context.contains(key)) {
					throw new ParsingValueException("Variable #" + key + "# not found (" + context.getLastCorrespondingKey(key) + " is not defined)");
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
	private static Function<ComparisonRuleSolver, Boolean> getResolveFunction(final Map<String, Object> context) {
		return f -> f.apply(s -> {
			if (s.startsWith("#")) {
				final var key = s.substring(1, s.length() - 1);
				return extractValueFromMap(key, "", key, context);
			}
			return STATIC_RESOLVE_FUNCTION.apply(s);
		});
	}

	private static Object extractValueFromMap(final String initialKey, final String parsedKey, final String key, final Object data) {
		final String[] keys = key.split("\\.", 2);
		final String prefix = keys[0];
		final var currentGlobalKey = (parsedKey == "" ? "" : parsedKey + ".") + prefix;
		if (!containsKey(data, prefix)) {
			throw new ParsingValueException("Variable #" + initialKey + "# not found (" + currentGlobalKey + " is not defined)");
		}

		if (keys.length == 1) {
			return getValue(data, prefix);
		} else {
			final String restOfKey = keys[1];
			return extractValueFromMap(initialKey, currentGlobalKey, restOfKey, getValue(data, prefix));
		}
	}

	private static boolean containsKey(final Object o, final String key) {
		if (o instanceof final Map map) {
			return map.containsKey(key);
		}
		try {
			BeanUtil.getPropertyDescriptor(key, o.getClass());
			return true;
		} catch (final VSystemException e) {
			return false;
		}
	}

	private static Object getValue(final Object o, final String key) {
		if (o instanceof final Map map) {
			return map.get(key);
		}
		return BeanUtil.getValue(o, key);

	}

	public static class ParseResult {
		private final boolean isValid;
		private final boolean result;
		private final String errorMessage;

		private ParseResult(final OperationSolver<ComparisonRuleSolver, BoolOperator, Boolean> solver, final Function<ComparisonRuleSolver, Boolean> resolveFunction) {
			boolean tmpIsValid;
			boolean tmpResult;
			String tmpErrorMessage;
			try {
				tmpResult = solver.solve(resolveFunction);
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
