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

	private final List<String> operators;
	private final int priority;
	private final BinaryOperator<Object> binaryOperator;

	ArithmeticsOperator(final int priority, final String... operators) {
		this.priority = priority;
		this.operators = Arrays.asList(operators);

		binaryOperator = (a, b) -> doArithmetics(a, b, this);
	}

	@Override
	public List<String> getStrValues() {
		return operators;
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
			throw new ParsingTypeException("Cannot compute on different types", left, right, operator.getStrValues().get(0));
		}

		if (left instanceof final Integer leftI && right instanceof final Integer rightI) {
			switch (operator) {
				case PLUS:
					return leftI + rightI;
				case MINUS:
					return leftI - rightI;
				case MULTIPLY:
					return leftI * rightI;
				case DIVIDE:
					return leftI / rightI;
			}
		} else if (left instanceof final BigDecimal leftD && right instanceof final BigDecimal rightD) {
			switch (operator) {
				case PLUS:
					return leftD.add(rightD);
				case MINUS:
					return leftD.subtract(rightD);
				case MULTIPLY:
					return leftD.multiply(rightD);
				case DIVIDE:
					return leftD.divide(rightD);
			}
		}
		// TODO: handle other types (ex dates)
		throw new ParsingTypeException("Type '" + left.getClass().getSimpleName() + "' not supported");
	}
}
