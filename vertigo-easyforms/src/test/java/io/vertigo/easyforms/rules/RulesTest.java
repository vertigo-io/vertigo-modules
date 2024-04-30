package io.vertigo.easyforms.rules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertigo.commons.peg.AbstractRule;
import io.vertigo.commons.peg.PegChoice;
import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;

public class RulesTest {
	private static final OperationRule<Boolean, BoolOperator, Boolean> BOOLEAN_RULE = new OperationRule<>(new TermRule(), Boolean.class, BoolOperator.class);

	@Test
	public void testBooleanRule() throws PegNoMatchFoundException {
		final var result = BOOLEAN_RULE.parse("true or false AND false");
		Assertions.assertTrue(result.getValue().solve(a -> a));

		final var result2 = BOOLEAN_RULE.parse("false and false or true");
		Assertions.assertTrue(result2.getValue().solve(a -> a));

		final var result3 = BOOLEAN_RULE.parse("(true or false) AND false");
		Assertions.assertFalse(result3.getValue().solve(a -> a));

		final var result4 = BOOLEAN_RULE.parse("(true or (false or false and true)) AND (false or false) and true");
		Assertions.assertFalse(result4.getValue().solve(a -> a));
	}

	@Test
	public void testParenthesisMismatch() {
		Assertions.assertThrowsExactly(PegNoMatchFoundException.class, () -> {
			BOOLEAN_RULE.parse("true or false AND ) false");
		});

		Assertions.assertThrows(PegNoMatchFoundException.class, () -> {
			BOOLEAN_RULE.parse("(true or false AND false");
		});

		Assertions.assertThrows(PegNoMatchFoundException.class, () -> {
			BOOLEAN_RULE.parse("true true");
		});

		Assertions.assertThrows(PegNoMatchFoundException.class, () -> {
			BOOLEAN_RULE.parse("or");
		});

		Assertions.assertThrows(PegNoMatchFoundException.class, () -> {
			BOOLEAN_RULE.parse("(true or ) false AND false");
		});
	}

	private static class TermRule extends AbstractRule<Boolean, PegChoice> {

		private static final PegRule<PegChoice> TERM_RULE = PegRules.choice(PegRules.term("true"), PegRules.term("false"));

		public TermRule() {
			super(TERM_RULE);
		}

		@Override
		protected Boolean handle(final PegChoice choice) {
			return Boolean.valueOf(choice.value().toString());
		}

	}

}
