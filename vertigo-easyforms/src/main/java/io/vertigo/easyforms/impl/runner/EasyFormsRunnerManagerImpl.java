package io.vertigo.easyforms.impl.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.core.locale.LocaleManager;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.ConstraintException;
import io.vertigo.datamodel.smarttype.definitions.FormatterException;
import io.vertigo.easyforms.impl.runner.library.EfLibraryResources;
import io.vertigo.easyforms.impl.runner.library.constraint.EfConstraintResources;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.data.EasyFormsDataDescriptor;

public final class EasyFormsRunnerManagerImpl implements EasyFormsRunnerManager, Activeable {

	private static final String FORMAT_ERROR_MEASURE = "formatError";
	private static final String VALIDATION_ERROR_MEASURE = "validationError";

	private final LocaleManager localeManager;
	private final AnalyticsManager analyticsManager;
	private final SmartTypeManager smartTypeManager;

	@Inject
	public EasyFormsRunnerManagerImpl(final LocaleManager localeManager, final AnalyticsManager analyticsManager, final SmartTypeManager smartTypeManager) {
		this.localeManager = localeManager;
		this.analyticsManager = analyticsManager;
		this.smartTypeManager = smartTypeManager;
	}

	@Override
	public void start() {
		localeManager.add("io.vertigo.easyforms.runner.Resources", io.vertigo.easyforms.impl.runner.Resources.values());
		localeManager.add("io.vertigo.easyforms.runner.library.EfLibraryResources", EfLibraryResources.values());
		localeManager.add("io.vertigo.easyforms.runner.library.EfConstraintResources", EfConstraintResources.values());

		localeManager.add("io.vertigo.easyforms.domain.DtResources", io.vertigo.easyforms.domain.DtResources.values());
	}

	@Override
	public void stop() {
		// Nothing
	}

	// *****
	// *** Methods below could be part of a global (non easy form specific) validation manager (or dataManager ?)
	// *****

	@Override
	public Object formatField(final EasyFormsDataDescriptor fieldDescriptor, final Object inputValue) throws FormatterException {
		if (inputValue == null) {
			return null;
		}

		try {
			return doFormatField(fieldDescriptor, inputValue);
		} catch (final FormatterException e) {
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(FORMAT_ERROR_MEASURE, 1));

			throw e;
		}
	}

	private Object doFormatField(final EasyFormsDataDescriptor fieldDescriptor, final Object inputValue) throws FormatterException {
		switch (fieldDescriptor.cardinality()) {
			case MANY:
				if (!(inputValue instanceof List)) {
					throw new ClassCastException("Value " + inputValue + " must be a list");
				}
				final var inputCollection = (List) inputValue;
				final List<Object> resolvedList = new ArrayList<>(inputCollection.size());
				for (final var elem : inputCollection) {
					resolvedList.add(smartTypeManager.stringToValue(fieldDescriptor.smartTypeDefinition(), elem.toString()));
				}
				return resolvedList;
			case ONE:
			case OPTIONAL_OR_NULLABLE:
				return smartTypeManager.stringToValue(fieldDescriptor.smartTypeDefinition(), inputValue.toString());
			default:
				throw new UnsupportedOperationException();
		}
	}

	/**
	 * Validates the value (the value type is also automatically checked before) with business constraints.
	 *
	 * @param fieldDescriptor field description
	 * @param value the value
	 */
	@Override
	public final List<String> validateField(final EasyFormsDataDescriptor fieldDescriptor, final Object value, final Map<String, Object> context) {
		// validate data nature (structure)
		try {
			fieldDescriptor.validate(value);
		} catch (final ConstraintException e) {
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(VALIDATION_ERROR_MEASURE, 1));

			return List.of(e.getMessageText().getDisplay());
		}

		// If ok, validate data intention (business rules)
		final List<String> errors = doValidateField(fieldDescriptor, value, context);
		if (!errors.isEmpty()) {
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(VALIDATION_ERROR_MEASURE, 1));
		}
		return errors;
	}

	private List<String> doValidateField(final EasyFormsDataDescriptor fieldDescriptor, final Object value, final Map<String, Object> context) {
		switch (fieldDescriptor.cardinality()) {
			case MANY:
				if (value instanceof final List valueList) {
					final var errorList = new ArrayList<String>();
					if (fieldDescriptor.getMinListSize() != null && fieldDescriptor.getMinListSize().intValue() > valueList.size()) {
						errorList.add(LocaleMessageText.of(Resources.EfMinListSize, fieldDescriptor.getMinListSize(), fieldDescriptor.getMinListSize().intValue() > 1 ? "s" : "").getDisplay());
					} else if (fieldDescriptor.getMaxListSize() != null && fieldDescriptor.getMaxListSize().intValue() < valueList.size()) {
						errorList.add(LocaleMessageText.of(Resources.EfMaxListSize, fieldDescriptor.getMaxListSize(), fieldDescriptor.getMaxListSize().intValue() > 1 ? "s" : "").getDisplay());
					}
					for (final Object element : valueList) {
						errorList.addAll(doCheckConstraints(fieldDescriptor, element, context));
					}
					return errorList;
				} else {
					throw new ClassCastException("Value " + value + " must be a list");
				}
			case ONE:
			case OPTIONAL_OR_NULLABLE:
				return doCheckConstraints(fieldDescriptor, value, context);
			default:
				throw new UnsupportedOperationException();
		}
	}

	private List<String> doCheckConstraints(final EasyFormsDataDescriptor fieldDescriptor, final Object value, final Map<String, Object> context) {
		final var errorList = new ArrayList<String>();
		for (final Constraint constraint : fieldDescriptor.getBusinessConstraints()) {
			if (!constraint.checkConstraint(value)) {
				errorList.add(constraint.getErrorMessage().getDisplay());
			}
		}
		return errorList;
	}

}
