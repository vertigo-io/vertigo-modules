package io.vertigo.easyforms.rules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertigo.commons.peg.AbstractRule;
import io.vertigo.commons.peg.PegChoice;
import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;
import io.vertigo.easyforms.rules.term.BoolOperator;

public class RulesTest {
	private static final OperationRule<Boolean, BoolOperator> BOOLEAN_RULE = new OperationRule<>(new TermRule(), BoolOperator.class, true);
	private static final OperationRule<Boolean, BoolOperator> BOOLEAN_RULE_LAX = new OperationRule<>(new TermRule(), BoolOperator.class, true, false);

	@Test
	public void testBooleanRule() throws PegNoMatchFoundException {
		final var result = BOOLEAN_RULE.parse("true or false AND false");
		Assertions.assertTrue(result.getValue());

		final var result2 = BOOLEAN_RULE.parse("false  and false or true");
		Assertions.assertTrue(result2.getValue());

		final var result3 = BOOLEAN_RULE.parse("(true or false) AND false");
		Assertions.assertFalse(result3.getValue());

		final var result4 = BOOLEAN_RULE.parse("(true or (false or false and true)) AND (false or false) and true");
		Assertions.assertFalse(result4.getValue());
	}

	@Test
	public void testErrors() {
		// Parenthesis mismatch
		Assertions.assertThrows(PegNoMatchFoundException.class, () -> {
			BOOLEAN_RULE.parse("true or false ) or false");
		});
		Assertions.assertThrows(PegNoMatchFoundException.class, () -> {
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

		// other errors
		Assertions.assertThrows(PegNoMatchFoundException.class, () -> {
			BOOLEAN_RULE.parse("true 12"); // lack operator
		});

		Assertions.assertThrows(PegNoMatchFoundException.class, () -> {
			BOOLEAN_RULE.parse("true&&false"); // lack space between operator
		});
	}

	@Test
	public void testLax() throws PegNoMatchFoundException {
		final var result = BOOLEAN_RULE_LAX.parse("true or false AND false 42");
		Assertions.assertEquals(23, result.getIndex());
		Assertions.assertTrue(result.getValue());

		final var result2 = BOOLEAN_RULE_LAX.parse("true or false AND false)");
		Assertions.assertEquals(23, result2.getIndex());
		Assertions.assertTrue(result2.getValue());
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
