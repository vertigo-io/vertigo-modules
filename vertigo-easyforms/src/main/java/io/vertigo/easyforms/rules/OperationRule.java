package io.vertigo.easyforms.rules;

import java.util.ArrayList;

import io.vertigo.commons.peg.PegChoice;
import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.commons.peg.PegResult;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;

/**
 * Rule for parsing an operation.
 *
 * @param <A> Type of the operand
 * @param <B> Type of the operator
 * @param <R> Type of the result
 * @author skerdudou
 */
public class OperationRule<A, B extends Enum<B> & IOperatorTerm<R>, R> implements PegRule<OperationSolver<A, B, R>> {

	private static final PegRule<Dummy> SPACES_RULE = PegRules.blanks(" \t\n\r");
	private static final PegRule<Brackets> OPEN_BRACKET_RULE = EnumRuleHelper.getIndividualRule(Brackets.OPEN);
	private static final PegRule<Brackets> CLOSE_BRACKET_RULE = EnumRuleHelper.getIndividualRule(Brackets.CLOSE);

	private final PegRule<PegChoice> state0Rule;
	private final PegRule<PegChoice> state1Rule;
	private final Class<A> operandClass;
	private final Class<B> operatorClass;

	public OperationRule(final PegRule<A> operandRule, final Class<A> operandClass, final Class<B> operatorClass) {
		this.operandClass = operandClass;
		this.operatorClass = operatorClass;

		final var operatorRule = EnumRuleHelper.getGlobalRule(operatorClass);

		state0Rule = PegRules.choice(operandRule, OPEN_BRACKET_RULE, SPACES_RULE);
		state1Rule = PegRules.choice(operatorRule, CLOSE_BRACKET_RULE, SPACES_RULE);
	}

	@Override
	public String getExpression() {
		return "todo";
	}

	@Override
	public PegResult<OperationSolver<A, B, R>> parse(final String text, final int start) throws PegNoMatchFoundException {
		/*
		state 0 :
		 - ( => state 0, brackets + 1
		 - operand => state 1
		state 1 :
		 - operator => state 0
		 - ) => state 1, brackets - 1

		spaces dont change state
		*/
		var state = 0;
		var bracketsCount = 0;
		var index = start;
		final var rawStack = new ArrayList<>();

		while (true) {
			if (state == 0) {
				final PegResult<PegChoice> result;
				try {
					result = state0Rule.parse(text, index);
				} catch (final PegNoMatchFoundException e) {
					break;
				}
				index = result.getIndex();

				final var value = result.getValue().value();
				if (value instanceof Dummy) {
					// ignore spaces
				} else if (value == Brackets.OPEN) {
					rawStack.add(value);
					bracketsCount++;
				} else { // operand
					rawStack.add(value);
					state = 1;
				}
			} else {
				final PegResult<PegChoice> result;
				try {
					result = state1Rule.parse(text, index);
				} catch (final PegNoMatchFoundException e) {
					throw new PegNoMatchFoundException(text, index, null, "Expecting operator or closing bracket");
				}
				index = result.getIndex();

				final var value = result.getValue().value();
				if (value instanceof Dummy) {
					// ignore spaces
				} else if (value == Brackets.CLOSE) {
					rawStack.add(value);
					bracketsCount--;
					if (bracketsCount < 0) {
						throw new PegNoMatchFoundException(text, index, null, "Unexpected closing bracket");
					}
				} else { // operator
					rawStack.add(value);
					state = 0;
				}
			}

			if (index == text.length()) {
				break;
			}
		}

		if (state == 0) {
			throw new PegNoMatchFoundException(text, index, null, "Expecting value or opening bracket");
		}
		if (bracketsCount > 0) {
			throw new PegNoMatchFoundException(text, index, null, "Missing closing bracket");
		}

		return new PegResult<>(index, new OperationSolver<>(rawStack, operandClass, operatorClass));
	}

}
