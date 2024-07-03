package io.vertigo.easyforms.rules.term;

import java.util.List;

import io.vertigo.easyforms.rules.ITermRule;

/**
 * All comparison operators.
 */
public enum CompareTerm implements ITermRule {
	// Refacto : see : Equivalent of ValueOperator

	/** Lesser Than or Equals. */
	LTE("<="),
	/** Greater Than or Equals. */
	GTE(">="),
	/** Not Equals. */
	NEQ("!="),
	/** Equals. */
	EQ("="),
	/** Lesser Than. */
	LT("<"),
	/** Greater Than. */
	GT(">");

	private final String str;

	CompareTerm(final String str) {
		this.str = str;
	}

	@Override
	public List<String> getStrValues() {
		return List.of(str);
	}

	public static Boolean doCompare(final Object left, final Object right, final CompareTerm operator) {
		final Object leftResolved = convertIntegerToLong(left);
		final Object rightResolved = convertIntegerToLong(right);

		if (leftResolved != null && rightResolved != null && leftResolved.getClass() != rightResolved.getClass()) {
			throw new ParsingValueException("Cannot compare different types", leftResolved, rightResolved, operator.str);
		}

		if (leftResolved instanceof String && operator != EQ && operator != NEQ) {
			throw new ParsingValueException("Operator '" + operator.str + "' not supported for String", leftResolved, rightResolved, operator.str);
		}

		if (leftResolved == null || rightResolved == null) {
			if (operator == EQ) {
				return leftResolved == rightResolved;
			} else if (operator == NEQ) {
				return leftResolved != rightResolved;
			} else {
				return false;
			}
		}

		final int compareResult;
		if (leftResolved instanceof final Comparable leftResolvedC && rightResolved instanceof final Comparable rightResolvedC) {
			compareResult = leftResolvedC.compareTo(rightResolvedC);
		} else {
			throw new ParsingValueException("Type '" + leftResolved.getClass() + "' not supported");
		}

		switch (operator) {
			case LTE:
				return compareResult <= 0;
			case GTE:
				return compareResult >= 0;
			case NEQ:
				return compareResult != 0;
			case EQ:
				return compareResult == 0;
			case LT:
				return compareResult < 0;
			case GT:
				return compareResult > 0;
			default:
				throw new ParsingValueException("Operator '" + operator.str + "' not supported");
		}

	}

	private static Object convertIntegerToLong(final Object o) {
		if (o instanceof final Integer i) {
			return Long.valueOf(i.longValue());
		}
		return o;
	}

}
