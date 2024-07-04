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
package io.vertigo.easyforms.rules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertigo.easyforms.rules.term.CompareTerm;
import io.vertigo.easyforms.rules.term.ParsingValueException;

public class CompareTest {

	@Test
	public void testCompareTerm() {
		final var i12 = Integer.valueOf(12);
		final var i13 = Integer.valueOf(13);

		Assertions.assertTrue(CompareTerm.doCompare(i12, i13, CompareTerm.LT));
		Assertions.assertTrue(CompareTerm.doCompare(i12, i12, CompareTerm.EQ));
		Assertions.assertTrue(CompareTerm.doCompare(i12, i12, CompareTerm.GTE));
		Assertions.assertFalse(CompareTerm.doCompare(i12, i13, CompareTerm.EQ));

		final var s12 = "12";
		final var s13 = "13";
		Assertions.assertThrows(ParsingValueException.class, () -> {
			CompareTerm.doCompare(s12, s13, CompareTerm.LT);
		});
		Assertions.assertTrue(CompareTerm.doCompare(s12, s12, CompareTerm.EQ));
		Assertions.assertFalse(CompareTerm.doCompare(s12, s12, CompareTerm.NEQ));

		Assertions.assertFalse(CompareTerm.doCompare(s12, s13, CompareTerm.EQ));
		Assertions.assertTrue(CompareTerm.doCompare(s12, s13, CompareTerm.NEQ));

		Assertions.assertThrows(ParsingValueException.class, () -> {
			CompareTerm.doCompare(i12, s13, CompareTerm.EQ);
		});
	}

}
