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
package io.vertigo.easyforms.runner.pack.constraint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import io.vertigo.basics.constraint.ConstraintUtil;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.util.DateUtil;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.datamodel.smarttype.definitions.Property;

/**
 * Contrainte sur la valeur minimale d'une date.
 * arguments = valeur minimale.
 *
 * @author npiedeloup
 */
public final class ConstraintLocalDateMinimum implements Constraint<Double, LocalDate> {
	private static final String DATE_PATTERN = "dd/MM/yyyy";
	private final String minExpression;
	private final LocalDate minStableValue;
	private final LocaleMessageText errorMessage;

	/**
	 * Constructor.
	 *
	 * @param args the minimum value
	 */
	public ConstraintLocalDateMinimum(final String args, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		Assertion.check()
				.isNotBlank(args, "Vous devez préciser la valeur minimum comme argument de ConstraintNumberMinimum");
		//-----
		minExpression = args;
		//check format :

		if (minExpression.contains("now")) {
			Assertion.check().isNotNull(DateUtil.parseToLocalDate(minExpression, DATE_PATTERN), "Pattern de date non reconnu {0}", minExpression);
			minStableValue = null;
			errorMessage = ConstraintUtil.resolveMessage(overrideMessageOpt, overrideResourceMessageOpt, EfConstraintResources.EfConDateMin,
					minExpression);
		} else {
			minStableValue = DateUtil.parseToLocalDate(minExpression, DATE_PATTERN);
			errorMessage = ConstraintUtil.resolveMessage(overrideMessageOpt, overrideResourceMessageOpt, EfConstraintResources.EfConDateMin,
					DateTimeFormatter.ofPattern(DATE_PATTERN).format(minStableValue));
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkConstraint(final LocalDate value) {
		final var currentMin = minStableValue != null ? minStableValue : DateUtil.parseToLocalDate(minExpression, DATE_PATTERN);
		return value == null
				|| !value.isBefore(currentMin); //value >= currentMin

	}

	/** {@inheritDoc} */
	@Override
	public LocaleMessageText getErrorMessage() {
		return errorMessage;
	}

	/** {@inheritDoc} */
	@Override
	public Property getProperty() {
		return DtProperty.MIN_VALUE;
	}

	/** {@inheritDoc} */
	@Override
	public Double getPropertyValue() {
		return (double) DateUtil.parseToLocalDate(minExpression, DATE_PATTERN).toEpochDay();
	}
}
