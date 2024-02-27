package io.vertigo.easyforms.easyformsrunner.model.data;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.data.definitions.DataDescriptor;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.ConstraintException;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.impl.easyformsrunner.Resources;

public class EasyFormsDataDescriptor extends DataDescriptor {

	private final List<Constraint> businessConstraints;
	private final Integer minListSize; // When cardinality MANY, provide minimum number of elements
	private final Integer maxListSize; // When cardinality MANY, provide maximum number of elements

	public EasyFormsDataDescriptor(final String name, final SmartTypeDefinition smartTypeDefinition, final Cardinality cardinality, final List<Constraint> businessConstraints,
			final Integer minListSize, final Integer maxListSize) {
		super(name, smartTypeDefinition, cardinality);
		this.businessConstraints = businessConstraints;
		this.minListSize = minListSize;
		this.maxListSize = maxListSize;
	}

	/**
	 * Validates the value (the value type is also automatically checked before) with business constraints.
	 *
	 * @param value the value
	 * @throws ConstraintException
	 */
	public final List<String> validateAll(final Object value) {
		try {
			validate(value);
		} catch (final ConstraintException e) {
			return List.of(e.getMessageText().getDisplay());
			/*
			 // responsability of valiation manager ?
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(ERROR_CONTROL_FORM_MEASURE, 1)
					.setTag("controle", "Constraints")
					.setTag("smartType", smartTypeDefinition.id().shortName())
					.setTag("champ", field.getLabel()));
					*/
		}

		// from SmartTypeManagerImpl, should be refactored (eg in a ValidatorManager ?)
		switch (cardinality()) {
			case MANY:
				if (value instanceof final List valueList) {
					final var errorList = new ArrayList<String>();
					if (minListSize != null && minListSize.intValue() > valueList.size()) {
						errorList.add(LocaleMessageText.of(Resources.EfMinListSize, minListSize, minListSize.intValue() > 1 ? "s" : "").getDisplay());
					}
					if (maxListSize != null && maxListSize.intValue() < valueList.size()) {
						errorList.add(LocaleMessageText.of(Resources.EfMaxListSize, maxListSize, maxListSize.intValue() > 1 ? "s" : "").getDisplay());
					}
					for (final Object element : valueList) {
						errorList.addAll(checkConstraints(element));
					}
					return errorList;
				} else {
					throw new ClassCastException("Value " + value + " must be a list");
				}
			case ONE:
			case OPTIONAL_OR_NULLABLE:
				return checkConstraints(value);
			default:
				throw new UnsupportedOperationException();
		}
	}

	private List<String> checkConstraints(final Object value) {
		final var errorList = new ArrayList<String>();
		for (final Constraint constraint : businessConstraints) {
			if (!constraint.checkConstraint(value)) {
				errorList.add(constraint.getErrorMessage().getDisplay());
			}
		}
		return errorList;
	}

}
