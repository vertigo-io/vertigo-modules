package io.vertigo.easyforms.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

/**
 * Class to solve the operation.
 *
 * @param <A> Type of the operand
 * @param <B> Type of the operator
 * @param <R> Type of the result
 * @author skerdudou
 */
public class OperationSolver<A, B extends IOperatorTerm<R>, R> {

	private final List<Object> rawStack; // Reverse Polish notation stack
	private final Class<A> operandClass;
	private final Class<B> operatorClass;

	OperationSolver(final List<Object> inputStack, final Class<A> operandClass, final Class<B> operatorClass) {
		// Resolving the parentheses and operator priority by converting to reverse polish notation
		// using https://en.wikipedia.org/wiki/Shunting_yard_algorithm

		this.operandClass = operandClass;
		this.operatorClass = operatorClass;
		rawStack = new ArrayList<>();

		final var operatorStack = new Stack<>();
		for (final var o : inputStack) {
			if (operandClass.isAssignableFrom(o.getClass())) {
				// operand
				rawStack.add(o);
			} else if (operatorClass.isAssignableFrom(o.getClass())) {
				// operator
				final B operator = operatorClass.cast(o);
				while (!operatorStack.isEmpty() && operatorStack.peek() != Brackets.OPEN) {
					final var prevOp = operatorClass.cast(operatorStack.peek());
					if (prevOp.getPriority() > operator.getPriority()) {
						rawStack.add(operatorStack.pop());
					} else {
						break;
					}
				}
				operatorStack.add(o);
			} else if (o == Brackets.OPEN) {
				operatorStack.add(o);
			} else if (o == Brackets.CLOSE) {
				while (!operatorStack.isEmpty() && operatorStack.peek() != Brackets.OPEN) {
					rawStack.add(operatorStack.pop());
				}
				if (operatorStack.isEmpty()) {
					throw new IllegalArgumentException("Mismatched parentheses");
				}
				operatorStack.remove(operatorStack.size() - 1); // remove the open bracket
			} else {
				throw new IllegalArgumentException("Unknown token: " + o);
			}

		}

		while (!operatorStack.isEmpty()) {
			rawStack.add(operatorStack.pop());
		}

	}

	/**
	 * Solve the expression.
	 *
	 * @param operandFunction Function to parse the operand value
	 * @param operatorFunction Function to apply the operator
	 * @return the result
	 */
	public R solve(final Function<A, R> operandFunction) {
		final var workingStack = new Stack<R>();

		// stack is in reserve polish notation, just apply the operators on the 2 last operands
		for (final var element : rawStack) {
			if (operandClass.isAssignableFrom(element.getClass())) {
				workingStack.add(operandFunction.apply(operandClass.cast(element)));
			} else if (operatorClass.isAssignableFrom(element.getClass())) {
				final var operator = operatorClass.cast(element);
				final var right = workingStack.pop();
				final var left = workingStack.pop();
				workingStack.add(operator.apply(left, right));
			}
		}

		if (workingStack.size() != 1) {
			throw new IllegalArgumentException("Invalid expression");
		}

		return workingStack.pop();
	}

}
