package io.vertigo.easyforms.impl.easyformsdesigner.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.util.VCollectors;
import io.vertigo.easyforms.dao.EasyFormDAO;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsFieldUiFields;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldUi;
import io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi;
import io.vertigo.easyforms.domain.EasyFormsTemplateFieldValidatorUi;
import io.vertigo.easyforms.easyformsdesigner.services.IEasyFormsDesignerServices;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldValidatorTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateField;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateFieldValidator;
import io.vertigo.easyforms.easyformsrunner.model.ui.EasyFormsTemplateFieldValidatorUiList;
import io.vertigo.vega.webservice.validation.UiMessageStack;
import io.vertigo.vega.webservice.validation.ValidationUserException;

@Transactional
public class EasyFormsDesignerServices implements IEasyFormsDesignerServices {

	@Inject
	private EasyFormDAO easyFormDAO;

	@Override
	public DtList<EasyFormsFieldTypeUi> getFieldTypeUiList() {
		return Node.getNode().getDefinitionSpace().getAll(EasyFormsFieldTypeDefinition.class)
				.stream()
				.filter(d -> d.getCategory() != null)
				.map(fieldType -> {
					final var fieldTypeUi = new EasyFormsFieldTypeUi();
					fieldTypeUi.setName(fieldType.id().fullName());
					fieldTypeUi.setCategory(fieldType.getCategory());
					fieldTypeUi.setUiComponentName(fieldType.getUiComponentName());
					fieldTypeUi.setLabel(fieldType.getLabel());
					fieldTypeUi.setUiParameters(fieldType.getUiParameters());
					fieldTypeUi.setParamTemplate(fieldType.getParamTemplate());
					return fieldTypeUi;
				})
				.collect(VCollectors.toDtList(EasyFormsFieldTypeUi.class));
	}

	@Override
	public DtList<EasyFormsFieldValidatorTypeUi> getFieldValidatorTypeUiList() {
		return Node.getNode().getDefinitionSpace().getAll(EasyFormsFieldValidatorTypeDefinition.class)
				.stream()
				.sorted(Comparator.comparingInt(EasyFormsFieldValidatorTypeDefinition::getPriority))
				.map(fieldValidator -> {
					final var fieldValidatorUi = new EasyFormsFieldValidatorTypeUi();

					fieldValidatorUi.setName(fieldValidator.id().fullName());
					fieldValidatorUi.setLabel(fieldValidator.getLabel());
					fieldValidatorUi.setDescription(fieldValidator.getDescription());
					fieldValidatorUi.setFieldTypes(fieldValidator.getFieldTypes().stream().map(EasyFormsFieldTypeDefinition::getName).toList());
					return fieldValidatorUi;
				})
				.collect(VCollectors.toDtList(EasyFormsFieldValidatorTypeUi.class));
	}

	@Override
	public DtList<EasyFormsFieldUi> getFieldUiListByEasyForm(final EasyForm easyForm) {
		return easyForm.getTemplate().getFields().stream()
				.map(field -> {
					final var fieldType = EasyFormsFieldTypeDefinition.resolve(field.getFieldTypeName());
					final var fieldUi = new EasyFormsFieldUi();
					fieldUi.setFieldCode(field.getCode());
					fieldUi.setFieldType(fieldType.id().fullName());
					fieldUi.setFieldTypeLabel(fieldType.getLabel());
					fieldUi.setLabel(field.getLabel());
					fieldUi.setTooltip(field.getTooltip());
					fieldUi.setIsDefault(field.isDefault());
					fieldUi.setParameters(field.getParameters());
					fieldUi.setIsMandatory(field.isMandatory());
					fieldUi.setFieldValidators(validatorToValidatorUi(field.getValidators()));

					return fieldUi;
				})
				.collect(VCollectors.toDtList(EasyFormsFieldUi.class));
	}

	private EasyFormsTemplateFieldValidatorUiList validatorToValidatorUi(final List<EasyFormsTemplateFieldValidator> validators) {
		if (validators == null) {
			return new EasyFormsTemplateFieldValidatorUiList();
		}

		return new EasyFormsTemplateFieldValidatorUiList(
				validators.stream()
						.map(validator -> {
							final var validatorUi = new EasyFormsTemplateFieldValidatorUi();
							validatorUi.setValidatorTypeName(validator.getName());
							validatorUi.setParameters(validator.getParameters());

							final EasyFormsFieldValidatorTypeDefinition validatorType = Node.getNode().getDefinitionSpace().resolve(validator.getName(), EasyFormsFieldValidatorTypeDefinition.class);
							validatorUi.setLabel(validatorType.getLabel());
							validatorUi.setParameterizedLabel(validatorType.getParameterizedLabel(validator.getParameters()));
							validatorUi.setDescription(validatorType.getDescription());

							return validatorUi;
						})
						.toList());
	}

	@Override
	public void checkUpdateField(final DtList<EasyFormsFieldUi> fields, final Integer editIndex, final EasyFormsFieldUi fieldEdit, final UiMessageStack uiMessageStack) {
		for (int i = 0; i < fields.size(); i++) {
			if (i != editIndex && fields.get(i).getFieldCode().equals(fieldEdit.getFieldCode())) {
				//si le code n'est pas unique ce n'est pas bon.
				throw new ValidationUserException(LocaleMessageText.of("Le code du champ doit être unique dans le formulaire."), // TODO i18n
						fieldEdit, EasyFormsFieldUiFields.fieldCode);
			}
		}

		/*
		 * TODO: Example of multi-field parameter constraint that should be parametrable by project (via manager ?)
				if (!editField.getIsDefault() && editField.getIsDisplay()) {
					//on retire tous les autres qui auraient isDisplay (sauf les defaults)
					for (var champIndex = 0; champIndex < champs.size(); ++champIndex) {
						final var champUi = champs.get(champIndex);
						if (champIndex != editIndex && champUi.getIsDisplay() && !champUi.getIsDefault()) {
							champUi.setIsDisplay(false);
							uiMessageStack.warning("Il ne peut y avoir qu'un seul champ complémentaire affiché dans le formulaire.\nLe champ \"" + champUi.getFieldCode() + "\" n'est plus inclus");
						}
					}
				}
				*/
	}

	@Override
	public Long saveNewForm(final EasyForm easyForm, final DtList<EasyFormsFieldUi> fields) {
		final List<EasyFormsTemplateField> templateFields = new ArrayList<>();
		for (final EasyFormsFieldUi fieldUi : fields) {
			final EasyFormsFieldTypeDefinition fieldType = EasyFormsFieldTypeDefinition.resolve(fieldUi.getFieldType());
			Assertion.check().isNotNull(fieldType, "Field type can't be null");

			templateFields.add(new EasyFormsTemplateField(fieldUi.getFieldCode(), fieldType)
					.withLabel(fieldUi.getLabel())
					.withTooltip(fieldUi.getTooltip())
					.withDefault(Boolean.TRUE.equals(fieldUi.getIsDefault()))
					.withMandatory(Boolean.TRUE.equals(fieldUi.getIsMandatory()))
					.withParameters(fieldUi.getParameters())
					.withValidators(validatorUiToValidator(fieldUi.getFieldValidators())));
		}
		easyForm.setTemplate(new EasyFormsTemplate(templateFields));
		easyFormDAO.save(easyForm);
		return easyForm.getEfoId();
	}

	private List<EasyFormsTemplateFieldValidator> validatorUiToValidator(final List<EasyFormsTemplateFieldValidatorUi> validatorsUi) {
		if (validatorsUi == null) {
			return List.of();
		}

		return validatorsUi.stream()
				.map(validatorUi -> new EasyFormsTemplateFieldValidator(validatorUi.getValidatorTypeName())
						.withParameters(validatorUi.getParameters()))
				.toList();
	}

	@Override
	public EasyForm createEasyForm(final EasyForm easyForm) {
		Assertion.check().isNotNull(easyForm);
		//---
		return easyFormDAO.create(easyForm);
	}

}
