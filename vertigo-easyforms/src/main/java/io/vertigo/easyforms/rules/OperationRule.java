package io.vertigo.easyforms.rules;

import java.util.function.Function;

import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.commons.peg.PegResult;
import io.vertigo.commons.peg.PegRule;

public class OperationRule<A, B extends Enum<B> & IOperatorTerm<A>> implements PegRule<A> {

	private final DelayedOperationRule<A, B, A> mainRule;

	public OperationRule(final PegRule<A> operandRule, final Class<B> operatorClass, final boolean isOperatorSpaced, final boolean matchAll) {
		mainRule = new DelayedOperationRule<>(operandRule, operatorClass, isOperatorSpaced, matchAll);
	}

	public OperationRule(final PegRule<A> operandRule, final Class<B> operatorClass, final boolean isOperatorSpaced) {
		mainRule = new DelayedOperationRule<>(operandRule, operatorClass, isOperatorSpaced);
	}

	@Override
	public String getExpression() {
		return mainRule.getExpression();
	}

	@Override
	public PegResult<A> parse(final String text, final int start) throws PegNoMatchFoundException {
		final var mainResult = mainRule.parse(text, start);

		return new PegResult<>(mainResult.getIndex(), mainResult.getValue().solve(Function.identity()));
	}

}
