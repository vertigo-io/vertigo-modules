package io.vertigo.easyforms.rules.term;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

import io.vertigo.easyforms.rules.IOperatorTerm;

/**
 * All boolean operators.
 */
public enum BoolOperator implements IOperatorTerm<Boolean> {
	OR(1, (a, b) -> a || b, "OR", "Or", "or", "||"),
	AND(2, (a, b) -> a && b, "AND", "And", "and", "&&");

	private final List<String> operators;
	private final int priority;
	private final BinaryOperator<Boolean> binaryOperator;

	BoolOperator(final int priority, final BinaryOperator<Boolean> binaryOperator, final String... operators) {
		this.priority = priority;
		this.binaryOperator = binaryOperator;
		this.operators = Arrays.asList(operators);
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
	public Boolean apply(final Boolean left, final Boolean right) {
		return binaryOperator.apply(left, right);
	}

}
