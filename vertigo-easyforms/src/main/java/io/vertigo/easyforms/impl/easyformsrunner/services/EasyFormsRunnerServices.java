package io.vertigo.easyforms.impl.easyformsrunner.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.smarttype.SmarttypeResources;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.ConstraintException;
import io.vertigo.datamodel.smarttype.definitions.FormatterException;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.dao.EasyFormDAO;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldValidatorType;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateField;
import io.vertigo.vega.webservice.validation.UiMessageStack;
import io.vertigo.vega.webservice.validation.ValidationUserException;

@Transactional
public class EasyFormsRunnerServices implements Component {

	private static final String FORM_PREFIX = "form_";
	private static final String ERROR_CONTROL_FORM_MEASURE = "errorControlForm";

	@Inject
	private SmartTypeManager smartTypeManager;
	@Inject
	private AnalyticsManager analyticsManager;

	@Inject
	private EasyFormDAO easyFormDAO;

	public EasyForm getEasyFormById(final UID<EasyForm> efoUid) {
		Assertion.check().isNotNull(efoUid);
		//---
		return easyFormDAO.get(efoUid);
	}

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
		final var fieldType = EasyFormsFieldType.resolve(field.getFieldTypeName());
		final var smartTypeDefinition = getSmartTypeByName(fieldType.getSmartTypeName());
		final var inputValue = formData.get(field.getCode());
		if (inputValue == null || (inputValue instanceof final String inputString && inputString.isBlank())) {
			// when null, the only check is for mandatory
			if (field.isMandatory()) {
				uiMessageStack.error(LocaleMessageText.of(SmarttypeResources.SMARTTYPE_MISSING_VALUE).getDisplay(), formOwner, FORM_PREFIX + field.getCode());
				analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
						.incMeasure(ERROR_CONTROL_FORM_MEASURE, 1)
						.setTag("controle", "Obligatoire")
						.setTag("champ", field.getLabel()));
			}
		} else {
			if (inputValue instanceof final List<?> inputCollection) {
				final List<Object> resolvedList = new ArrayList<>();
				for (final var elem : inputCollection) {
					checkType(field, formOwner, uiMessageStack, smartTypeDefinition, elem)
							.ifPresent(resolvedList::add);
				}
				formData.put(field.getCode(), resolvedList);
			} else {
				checkType(field, formOwner, uiMessageStack, smartTypeDefinition, inputValue)
						.ifPresent(v -> formData.put(field.getCode(), v));
			}
		}
	}

	private Optional<Object> checkType(final EasyFormsTemplateField field, final Entity formOwner, final UiMessageStack uiMessageStack,
			final SmartTypeDefinition smartTypeDefinition, final Object inputValue) {
		Object typedValue = null;
		try {
			typedValue = smartTypeManager.stringToValue(smartTypeDefinition, inputValue.toString());

			smartTypeManager.validate(smartTypeDefinition, Cardinality.OPTIONAL_OR_NULLABLE, typedValue);
			checkFieldValidators(field, typedValue, formOwner, uiMessageStack);
		} catch (final FormatterException e) {
			uiMessageStack.error(e.getMessageText().getDisplay(), formOwner, FORM_PREFIX + field.getCode());
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(ERROR_CONTROL_FORM_MEASURE, 1)
					.setTag("controle", "Formatter")
					.setTag("smartType", smartTypeDefinition.id().shortName())
					.setTag("champ", field.getLabel()));
		} catch (final ConstraintException e) {
			uiMessageStack.error(e.getMessageText().getDisplay(), formOwner, FORM_PREFIX + field.getCode());
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(ERROR_CONTROL_FORM_MEASURE, 1)
					.setTag("controle", "Constraints")
					.setTag("smartType", smartTypeDefinition.id().shortName())
					.setTag("champ", field.getLabel()));
		}
		return Optional.ofNullable(typedValue);
	}

	private void checkFieldValidators(final EasyFormsTemplateField field, final Object typedValue, final Entity formOwner, final UiMessageStack uiMessageStack) {
		field.getValidators().forEach(validator -> {
			final var validatorType = Node.getNode().getDefinitionSpace().resolve(validator.getName(), EasyFormsFieldValidatorType.class);
			final Constraint<?, Object> constraint = validatorType.getConstraint();
			if (!constraint.checkConstraint(typedValue)) {
				uiMessageStack.error(constraint.getErrorMessage().getDisplay(), formOwner, FORM_PREFIX + field.getCode());
			}
		});
	}

	private static SmartTypeDefinition getSmartTypeByName(final String nomSmartType) {
		return Node.getNode().getDefinitionSpace().resolve(nomSmartType, SmartTypeDefinition.class);
	}

}
