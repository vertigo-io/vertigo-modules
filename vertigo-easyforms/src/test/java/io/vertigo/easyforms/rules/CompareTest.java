package io.vertigo.easyforms.rules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertigo.easyforms.rules.term.CompareTerm;
import io.vertigo.easyforms.rules.term.ParsingTypeException;

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
		Assertions.assertThrows(ParsingTypeException.class, () -> {
			CompareTerm.doCompare(s12, s13, CompareTerm.LT);
		});
		Assertions.assertTrue(CompareTerm.doCompare(s12, s12, CompareTerm.EQ));
		Assertions.assertFalse(CompareTerm.doCompare(s12, s12, CompareTerm.NEQ));

		Assertions.assertFalse(CompareTerm.doCompare(s12, s13, CompareTerm.EQ));
		Assertions.assertTrue(CompareTerm.doCompare(s12, s13, CompareTerm.NEQ));

		Assertions.assertThrows(ParsingTypeException.class, () -> {
			CompareTerm.doCompare(i12, s13, CompareTerm.EQ);
		});
	}

}
