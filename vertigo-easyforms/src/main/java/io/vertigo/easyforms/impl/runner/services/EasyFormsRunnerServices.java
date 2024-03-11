package io.vertigo.easyforms.impl.runner.services;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.FormatterException;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.dao.EasyFormDAO;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.data.EasyFormsDataDescriptor;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldValidatorTypeDefinition;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateField;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateFieldValidator;
import io.vertigo.easyforms.runner.services.IEasyFormsRunnerServices;
import io.vertigo.vega.webservice.validation.UiMessageStack;
import io.vertigo.vega.webservice.validation.ValidationUserException;

@Transactional
public class EasyFormsRunnerServices implements IEasyFormsRunnerServices {

	private static final String FORM_PREFIX = "form_";

	@Inject
	private EasyFormsRunnerManager easyFormsRunnerManager;

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
		final Set<String> allowedFields = formTempalte.getFields().stream().map(EasyFormsTemplateField::getCode).collect(Collectors.toSet());
		for (final String formField : formData.keySet()) {
			if (!allowedFields.contains(formField)) {
				uiMessageStack.error("Not allowed", formOwner, FORM_PREFIX + formField);
			}
		}
		//---
		for (final EasyFormsTemplateField field : formTempalte.getFields()) {
			formatAndCheckField(field, formData, formOwner, uiMessageStack);
		}

		//---
		if (uiMessageStack.hasErrors()) {
			throw new ValidationUserException();
		}
	}

	private void formatAndCheckField(final EasyFormsTemplateField field, final EasyFormsData formData, final Entity formOwner, final UiMessageStack uiMessageStack) {
		final EasyFormsDataDescriptor fieldDescriptor = fieldToDataDescriptor(field);

		// format field and replace value inside EasyFormsData (eg: Put last name in upper case)
		final var inputValue = formData.get(field.getCode());
		Object typedValue;
		try {
			typedValue = easyFormsRunnerManager.formatField(fieldDescriptor, inputValue);
		} catch (final FormatterException e) {
			uiMessageStack.error(e.getMessageText().getDisplay(), formOwner, FORM_PREFIX + field.getCode());
			return;
		}
		formData.put(field.getCode(), typedValue);

		// if formatter succeed, validate constraints
		final var errors = easyFormsRunnerManager.validateField(fieldDescriptor, typedValue, Map.of());
		for (final var error : errors) {
			uiMessageStack.error(error, formOwner, FORM_PREFIX + field.getCode());
		}
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

	private static SmartTypeDefinition getSmartTypeByName(final String nomSmartType) {
		return Node.getNode().getDefinitionSpace().resolve(nomSmartType, SmartTypeDefinition.class);
	}

	private static Constraint validatorToConstraint(final EasyFormsTemplateFieldValidator validator) {
		final var validatorType = Node.getNode().getDefinitionSpace().resolve(validator.getName(), EasyFormsFieldValidatorTypeDefinition.class);
		// no use of validator parameters for now
		return validatorType.getConstraint();
	}

}
