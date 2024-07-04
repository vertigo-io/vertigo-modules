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
package io.vertigo.easyforms.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import io.vertigo.easyforms.rules.term.BracketsTerm;

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
	private final Class<B> operatorClass;

	OperationSolver(final List<Object> inputStack, final Class<B> operatorClass) {
		// Resolving the parentheses and operator priority by converting to reverse polish notation
		// using https://en.wikipedia.org/wiki/Shunting_yard_algorithm

		this.operatorClass = operatorClass;
		rawStack = new ArrayList<>();

		final var operatorStack = new Stack<>();
		for (final var o : inputStack) {
			if (operatorClass.isAssignableFrom(o.getClass())) {
				// operator
				final B operator = operatorClass.cast(o);
				while (!operatorStack.isEmpty() && operatorStack.peek() != BracketsTerm.OPEN) {
					final var prevOp = operatorClass.cast(operatorStack.peek());
					if (prevOp.getPriority() > operator.getPriority()) {
						rawStack.add(operatorStack.pop());
					} else {
						break;
					}
				}
				operatorStack.add(o);
			} else if (o == BracketsTerm.OPEN) {
				operatorStack.add(o);
			} else if (o == BracketsTerm.CLOSE) {
				while (!operatorStack.isEmpty() && operatorStack.peek() != BracketsTerm.OPEN) {
					rawStack.add(operatorStack.pop());
				}
				if (operatorStack.isEmpty()) {
					throw new IllegalArgumentException("Mismatched parentheses");
				}
				operatorStack.remove(operatorStack.size() - 1); // remove the open bracket
			} else {
				// operand (enforced by OperationRule)
				rawStack.add(o);
			}

		}

		while (!operatorStack.isEmpty()) {
			rawStack.add(operatorStack.pop());
		}

	}

	/**
	 * Solve the expression.
	 *
	 * @param operandResolver Function to parse the operand value
	 * @return the result
	 */
	public R solve(final Function<A, R> operandResolver) {
		final var inStack = resolveValues(operandResolver);
		final var workingStack = new Stack<R>();

		// stack is in reverse polish notation, just apply the operators on the 2 last operands
		for (final var element : inStack) {
			if (element != null && operatorClass.isAssignableFrom(element.getClass())) {
				final var operator = operatorClass.cast(element);
				final var right = workingStack.pop();
				final var left = workingStack.pop();
				workingStack.add(operator.apply(left, right));
			} else {
				// operand
				workingStack.add((R) element); // enforced by resolveValues
			}
		}

		if (workingStack.size() != 1) {
			throw new IllegalArgumentException("Invalid expression");
		}

		return workingStack.pop();
	}

	private List<Object> resolveValues(final Function<A, R> operandResolver) {
		final var outStack = new ArrayList<>(rawStack.size());
		for (final var elem : rawStack) {
			if (operatorClass.isAssignableFrom(elem.getClass())) {
				outStack.add(elem); // operator
			} else {
				// operand (enforced by constructor/OperationRule)
				// raw value ready to be resolved
				final R value = operandResolver.apply((A) elem);
				outStack.add(value);
			}
		}
		return outStack;
	}

}
