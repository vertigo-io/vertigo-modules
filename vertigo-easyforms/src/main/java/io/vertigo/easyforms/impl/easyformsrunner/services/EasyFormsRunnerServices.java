package io.vertigo.easyforms.impl.easyformsrunner.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.FormatterException;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.dao.EasyFormDAO;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.easyformsrunner.model.data.EasyFormsDataDescriptor;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldValidatorTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateField;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateFieldValidator;
import io.vertigo.easyforms.easyformsrunner.services.IEasyFormsRunnerServices;
import io.vertigo.vega.webservice.validation.UiMessageStack;
import io.vertigo.vega.webservice.validation.ValidationUserException;

@Transactional
public class EasyFormsRunnerServices implements Component, IEasyFormsRunnerServices {

	private static final String FORM_PREFIX = "form_";
	private static final String ERROR_CONTROL_FORM_MEASURE = "errorControlForm";

	@Inject
	private SmartTypeManager smartTypeManager;
	@Inject
	private AnalyticsManager analyticsManager;

	@Inject
	private EasyFormDAO easyFormDAO;

	@Override
	public EasyForm getEasyFormById(final UID<EasyForm> efoUid) {
		Assertion.check().isNotNull(efoUid);
		//---
		return easyFormDAO.get(efoUid);
	}

	@Override
	public void checkFormulaire(final Entity formOwner, final EasyFormsData formData, final EasyFormsTemplate formTempalte, final UiMessageStack uiMessageStack) {
		final Set<String> champsAutorises = formTempalte.getFields().stream().map(EasyFormsTemplateField::getCode).collect(Collectors.toSet());
		for (final String champFormulaire : formData.keySet()) {
			if (!champsAutorises.contains(champFormulaire)) {
				uiMessageStack.error("Champ non autorisé ", formOwner, FORM_PREFIX + champFormulaire);
			}
		}
		//---
		for (final EasyFormsTemplateField field : formTempalte.getFields()) {
			checkChampFormulaire(field, formData, formOwner, uiMessageStack);
		}

		//---
		if (uiMessageStack.hasErrors()) {
			throw new ValidationUserException();
		}
	}

	private void checkChampFormulaire(final EasyFormsTemplateField field, final EasyFormsData formData, final Entity formOwner, final UiMessageStack uiMessageStack) {
		final EasyFormsDataDescriptor fieldDescriptor = fieldToDataDescriptor(field);

		final var inputValue = formData.get(field.getCode());
		Object typedValue;
		try {
			typedValue = format(fieldDescriptor, inputValue);
		} catch (final FormatterException e) {
			uiMessageStack.error(e.getMessageText().getDisplay(), formOwner, FORM_PREFIX + field.getCode());
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(ERROR_CONTROL_FORM_MEASURE, 1)
					.setTag("controle", "Formatter")
					.setTag("smartType", fieldDescriptor.smartTypeDefinition().id().shortName())
					.setTag("champ", field.getLabel()));
			return;
		}

		final var errors = fieldDescriptor.validateAll(typedValue);
		for (final var error : errors) {
			uiMessageStack.error(error, formOwner, FORM_PREFIX + field.getCode());
		}
	}

	private Object format(final EasyFormsDataDescriptor fieldDescriptor, final Object inputValue) throws FormatterException {
		if (inputValue == null) {
			return null;
		}

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

	private static SmartTypeDefinition getSmartTypeByName(final String nomSmartType) {
		return Node.getNode().getDefinitionSpace().resolve(nomSmartType, SmartTypeDefinition.class);
	}

	private EasyFormsDataDescriptor fieldToDataDescriptor(final EasyFormsTemplateField field) {
		final var fieldType = EasyFormsFieldTypeDefinition.resolve(field.getFieldTypeName());
		final var smartTypeDefinition = getSmartTypeByName(fieldType.getSmartTypeName());
		final var cardinality = fieldType.isList() ? Cardinality.MANY : field.isMandatory() ? Cardinality.ONE : Cardinality.OPTIONAL_OR_NULLABLE;

		final var constraints = field.getValidators().stream()
				.map(EasyFormsRunnerServices::validatorToConstraint)
				.collect(Collectors.toList());

		return new EasyFormsDataDescriptor(field.getCode(), smartTypeDefinition, cardinality, constraints, fieldType.isList() && field.isMandatory() ? 1 : null, null);
	}

	private static Constraint validatorToConstraint(final EasyFormsTemplateFieldValidator validator) {
		final var validatorType = Node.getNode().getDefinitionSpace().resolve(validator.getName(), EasyFormsFieldValidatorTypeDefinition.class);
		// no use of validator parameters for now
		return validatorType.getConstraint();
	}

}
