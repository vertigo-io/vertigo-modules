package io.vertigo.easyforms.rules.term;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

import io.vertigo.easyforms.rules.IOperatorTerm;

/**
 * All boolean operators.
 */
public enum ArithmeticsOperator implements IOperatorTerm<Object> {
	PLUS(1, "+"),
	MINUS(1, "-"),
	MULTIPLY(2, "*"),
	DIVIDE(2, "/");

	private final String str;
	private final int priority;
	private final BinaryOperator<Object> binaryOperator;

	ArithmeticsOperator(final int priority, final String str) {
		this.priority = priority;
		this.str = str;

		binaryOperator = (a, b) -> doArithmetics(a, b, this);
	}

	@Override
	public List<String> getStrValues() {
		return Arrays.asList(str);
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public Object apply(final Object left, final Object right) {
		return binaryOperator.apply(left, right);
	}

	private static Object doArithmetics(final Object left, final Object right, final ArithmeticsOperator operator) {
		if (left.getClass() != right.getClass()) {
			throw new ParsingValueException("Cannot compute on different types", left, right, operator.getStrValues().get(0));
		}

		if (left instanceof final String leftStr && right instanceof final String rightStr) {
			if (operator != PLUS) {
				throw new ParsingValueException("Operator '" + operator.str + "' not supported for String", left, right, operator.str);
			}
			return leftStr + rightStr;
		} else if (left instanceof final Integer leftI && right instanceof final Integer rightI) {
			return switch (operator) {
				case PLUS -> leftI + rightI;
				case MINUS -> leftI - rightI;
				case MULTIPLY -> leftI * rightI;
				case DIVIDE -> leftI / rightI;
			};
		} else if (left instanceof final BigDecimal leftD && right instanceof final BigDecimal rightD) {
			return switch (operator) {
				case PLUS -> leftD.add(rightD);
				case MINUS -> leftD.subtract(rightD);
				case MULTIPLY -> leftD.multiply(rightD);
				case DIVIDE -> leftD.divide(rightD);
			};
		}
		// TODO: handle other types (ex dates)
		throw new ParsingValueException("Type '" + left.getClass().getSimpleName() + "' not supported");
	}
}
