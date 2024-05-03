package io.vertigo.easyforms.rules;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertigo.easyforms.impl.runner.rule.EasyFormsRuleParser;

public class EasyFormsRuleParserTest {

	@Test
	public void complexRuleTest() {
		final var result = EasyFormsRuleParser.parse(" (150  > (8+2)  *   2 or  10 < 8) and 2 != 3 ", Map.of());
		Assertions.assertTrue(result.isValid());
		Assertions.assertTrue(result.getResult());

		final var result2 = EasyFormsRuleParser.parse(" \"test\" != \"test2\" && \"test2\" = \"test2\"", Map.of());
		Assertions.assertTrue(result2.isValid());
		Assertions.assertTrue(result2.getResult());

		final var result3 = EasyFormsRuleParser.parse("10.2>10.3", Map.of());
		Assertions.assertTrue(result3.isValid());
		Assertions.assertFalse(result3.getResult());

		final var result4 = EasyFormsRuleParser.parse("10.2 + 1.0 > 10.3", Map.of());
		Assertions.assertTrue(result4.isValid());
		Assertions.assertTrue(result4.getResult());

		final var result5 = EasyFormsRuleParser.parse("true = true", Map.of());
		Assertions.assertTrue(result5.isValid());
		Assertions.assertTrue(result5.getResult());

		final var result6 = EasyFormsRuleParser.parse("#var1# = true", Map.of("var1", Boolean.TRUE));
		Assertions.assertTrue(result6.isValid());
		Assertions.assertTrue(result6.getResult());

		final var result7 = EasyFormsRuleParser.parse("#var1# < #var2#", Map.of("var1", 12, "var2", 13));
		Assertions.assertTrue(result7.isValid());
		Assertions.assertTrue(result7.getResult());

		// syntax errors
		final var resultErrSyntax = EasyFormsRuleParser.parse(" \"test  ", Map.of()); // string never ends
		Assertions.assertFalse(resultErrSyntax.isValid());

		// resolving errors (type dependent)
		final var resultErrType1 = EasyFormsRuleParser.parse(" \"test\" > \"test2\" ", Map.of());
		Assertions.assertFalse(resultErrType1.isValid());

		final var resultErrType2 = EasyFormsRuleParser.parse(" 12 > \"test2\" ", Map.of());
		Assertions.assertFalse(resultErrType2.isValid());

		final var resultErrType3 = EasyFormsRuleParser.parse(" 12 + true < 12", Map.of());
		Assertions.assertFalse(resultErrType3.isValid());
	}
}
