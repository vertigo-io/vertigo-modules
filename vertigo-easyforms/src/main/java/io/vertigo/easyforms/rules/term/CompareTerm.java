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
		if (left != null && right != null && left.getClass() != right.getClass()) {
			throw new ParsingValueException("Cannot compare different types", left, right, operator.str);
		}

		if (left instanceof String && operator != EQ && operator != NEQ) {
			throw new ParsingValueException("Operator '" + operator.str + "' not supported for String", left, right, operator.str);
		}

		if (left == null || right == null) {
			if (operator == EQ) {
				return left == right;
			} else if (operator == NEQ) {
				return left != right;
			} else {
				return false;
			}
		}

		final int compareResult;
		if (left instanceof final Comparable leftC && right instanceof final Comparable rightC) {
			compareResult = leftC.compareTo(rightC);
		} else {
			throw new ParsingValueException("Type '" + left.getClass() + "' not supported");
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

}
