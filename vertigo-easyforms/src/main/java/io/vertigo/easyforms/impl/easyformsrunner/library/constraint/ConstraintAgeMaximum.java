package io.vertigo.easyforms.impl.easyformsrunner.library.constraint;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import io.vertigo.basics.constraint.ConstraintUtil;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.datamodel.smarttype.definitions.Property;

/**
 * Constraint of maximal age. Actual age must be lower or equals to configured age.
 *
 * @author skerdudou
 */
public final class ConstraintAgeMaximum implements Constraint<Integer, LocalDate> {
	private final Integer age;
	private final LocaleMessageText errorMessage;

	/**
	 * Constructor.
	 *
	 * @param args the max age
	 */
	public ConstraintAgeMaximum(final String args, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		Assertion.check()
				.isNotBlank(args, "Age must be passend in parameter.");
		//-----
		age = Integer.valueOf(args);

		errorMessage = ConstraintUtil.resolveMessage(overrideMessageOpt, overrideResourceMessageOpt,
				() -> LocaleMessageText.of("L'age doit être au maximum de {0} ans", age));
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkConstraint(final LocalDate value) {
		final var actualAge = Period.between(value, LocalDate.now()).getYears();
		return value == null || actualAge <= age;

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
	public Integer getPropertyValue() {
		return age;
	}
}
