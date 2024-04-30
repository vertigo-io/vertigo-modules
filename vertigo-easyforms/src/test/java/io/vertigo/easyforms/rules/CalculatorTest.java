package io.vertigo.easyforms.rules;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertigo.commons.peg.AbstractRule;
import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;
import io.vertigo.commons.peg.PegWordRule;

public class CalculatorTest {

	private static final OperationRule<Integer, CalculatorOperator, Integer> CALCULATOR_RULE = new OperationRule<>(new TermRule(), Integer.class, CalculatorOperator.class);

	@Test
	public void testCalculatorRule() throws PegNoMatchFoundException {
		final var result = CALCULATOR_RULE.parse("2*3");
		Assertions.assertEquals(6, result.getValue().solve(a -> a));

		final var result2 = CALCULATOR_RULE.parse("2+2*3");
		Assertions.assertEquals(8, result2.getValue().solve(a -> a));

		final var result3 = CALCULATOR_RULE.parse("2*3+2");
		Assertions.assertEquals(8, result3.getValue().solve(a -> a));

		final var result4 = CALCULATOR_RULE.parse("(2+2)*3");
		Assertions.assertEquals(12, result4.getValue().solve(a -> a));

		final var result5 = CALCULATOR_RULE.parse("121 /   11");
		Assertions.assertEquals(11, result5.getValue().solve(a -> a));
	}

	@Test
	public void testFail() {
		//l'opérateur  $ n'existe pas
		Assertions.assertThrows(PegNoMatchFoundException.class, () -> CALCULATOR_RULE.parse("2 $ 3"));
	}

	public enum CalculatorOperator implements IOperatorTerm<Integer> {
		PLUS(1, (a, b) -> a + b, "+"),
		MINUS(1, (a, b) -> a - b, "-"),
		MULTIPLY(2, (a, b) -> a * b, "*"),
		DIVIDE(2, (a, b) -> a / b, "/");

		private final List<String> operators;
		private final int priority;
		private final BinaryOperator<Integer> binaryOperator;

		CalculatorOperator(final int priority, final BinaryOperator<Integer> binaryOperator, final String... operators) {
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
		public Integer apply(final Integer left, final Integer right) {
			return binaryOperator.apply(left, right);
		}

	}

	private static class TermRule extends AbstractRule<Integer, String> {

		private static final PegRule<String> TERM_RULE = PegRules.word(false, "0123456789", PegWordRule.Mode.ACCEPT, "0-9+");

		public TermRule() {
			super(TERM_RULE);
		}

		@Override
		protected Integer handle(final String number) {
			return Integer.valueOf(number);
		}

	}
}
