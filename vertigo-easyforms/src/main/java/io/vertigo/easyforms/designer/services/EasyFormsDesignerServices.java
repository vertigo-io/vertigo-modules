/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.easyforms.designer.services;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.util.DataModelUtil;
import io.vertigo.datamodel.data.util.VCollectors;
import io.vertigo.datamodel.smarttype.SmarttypeResources;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.dao.EasyFormDAO;
import io.vertigo.easyforms.designer.Resources;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsItemUiFields;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsLabelUiFields;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsSectionUiFields;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi;
import io.vertigo.easyforms.domain.EasyFormsItemUi;
import io.vertigo.easyforms.domain.EasyFormsLabelUi;
import io.vertigo.easyforms.domain.EasyFormsSectionUi;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldValidatorTypeDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem.ItemType;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemBlock;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.rule.EasyFormsRuleParser;
import io.vertigo.easyforms.runner.rule.FormContextDescription;
import io.vertigo.vega.webservice.validation.UiErrorBuilder;
import io.vertigo.vega.webservice.validation.UiMessageStack;

@Transactional
public class EasyFormsDesignerServices implements Component {

	public static final String FORM_INTERNAL_CTX_NAME = "ctx";
	private static final Pattern EMPTY_HTML_PATTERN = Pattern.compile("^( |&nbsp;|<br */?>|</?div>)*$");

	@Inject
	private EasyFormDAO easyFormDAO;

	/**
	 * Get the list of all field types.
	 *
	 * @param filter Filter returned field types.
	 * @return A list of all field types.
	 */
	public DtList<EasyFormsFieldTypeUi> getFieldTypeUiList(final Predicate<EasyFormsFieldTypeDefinition> filter) {
		return Node.getNode().getDefinitionSpace().getAll(EasyFormsFieldTypeDefinition.class)
				.stream()
				.filter(filter)
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

	/**
	 * Get the list of all field validator types.
	 *
	 * @return A list of all field validator types.
	 */
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

	/**
	 * Check and update a section of the EasyFormsTemplate.
	 *
	 * @param easyFormsTemplate The EasyFormsTemplate to update.
	 * @param editIndex The index of the section to update.
	 * @param sectionEdit The new section data.
	 * @param labels The labels for the section.
	 * @param additionalContext Additional context for the update.
	 * @param uiMessageStack The UI message stack for the update.
	 */
	public void checkUpdateSection(final EasyFormsTemplate easyFormsTemplate, final Integer editIndex, final EasyFormsSectionUi sectionEdit, final DtList<EasyFormsLabelUi> labels,
			final Map<String, Serializable> additionalContext, final UiMessageStack uiMessageStack) {
		final UiErrorBuilder errorBuilder = new UiErrorBuilder();

		if (sectionEdit.getCode().equalsIgnoreCase(FORM_INTERNAL_CTX_NAME)) {
			errorBuilder.addError(sectionEdit, EasyFormsSectionUiFields.code, LocaleMessageText.of(Resources.EfDesignerReservedCode));
		}

		final var sections = easyFormsTemplate.getSections();
		// check section code unicity
		for (int i = 0; i < sections.size(); i++) {
			if (i != editIndex
					&& sections.get(i).getCode().equalsIgnoreCase(sectionEdit.getCode())) {
				// section code must be unique
				errorBuilder.addError(sectionEdit, EasyFormsSectionUiFields.code, LocaleMessageText.of(Resources.EfDesignerSectionCodeUnicity));
			}
		}

		if (StringUtil.isBlank(labels.get(0).getLongLabel())) {
			errorBuilder.addError(labels.get(0), EasyFormsLabelUiFields.longLabel, LocaleMessageText.of(SmarttypeResources.SMARTTYPE_MISSING_VALUE));
		}

		if (!StringUtil.isBlank(sectionEdit.getCondition())) {
			// check section condition
			final var formContextDescription = buildContextDescription(easyFormsTemplate, additionalContext);
			final var parseResult = EasyFormsRuleParser.parseComparisonTest(sectionEdit.getCondition(), formContextDescription);
			if (!parseResult.isValid()) {
				errorBuilder.addError(sectionEdit, EasyFormsSectionUiFields.condition, LocaleMessageText.of(Resources.EfDesignerSectionConditionInvalid));
				uiMessageStack.error(parseResult.getErrorMessage(), sectionEdit, EasyFormsSectionUiFields.condition + "_detail");
			}
		}
		errorBuilder.throwUserExceptionIfErrors();
	}

	/**
	 * Build a context description for the EasyFormsTemplate.
	 *
	 * @param easyFormsTemplate The EasyFormsTemplate to build the context for.
	 * @return The context description.
	 */
	public FormContextDescription buildContextDescription(final EasyFormsTemplate easyFormsTemplate) {
		return buildContextDescription(easyFormsTemplate, Map.of());
	}

	/**
	 * Build a context description for the EasyFormsTemplate with additional context.
	 *
	 * @param easyFormsTemplate The EasyFormsTemplate to build the context for.
	 * @param additionalContext Additional context for the description that will appear in 'ctx' section.
	 * @return The context description.
	 */
	public FormContextDescription buildContextDescription(final EasyFormsTemplate easyFormsTemplate, final Map<String, Serializable> additionalContext) {
		final var contextDescription = new FormContextDescription();

		// add fields from this form to context
		for (final var section : easyFormsTemplate.getSections()) {
			String prefix;
			if (easyFormsTemplate.useSections()) {
				prefix = section.getCode() + ".";
			} else {
				prefix = "";
			}

			for (final var field : section.getAllFields()) {
				final var paramFieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
				final var smartType = Node.getNode().getDefinitionSpace().resolve(paramFieldTypeDefinition.getSmartTypeName(), SmartTypeDefinition.class);

				contextDescription.add(prefix + field.getCode(), smartType.getJavaClass());
			}
		}

		// add additional fields to context
		for (final var entry : additionalContext.entrySet()) {
			addValues("ctx", entry.getKey(), entry.getValue(), contextDescription);
		}

		return contextDescription;
	}

	private void addValues(final String prefix, final String key, final Object value, final FormContextDescription contextDescription) {
		final String newKey = prefix + "." + key;
		if (value instanceof final Map<?, ?> map) {
			for (final var entry : map.entrySet()) {
				addValues(newKey, entry.getKey().toString(), entry.getValue(), contextDescription);
			}
		} else if (value instanceof final DataObject dto) {
			final var dtDefinition = DataModelUtil.findDataDefinition(dto);
			dtDefinition.getFields().forEach(field -> contextDescription.add(newKey + "." + field.name(), field.getTargetJavaClass()));
		} else if (value instanceof final FormContextDescription description) {
			description.getContextMap().forEach((k, v) -> contextDescription.add(newKey + "." + k, v));
		} else {
			contextDescription.add(newKey, value.getClass());
		}
	}

	/**
	 * Check and update a field of the EasyFormsTemplate.
	 *
	 * @param easyFormsTemplate The EasyFormsTemplate to update.
	 * @param items The items in the EasyFormsTemplate.
	 * @param editIndex The index of the field to update.
	 * @param editIndex2 The secondary index of the field to update.
	 * @param fieldEdit The new field data.
	 * @param labels The labels for the field.
	 * @param additionalContext Additional context for the update.
	 * @param uiMessageStack The UI message stack for the update.
	 */
	public void checkUpdateField(final EasyFormsTemplate easyFormsTemplate, final List<AbstractEasyFormsTemplateItem> items, final Integer editIndex, final Optional<Integer> editIndex2,
			final EasyFormsItemUi fieldEdit, final DtList<EasyFormsLabelUi> labels, final Map<String, Serializable> additionalContext, final UiMessageStack uiMessageStack) {
		final UiErrorBuilder errorBuilder = new UiErrorBuilder();

		switch (ItemType.valueOf(fieldEdit.getType())) {
			case FIELD:
				// field code must be unique in section
				for (int i = 0; i < items.size(); i++) {
					if (i != editIndex
							&& items.get(i) instanceof final EasyFormsTemplateItemField field
							&& field.getCode().equalsIgnoreCase(fieldEdit.getFieldCode())) {
						errorBuilder.addError(fieldEdit, EasyFormsItemUiFields.fieldCode, LocaleMessageText.of(Resources.EfDesignerFieldCodeUnicity));
						break;
					} else if (items.get(i) instanceof final EasyFormsTemplateItemBlock block) {
						final var blockItems = block.getItems();
						for (int j = 0; j < blockItems.size(); j++) {
							if ((editIndex2.isEmpty() || editIndex2.get() != j)
									&& blockItems.get(j) instanceof final EasyFormsTemplateItemField field
									&& field.getCode().equalsIgnoreCase(fieldEdit.getFieldCode())) {
								errorBuilder.addError(fieldEdit, EasyFormsItemUiFields.fieldCode, LocaleMessageText.of(Resources.EfDesignerFieldCodeUnicity));
								break;
							}
						}
					}
				}

				if (StringUtil.isBlank(labels.get(0).getLabel())) {
					errorBuilder.addError(labels.get(0), EasyFormsLabelUiFields.label, LocaleMessageText.of(SmarttypeResources.SMARTTYPE_MISSING_VALUE));
				}

				break;
			case STATIC:
				if (StringUtil.isBlank(labels.get(0).getText()) || isHtmlEmpty(labels.get(0).getText())) {
					errorBuilder.addError(labels.get(0), EasyFormsLabelUiFields.text, LocaleMessageText.of(SmarttypeResources.SMARTTYPE_MISSING_VALUE));
				}
				break;
			case BLOCK:
				if (!StringUtil.isBlank(fieldEdit.getCondition())) {
					// check section condition
					final var formContextDescription = buildContextDescription(easyFormsTemplate, additionalContext);
					final var parseResult = EasyFormsRuleParser.parseComparisonTest(fieldEdit.getCondition(), formContextDescription);
					if (!parseResult.isValid()) {
						errorBuilder.addError(fieldEdit, EasyFormsSectionUiFields.condition, LocaleMessageText.of(Resources.EfDesignerSectionConditionInvalid));
						uiMessageStack.error(parseResult.getErrorMessage(), fieldEdit, EasyFormsItemUiFields.condition + "_detail");
					}
				}
				break;
		}

		errorBuilder.throwUserExceptionIfErrors();
	}

	private boolean isHtmlEmpty(final String str) {
		return EMPTY_HTML_PATTERN.matcher(str).matches();
	}

	/**
	 * Save an EasyForm.
	 *
	 * @param easyForm The EasyForm to save.
	 * @return The ID of the saved EasyForm.
	 */
	public Long saveForm(final EasyForm easyForm) {
		easyFormDAO.save(easyForm);
		return easyForm.getEfoId();
	}

	/**
	 * Create a new EasyForm.
	 *
	 * @param easyForm The EasyForm to create.
	 * @return The created EasyForm.
	 */
	public EasyForm createEasyForm(final EasyForm easyForm) {
		Assertion.check().isNotNull(easyForm);
		//---
		return easyFormDAO.create(easyForm);
	}

}
