package io.vertigo.easyforms.impl.runner.library.constraint;

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
 * Contrainte sur la valeur maximale d'une date.
 * arguments = valeur maximale.
 *
 * @author npiedeloup
 */
public final class ConstraintLocalDateMaximum implements Constraint<Double, LocalDate> {
	private static final String DATE_PATTERN = "dd/MM/yyyy";
	private final String maxExpression;
	private final LocalDate maxStableValue;
	private final LocaleMessageText errorMessage;

	/**
	 * Constructor.
	 *
	 * @param args the maximum value
	 */
	public ConstraintLocalDateMaximum(final String args, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		Assertion.check()
				.isNotBlank(args, "Vous devez préciser la valeur maximum comme argument de ConstraintLocalDateMaximum");
		//-----
		maxExpression = args;
		//check format :

		if (maxExpression.contains("now")) {
			Assertion.check().isNotNull(DateUtil.parseToLocalDate(maxExpression, DATE_PATTERN), "Pattern de date non reconnu {0}", maxExpression);
			maxStableValue = null;
			errorMessage = ConstraintUtil.resolveMessage(overrideMessageOpt, overrideResourceMessageOpt, EfConstraintResources.EfConDateMax,
					maxExpression);
		} else {
			maxStableValue = DateUtil.parseToLocalDate(maxExpression, DATE_PATTERN);
			errorMessage = ConstraintUtil.resolveMessage(overrideMessageOpt, overrideResourceMessageOpt, EfConstraintResources.EfConDateMax,
					DateTimeFormatter.ofPattern(DATE_PATTERN).format(maxStableValue));
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkConstraint(final LocalDate value) {
		final var currentMax = maxStableValue != null ? maxStableValue : DateUtil.parseToLocalDate(maxExpression, DATE_PATTERN);
		return value == null
				|| !value.isAfter(currentMax); //value <= currentMax

	}

	/** {@inheritDoc} */
	@Override
	public LocaleMessageText getErrorMessage() {
		return errorMessage;
	}

	/** {@inheritDoc} */
	@Override
	public Property getProperty() {
		return DtProperty.MAX_VALUE;
	}

	/** {@inheritDoc} */
	@Override
	public Double getPropertyValue() {
		return (double) DateUtil.parseToLocalDate(maxExpression, DATE_PATTERN).toEpochDay();
	}
}
