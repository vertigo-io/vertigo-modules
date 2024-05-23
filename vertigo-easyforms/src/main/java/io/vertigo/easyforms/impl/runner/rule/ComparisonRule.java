package io.vertigo.easyforms.impl.runner.rule;

import java.util.List;
import java.util.function.Function;

import io.vertigo.commons.peg.AbstractRule;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;
import io.vertigo.easyforms.impl.runner.rule.ComparisonRule.ComparisonRuleSolver;
import io.vertigo.easyforms.rules.DelayedOperationRule;
import io.vertigo.easyforms.rules.EnumRuleHelper;
import io.vertigo.easyforms.rules.OperationSolver;
import io.vertigo.easyforms.rules.term.ArithmeticsOperator;
import io.vertigo.easyforms.rules.term.CompareTerm;

public class ComparisonRule extends AbstractRule<ComparisonRuleSolver, List<Object>> {

	private static final DelayedOperationRule<String, ArithmeticsOperator, Object> TERM_RULE = new DelayedOperationRule<>(new ValueRule(), ArithmeticsOperator.class, false, false);

	private static final PegRule<List<Object>> COMPARATOR_RULE = PegRules.named(
			PegRules.sequence(
					TERM_RULE, // 0
					PegRules.skipBlanks(),
					PegRules.named(EnumRuleHelper.getGlobalRule(CompareTerm.class), "comparator", "Expected {0}"), // 2
					PegRules.skipBlanks(),
					TERM_RULE), // 4
			"term comparator term");

	public ComparisonRule() {
		super(COMPARATOR_RULE);
	}

	@Override
	protected ComparisonRuleSolver handle(final List<Object> elements) {
		return f -> {
			final var leftVal = ((OperationSolver<String, ArithmeticsOperator, Object>) elements.get(0)).solve(f);
			final var rightVal = ((OperationSolver<String, ArithmeticsOperator, Object>) elements.get(4)).solve(f);

			return CompareTerm.doCompare(leftVal, rightVal, (CompareTerm) elements.get(2));
		};
	}

	public static interface ComparisonRuleSolver extends Function<Function<String, Object>, Boolean> {
	};

}
