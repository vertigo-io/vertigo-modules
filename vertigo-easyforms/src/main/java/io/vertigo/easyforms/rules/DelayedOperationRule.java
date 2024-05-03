package io.vertigo.easyforms.rules;

import java.util.ArrayList;

import io.vertigo.commons.peg.PegChoice;
import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.commons.peg.PegResult;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;
import io.vertigo.easyforms.rules.term.BracketsTerm;

/**
 * Rule for parsing an operation, resolving terms (operands) later through OperationSolver.
 *
 * @param <A> Type of the operand
 * @param <B> Type of the operator
 * @param <R> Type of the result
 * @author skerdudou
 */
public class DelayedOperationRule<A, B extends Enum<B> & IOperatorTerm<R>, R> implements PegRule<OperationSolver<A, B, R>> {

	private static final PegRule<Dummy> SPACES_RULE = PegRules.blanks();
	private static final PegRule<BracketsTerm> OPEN_BRACKET_RULE = EnumRuleHelper.getIndividualRuleSkipSpaces(BracketsTerm.OPEN);
	private static final PegRule<BracketsTerm> CLOSE_BRACKET_RULE = EnumRuleHelper.getIndividualRuleSkipSpaces(BracketsTerm.CLOSE);

	private final PegRule<PegChoice> state0Rule;
	private final PegRule<PegChoice> state1Rule;
	private final Class<B> operatorClass;
	private boolean matchAll;

	public DelayedOperationRule(final PegRule<A> operandRule, final Class<B> operatorClass, final boolean isOperatorSpaced, final boolean matchAll) {
		this.operatorClass = operatorClass;
		this.matchAll = matchAll;

		final var operatorRule = isOperatorSpaced ? EnumRuleHelper.getSpacedGlobalRule(operatorClass) : EnumRuleHelper.getGlobalRule(operatorClass);

		state0Rule = PegRules.choice(OPEN_BRACKET_RULE, operandRule, SPACES_RULE);
		state1Rule = PegRules.choice(CLOSE_BRACKET_RULE, operatorRule, SPACES_RULE);
	}

	public DelayedOperationRule(final PegRule<A> operandRule, final Class<B> operatorClass, final boolean isOperatorSpaced) {
		this(operandRule, operatorClass, isOperatorSpaced, true);
	}

	@Override
	public String getExpression() {
		return "(" + state0Rule.getExpression() + " ~ " + state1Rule.getExpression() + ")";
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
		var endParsingIndex = start; // don't count spaces at the end of the parsing
		final var rawStack = new ArrayList<>();

		while (true) {
			if (state == 0) {
				final PegResult<PegChoice> result = state0Rule.parse(text, index); // can throw PegNoMatchFoundException if operand rule is not respected

				index = result.getIndex();
				final var value = result.getValue().value();

				if (value instanceof Dummy) {
					// ignore spaces
				} else {
					if (value == BracketsTerm.OPEN) {
						rawStack.add(value);
						bracketsCount++;
					} else { // operand (enforced by state0Rule)
						rawStack.add(value);
						state = 1;
					}
					endParsingIndex = index;
				}
			} else {
				final PegResult<PegChoice> result;
				try {
					result = state1Rule.parse(text, index);
				} catch (final PegNoMatchFoundException e) {
					if (matchAll) {
						throw new PegNoMatchFoundException(text, endParsingIndex, null, "Expecting operator or closing bracket");
					} else {
						break;
					}
				}

				index = result.getIndex();
				final var value = result.getValue().value();

				if (value instanceof Dummy) {
					// ignore spaces
				} else {
					if (value == BracketsTerm.CLOSE) {
						bracketsCount--;
						if (bracketsCount < 0) {
							if (matchAll) {
								throw new PegNoMatchFoundException(text, endParsingIndex, null, "Unexpected closing bracket");
							} else {
								break;
							}
						}
						rawStack.add(value);
					} else { // operator (enforced by state1Rule)
						rawStack.add(value);
						state = 0;
					}
					endParsingIndex = index;
				}

			}

			if (index == text.length()) {
				break;
			}
		}

		if (state == 0) {
			throw new PegNoMatchFoundException(text, endParsingIndex, null, "Expecting value or opening bracket");
		}
		if (bracketsCount > 0) {
			throw new PegNoMatchFoundException(text, endParsingIndex, null, "Missing closing bracket");
		}

		return new PegResult<>(endParsingIndex, new OperationSolver<>(rawStack, operatorClass));
	}

}
