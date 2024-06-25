package io.vertigo.easyforms.impl.runner.pack.constraint;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import io.vertigo.basics.constraint.ConstraintUtil;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.Property;

/**
 * Constraint of minimum age. Actual age must be higher or equals to than configured age.
 *
 * @author skerdudou
 */
public final class ConstraintAgeMinimum implements Constraint<Integer, LocalDate> {
	private final Integer age;
	private final LocaleMessageText errorMessage;

	/**
	 * Constructor.
	 *
	 * @param args the min age
	 */
	public ConstraintAgeMinimum(final String args, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		Assertion.check()
				.isNotBlank(args, "Age must be passend in parameter.");
		//-----
		age = Integer.valueOf(args);

		errorMessage = ConstraintUtil.resolveMessage(overrideMessageOpt, overrideResourceMessageOpt, EfConstraintResources.EfConAgeMin, age);
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkConstraint(final LocalDate value) {
		return value == null || actualAge(value) >= age;
	}

	private static int actualAge(final LocalDate birthdate) {
		return Period.between(birthdate, LocalDate.now()).getYears();
	}

	/** {@inheritDoc} */
	@Override
	public LocaleMessageText getErrorMessage() {
		return errorMessage;
	}

	/** {@inheritDoc} */
	@Override
	public Property getProperty() {
		return new Property<>("minAge", Integer.class);
	}

	/** {@inheritDoc} */
	@Override
	public Integer getPropertyValue() {
		return age;
	}
}