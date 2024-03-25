package io.vertigo.easyforms.impl.runner.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.node.Node;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.data.model.DataObject;
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
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateFieldValidator;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateSection;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemBlock;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
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
	public void formatAndCheckFormulaire(final DataObject formOwner, final EasyFormsData formData, final EasyFormsTemplate formTempalte, final UiMessageStack uiMessageStack) {
		Assertion.check()
				.isFalse(formTempalte.getSections() == null || formTempalte.getSections().isEmpty(), "No form")
				.isFalse(StringUtil.isBlank(formTempalte.getSections().get(0).getCode()) && formTempalte.getSections().size() > 1, "If default section, it must be the only one");

		final EasyFormsData formattedFormData = new EasyFormsData();
		for (final var section : formTempalte.getSections()) {
			// test section condition, else continue;

			final EasyFormsData formDataSection;
			final EasyFormsData formattedFormDataSection;
			if (section.getCode() == null) { // no section
				formDataSection = formData;
				formattedFormDataSection = formattedFormData;
			} else {
				formDataSection = new EasyFormsData((Map<String, Object>) formData.get(section.getCode()));
				formattedFormDataSection = new EasyFormsData();
				formattedFormData.put(section.getCode(), formattedFormDataSection);
			}

			for (final var elem : section.getItems()) {
				if (elem instanceof final EasyFormsTemplateItemBlock block) {
					// TODO : test block condition
					for (final var elem2 : block.getItems()) {
						if (elem2 instanceof final EasyFormsTemplateItemField field) {
							final var formattedValue = formatAndCheckField(field, formDataSection, formOwner, uiMessageStack);
							formattedFormDataSection.put(field.getCode(), formattedValue);
						}
					}
					continue;
				} else if (elem instanceof final EasyFormsTemplateItemField field) {
					final var formattedValue = formatAndCheckField(field, formDataSection, formOwner, uiMessageStack);
					formattedFormDataSection.put(field.getCode(), formattedValue);
				}
			}
		}

		//---
		if (uiMessageStack.hasErrors()) {
			throw new ValidationUserException();
		}
		// validation succeed, replace raw data with clean data
		formData.clear();
		formData.putAll(formattedFormData);
	}

	private Object formatAndCheckField(final EasyFormsTemplateItemField field, final EasyFormsData formData, final DataObject formOwner, final UiMessageStack uiMessageStack) {
		final EasyFormsDataDescriptor fieldDescriptor = fieldToDataDescriptor(field);

		// format field (eg: Put last name in upper case)
		final var inputValue = formData.get(field.getCode());
		Object typedValue;
		try {
			typedValue = easyFormsRunnerManager.formatField(fieldDescriptor, inputValue);
		} catch (final FormatterException e) {
			uiMessageStack.error(e.getMessageText().getDisplay(), formOwner, FORM_PREFIX + field.getCode());
			return inputValue;
		}

		// if formatter succeed, validate constraints
		final var errors = easyFormsRunnerManager.validateField(fieldDescriptor, typedValue, Map.of());
		for (final var error : errors) {
			uiMessageStack.error(error, formOwner, FORM_PREFIX + field.getCode());
		}

		return typedValue;
	}

	private EasyFormsDataDescriptor fieldToDataDescriptor(final EasyFormsTemplateItemField field) {
		final var fieldType = EasyFormsFieldTypeDefinition.resolve(field.getFieldTypeName());
		final var smartTypeDefinition = getSmartTypeByName(fieldType.getSmartTypeName());
		final var cardinality = fieldType.isList() ? Cardinality.MANY : field.isMandatory() ? Cardinality.ONE : Cardinality.OPTIONAL_OR_NULLABLE;

		final List<Constraint> constraints;
		if (field.getValidators() == null) {
			constraints = List.of(); // defensive code as JSON could be missing this attribute
		} else {
			constraints = field.getValidators().stream()
					.map(EasyFormsRunnerServices::validatorToConstraint)
					.collect(Collectors.toList());
		}

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

	@Override
	public List<EasyFormsTemplateItemField> getAllFieldsFromSection(final EasyFormsTemplateSection section) {
		final List<EasyFormsTemplateItemField> list = new ArrayList<>();
		for (final var item : section.getItems()) {
			addFieldsForItem(list, item);
		}
		return list;
	}

	private void addFieldsForItem(final List<EasyFormsTemplateItemField> list, final AbstractEasyFormsTemplateItem item) {
		if (item instanceof final EasyFormsTemplateItemField field) {
			list.add(field);
		} else if (item instanceof final EasyFormsTemplateItemBlock block) {
			for (final var blockElem : block.getItems()) {
				addFieldsForItem(list, blockElem);
			}
		}
	}

	@Override
	public EasyFormsData getDefaultDataValues(final EasyFormsTemplate easyFormsTemplate) {
		final var templateDefaultData = new EasyFormsData();

		for (final var section : easyFormsTemplate.getSections()) {
			final Map<String, Object> sectionData;
			if (easyFormsTemplate.useSections()) {
				sectionData = new HashMap<>();
				templateDefaultData.put(section.getCode(), sectionData);
			} else {
				sectionData = templateDefaultData;
			}

			for (final var item : section.getItems()) {
				if (item instanceof final EasyFormsTemplateItemField field) {
					final var paramFieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
					if (paramFieldTypeDefinition.getDefaultValue() != null) {
						sectionData.put(field.getCode(), paramFieldTypeDefinition.getDefaultValue());
					}
				}
			}
		}
		return templateDefaultData;
	}

}
