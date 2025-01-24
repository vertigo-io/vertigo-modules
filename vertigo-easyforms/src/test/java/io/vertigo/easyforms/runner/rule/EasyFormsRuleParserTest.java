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
package io.vertigo.easyforms.runner.rule;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EasyFormsRuleParserTest {

	@Test
	public void complexRuleTest() {
		checkResult(" (150 + 2)  > (8+2)  *   2 or  10 < 8 and 2 != 3 ", true);

		checkResult(" (150  > (8+2)  *   2 or  10 < 8) and 2 != 3 ", true);

		checkResult(" \"test\" != \"test2\" && \"test2\" = \"test2\"", true);

		checkResult("10.2>10.3", false);

		checkResult("10.2 + 1.0 > 10.3", true);

		checkResult("true = true", true);

		checkResult("#var1# = true", Map.of("var1", Boolean.TRUE), true);

		checkResult("#var1# < #var2#", Map.of("var1", 12, "var2", 13), true);

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

	private void checkResult(final String expression, final boolean result) {
		checkResult(expression, Map.of(), result);
	}

	private void checkResult(final String expression, final Map<String, Object> context, final boolean result) {
		final var res = EasyFormsRuleParser.parse(expression, context);
		Assertions.assertTrue(res.isValid(), "Parse error");
		Assertions.assertEquals(result, res.getResult());
	}
}
