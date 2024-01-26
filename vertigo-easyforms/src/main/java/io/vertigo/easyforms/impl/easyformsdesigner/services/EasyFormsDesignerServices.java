package io.vertigo.easyforms.impl.easyformsdesigner.services;

import java.util.Comparator;

import javax.inject.Inject;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.structure.util.VCollectors;
import io.vertigo.easyforms.dao.EasyFormDAO;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsFieldUiFields;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldUi;
import io.vertigo.easyforms.domain.EasyFormsFieldValidatorUi;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldValidator;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplateBuilder;
import io.vertigo.vega.webservice.validation.UiMessageStack;
import io.vertigo.vega.webservice.validation.ValidationUserException;

@Transactional
public class EasyFormsDesignerServices implements Component {
	public static final String RESOURCES_PREFIX = "EF_FORM_CONTROL_";
	public static final String RESOURCES_SUFFIX_LABEL = "_LABEL";
	public static final String RESOURCES_SUFFIX_DESCRIPTION = "_DESCRIPTION";
	public static final String RESOURCES_SUFFIX_ERROR = "_ERROR";

	@Inject
	private EasyFormDAO easyFormDAO;

	public EasyForm getEasyFormById(final UID<EasyForm> efoUid) {
		Assertion.check().isNotNull(efoUid);
		//---
		return easyFormDAO.get(efoUid);
	}

	public DtList<EasyFormsFieldTypeUi> getFieldTypeUiList() {
		return Node.getNode().getDefinitionSpace().getAll(EasyFormsFieldType.class)
				.stream()
				.map(fieldType -> {
					final var fieldTypeUi = new EasyFormsFieldTypeUi();
					fieldTypeUi.setName(fieldType.id().fullName());
					fieldTypeUi.setLabel(fieldType.getLabel());
					return fieldTypeUi;
				})
				.collect(VCollectors.toDtList(EasyFormsFieldTypeUi.class));
	}

	public DtList<EasyFormsFieldValidatorUi> getFieldValidatorUiList() {
		return Node.getNode().getDefinitionSpace().getAll(EasyFormsFieldValidator.class)
				.stream()
				.sorted(Comparator.comparingInt(EasyFormsFieldValidator::getPriority))
				.map(fieldValidator -> {
					final var fieldValidatorUi = new EasyFormsFieldValidatorUi();
					final var localName = fieldValidator.id().shortName();
					final var resourcePrefix = RESOURCES_PREFIX + StringUtil.camelToConstCase(localName);

					fieldValidatorUi.setCode(fieldValidator.id().fullName());
					fieldValidatorUi.setLabel(LocaleMessageText.of(() -> resourcePrefix + RESOURCES_SUFFIX_LABEL).getDisplay());
					fieldValidatorUi.setDescription(LocaleMessageText.of(() -> resourcePrefix + RESOURCES_SUFFIX_DESCRIPTION).getDisplay());
					fieldValidatorUi.setFieldTypes(fieldValidator.getFieldTypes().stream().map(EasyFormsFieldType::getName).toList());
					return fieldValidatorUi;
				})
				.collect(VCollectors.toDtList(EasyFormsFieldValidatorUi.class));
	}

	public DtList<EasyFormsFieldUi> getFieldUiListByEasyFormId(final UID<EasyForm> efoUid) {
		final var easyForm = getEasyFormById(efoUid);
		return easyForm.getTemplate().getFields().stream()
				.map(field -> {
					final var fieldType = EasyFormsFieldType.resolve(field.getFieldTypeName());
					final var fieldUi = new EasyFormsFieldUi();
					fieldUi.setFieldCode(field.getCode());
					fieldUi.setFieldType(fieldType.id().fullName());
					fieldUi.setFieldTypeLabel(fieldType.getLabel());
					fieldUi.setLabel(field.getLabel());
					fieldUi.setTooltip(field.getTooltip());
					fieldUi.setIsDefault(field.isDefault());
					fieldUi.setIsMandatory(field.isMandatory());
					// TODO fieldUi.setFieldValidators(field.getFieldValidators() != null ? field.getFieldValidators() : Collections.emptyList()); //TODO normalement pas util
					return fieldUi;
				})
				.collect(VCollectors.toDtList(EasyFormsFieldUi.class));
	}

	public void checkUpdateField(final DtList<EasyFormsFieldUi> fields, final Integer editIndex, final EasyFormsFieldUi fieldEdit, final UiMessageStack uiMessageStack) {
		for (int i = 0; i < fields.size(); i++) {
			if (i != editIndex && fields.get(i).getFieldCode().equals(fieldEdit.getFieldCode())) {
				//si le code n'est pas unique ce n'est pas bon.
				throw new ValidationUserException(LocaleMessageText.of("Le code du champ doit être unique dans le formulaire."), // TODO i18n
						fieldEdit, EasyFormsFieldUiFields.fieldCode);
			}
		}

		/*
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

	public Long saveNewForm(final EasyForm easyForm, final DtList<EasyFormsFieldUi> fields) {
		final var easyFormsTemplateBuilder = new EasyFormsTemplateBuilder();
		for (final EasyFormsFieldUi fieldUi : fields) {
			final EasyFormsFieldType fieldType = EasyFormsFieldType.resolve(fieldUi.getFieldType());
			Assertion.check().isNotNull(fieldType, "Field type can't be null");
			easyFormsTemplateBuilder.addField(
					fieldUi.getFieldCode(),
					fieldType,
					fieldUi.getLabel(),
					fieldUi.getTooltip(),
					Boolean.TRUE.equals(fieldUi.getIsDefault()),
					Boolean.TRUE.equals(fieldUi.getIsMandatory()),
					fieldUi.getFieldValidators());
		}
		easyForm.setTemplate(easyFormsTemplateBuilder.build());
		easyFormDAO.save(easyForm);
		return easyForm.getEfoId();
	}

	public EasyForm createEasyForm(final EasyForm easyForm) {
		Assertion.check().isNotNull(easyForm);
		//---
		return easyFormDAO.create(easyForm);
	}

}
