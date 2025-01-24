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
package io.vertigo.easyforms.impl.easyformsrunner.pack.constraint;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertigo.easyforms.runner.pack.constraint.ConstraintPhone;
import io.vertigo.easyforms.runner.pack.constraint.ConstraintPhone.ConstraintPhoneEnum;

/**
 * Phone constraint tests.
 *
 * @author skerdudou
 */
public final class ConstraintPhoneTest {

	private static final String phoneUs = "+1 (123) 456-7890";

	private static final String phoneFrFixe = "01.23.45.67.89";
	private static final String phoneFrFixe2 = "+33 1 234 567 89";

	private static final String phoneFrFixeKo = "+3312345678 9";
	private static final String phoneFrFixeKo2 = "+33123456789.";

	private static final String phoneFrMobile = "06.12345678";
	private static final String phoneFrMobile2 = "07.12345678";
	private static final String phoneFrMobile3 = "+33712345678";

	private static final String phoneFrFixeGuadeloupe = "+590 590 123456";
	private static final String phoneFrMobileGuadeloupe = "+590 690 33 44 55";
	private static final String phoneFrMobileGuadeloupeKo = "+590 692 33 44 55";

	private static final String phoneFrFixeStPier = "+508 41 66 77";
	private static final String phoneFrMobileStPier = "+508 55 66 77";

	private static final String phoneFrFixeWallis = "+681 72 22 33";
	private static final String phoneFrMobileWallis = "+681 44 55 66";

	private static final String phoneFrFixeCaledonie = "+687 44.55.66";
	private static final String phoneFrMobileCaledonie = "+687 77.88.99";

	@Test
	public void testPhoneUs() {
		final var constraint = ConstraintPhone.ofEnum(ConstraintPhoneEnum.US, Optional.empty(), Optional.empty());

		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixe));

		Assertions.assertTrue(constraint.checkConstraint(phoneUs));
	}

	@Test
	public void testPhoneFrFixe() {
		final var constraint = ConstraintPhone.ofEnum(ConstraintPhoneEnum.FR_FIXE, Optional.empty(), Optional.empty());

		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixe));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixe2));

		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixeGuadeloupe));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixeStPier));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixeWallis));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixeCaledonie));

		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeKo));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeKo2));

		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobile));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobileGuadeloupe));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobileStPier));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobileWallis));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobileCaledonie));

		Assertions.assertFalse(constraint.checkConstraint(phoneUs));
	}

	@Test
	public void testPhoneFrMobile() {
		final var constraint = ConstraintPhone.ofEnum(ConstraintPhoneEnum.FR_MOBILE, Optional.empty(), Optional.empty());

		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixe));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixe2));

		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeGuadeloupe));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeStPier));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeWallis));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeCaledonie));

		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobile));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobile2));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobile3));

		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobileGuadeloupe));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobileGuadeloupeKo));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobileStPier));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobileWallis));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobileCaledonie));

		Assertions.assertFalse(constraint.checkConstraint(phoneUs));
	}

	@Test
	public void testPhoneCustomUs() {
		final var constraint = new ConstraintPhone("+1:10", Optional.empty(), Optional.empty());

		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixe));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeGuadeloupe));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeKo));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobile));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobileGuadeloupe));

		Assertions.assertTrue(constraint.checkConstraint(phoneUs));
	}

	@Test
	public void testPhoneCustomMultiple() {
		// US (+1), FR (+33) and mobile in Guadeloupe
		final var constraint = new ConstraintPhone("+1:10;+33:9;+590 69 [01]:6", Optional.empty(), Optional.empty());

		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixe2));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobile3));
		Assertions.assertTrue(constraint.checkConstraint(phoneUs));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobileGuadeloupe));

		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixe)); // no +33
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobile)); // no +33
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeGuadeloupe));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeKo));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobile));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrMobileGuadeloupeKo));
	}

	@Test
	public void testPhoneMultipleEnums() {
		final var constraint = ConstraintPhone.ofEnums(List.of(ConstraintPhoneEnum.FR_FIXE, ConstraintPhoneEnum.FR_MOBILE), Optional.empty(), Optional.empty());

		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixe));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixe2));

		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixeGuadeloupe));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixeStPier));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixeWallis));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrFixeCaledonie));

		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeKo));
		Assertions.assertFalse(constraint.checkConstraint(phoneFrFixeKo2));

		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobile));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobileGuadeloupe));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobileStPier));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobileWallis));
		Assertions.assertTrue(constraint.checkConstraint(phoneFrMobileCaledonie));

		Assertions.assertFalse(constraint.checkConstraint(phoneUs));
	}
}
