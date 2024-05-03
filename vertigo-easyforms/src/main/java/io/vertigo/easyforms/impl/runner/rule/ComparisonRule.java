package io.vertigo.easyforms.impl.runner.rule;

import java.util.List;
import java.util.function.Function;

import io.vertigo.commons.peg.AbstractRule;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;
import io.vertigo.easyforms.rules.EnumRuleHelper;
import io.vertigo.easyforms.rules.DelayedOperationRule;
import io.vertigo.easyforms.rules.OperationSolver;
import io.vertigo.easyforms.rules.term.ArithmeticsOperator;
import io.vertigo.easyforms.rules.term.CompareTerm;

public class ComparisonRule extends AbstractRule<Function<Function<String, Object>, Boolean>, List<Object>> {

	private static final DelayedOperationRule<String, ArithmeticsOperator, Object> TERM_RULE = new DelayedOperationRule<>(new ValueRule(), ArithmeticsOperator.class, false, false);

	private static final PegRule<List<Object>> COMPARATOR_RULE = PegRules.sequence(
			TERM_RULE, // 0
			PegRules.skipBlanks(),
			EnumRuleHelper.getGlobalRule(CompareTerm.class), // 2
			PegRules.skipBlanks(),
			TERM_RULE); // 4

	public ComparisonRule() {
		super(COMPARATOR_RULE);
	}

	@Override
	protected Function<Function<String, Object>, Boolean> handle(final List<Object> elements) {
		return f -> {
			final var leftVal = ((OperationSolver<String, ArithmeticsOperator, Object>) elements.get(0)).solve(f);
			final var rightVal = ((OperationSolver<String, ArithmeticsOperator, Object>) elements.get(4)).solve(f);

			return CompareTerm.doCompare(leftVal, rightVal, (CompareTerm) elements.get(2));
		};
	}

}
