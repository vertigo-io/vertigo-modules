/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.easyforms.impl.designer.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.vertigo.account.security.UserSession;
import io.vertigo.account.security.VSecurityManager;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.lang.VUserException;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.util.ClassUtil;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.data.definitions.DataFieldName;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.data.util.VCollectors;
import io.vertigo.easyforms.designer.services.IEasyFormsDesignerServices;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsFieldValidatorTypeUiFields;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsItemUiFields;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi;
import io.vertigo.easyforms.domain.EasyFormsItemUi;
import io.vertigo.easyforms.domain.EasyFormsLabelUi;
import io.vertigo.easyforms.domain.EasyFormsSectionUi;
import io.vertigo.easyforms.impl.designer.Resources;
import io.vertigo.easyforms.impl.runner.util.EasyFormsUiUtil;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldValidatorTypeDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem.ItemType;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateFieldValidator;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateSection;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemBlock;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemStatic;
import io.vertigo.easyforms.runner.services.IEasyFormsRunnerServices;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.quasar.tree.Tree;
import io.vertigo.ui.impl.quasar.tree.TreeNode;
import io.vertigo.ui.impl.springmvc.argumentresolvers.ViewAttribute;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;
import io.vertigo.vega.webservice.stereotype.Validate;
import io.vertigo.vega.webservice.validation.AbstractDtObjectValidator;
import io.vertigo.vega.webservice.validation.DefaultDtObjectValidator;
import io.vertigo.vega.webservice.validation.UiMessageStack;
import io.vertigo.vega.webservice.validation.ValidationUserException;

@Controller
@RequestMapping("/easyforms/designer")
public final class EasyFormsDesignerController extends AbstractVSpringMvcController {

	private static final ViewContextKey<EasyForm> efoKey = ViewContextKey.of("efo");
	private static final ViewContextKey<String> efoCurrentLang = ViewContextKey.of("efoLang");
	private static final ViewContextKey<ArrayList<String>> efoSupportedLang = ViewContextKey.of("efoAllLang");

	private static final ViewContextKey<EasyFormsFieldTypeUi> fieldTypesKey = ViewContextKey.of("fieldTypes");
	private static final ViewContextKey<Serializable> fieldTypesTemplateKey = ViewContextKey.of("fieldTypesTemplate");

	private static final ViewContextKey<EasyFormsSectionUi> editSectionKey = ViewContextKey.of("editSection");
	private static final ViewContextKey<EasyFormsItemUi> editItemKey = ViewContextKey.of("editItem");
	private static final ViewContextKey<EasyFormsLabelUi> editLabelTextKey = ViewContextKey.of("editLabelText");

	private static final ViewContextKey<EasyFormsFieldValidatorTypeUi> fieldValidatorsKey = ViewContextKey.of("fieldValidators");
	private static final ViewContextKey<EasyFormsFieldValidatorTypeUi> editFieldValidatorTypesKey = ViewContextKey.of("editFieldValidatorTypes");
	private static final ViewContextKey<Serializable> validatorEfoLabelsKey = ViewContextKey.of("validatorEfoLabels");

	private static final ViewContextKey<Serializable> additionalContextKey = ViewContextKey.of("additionalContext");
	private static final ViewContextKey<Tree> contextTreeKey = ViewContextKey.of("contextTree");

	private static final ViewContextKey<EasyFormsUiUtil> efoUiUtilKey = ViewContextKey.of("efoUiUtil");

	private static final ViewContextKey<String> messageKey = ViewContextKey.of("message");

	@Inject
	private IEasyFormsDesignerServices easyFormsDesignerServices;

	@Inject
	private IEasyFormsRunnerServices easyFormsRunnerServices;

	@Inject
	private EasyFormsRunnerManager easyFormsRunnerManager;

	@Inject
	private VSecurityManager securityManager;

	public void initContext(final ViewContext viewContext, final Optional<UID<EasyForm>> efoIdOpt, final Map<String, Serializable> additionalContext) {
		final EasyForm easyForm = efoIdOpt
				.map(easyFormsRunnerServices::getEasyFormById)
				.orElseGet(EasyForm::new);

		final var fieldTypeUiList = easyFormsDesignerServices.getFieldTypeUiList();
		fieldTypeUiList.sort(Comparator.comparing(EasyFormsFieldTypeUi::getLabel));

		final ArrayList<String> supportedLang = new ArrayList<>(easyFormsRunnerManager.getSupportedLang()); // needed to be ArrayList to be serializable
		final var userLang = securityManager.getCurrentUserSession().map(UserSession::getLocale).map(Locale::getLanguage).orElse("fr");

		viewContext.publishDto(efoKey, easyForm)
				.publishRef(efoCurrentLang, userLang)
				.publishRef(efoSupportedLang, supportedLang)
				.publishRef(additionalContextKey, (Serializable) additionalContext)
				.publishDtList(fieldTypesKey, fieldTypeUiList)
				.publishRef(fieldTypesTemplateKey,
						(Serializable) fieldTypeUiList.stream()
								.filter(EasyFormsFieldTypeUi::getHasTemplate)
								.collect(Collectors.toMap(EasyFormsFieldTypeUi::getName, EasyFormsFieldTypeUi::getParamTemplate)))
				.publishDtList(fieldValidatorsKey, EasyFormsFieldValidatorTypeUiFields.name, easyFormsDesignerServices.getFieldValidatorTypeUiList())
				.publishDtList(editFieldValidatorTypesKey, new DtList<>(EasyFormsFieldValidatorTypeUi.class))
				.publishDto(editSectionKey, new EasyFormsSectionUi())
				.publishDto(editItemKey, buildNewItemUi(ItemType.FIELD))
				.publishRef(efoUiUtilKey, new EasyFormsUiUtil())
				.publishRef(messageKey, "");
		updateFormContext(viewContext, easyForm, additionalContext);
		updateValidatorEfoLabels(viewContext, easyForm);
		setEmptyEditLabelText(viewContext, supportedLang);
	}

	private static EasyFormsItemUi buildNewItemUi(final ItemType type) {
		final var itemUi = new EasyFormsItemUi();
		itemUi.setType(type.name());

		switch (type) {
			case FIELD:
				itemUi.setIsSystem(false);
				itemUi.setIsMandatory(false);
				itemUi.setIsList(false);
				break;
			default:
				// noting
		}

		return itemUi;
	}

	private EasyFormsItemUi toItemUi(final AbstractEasyFormsTemplateItem item) {
		final EasyFormsItemUi itemUi = new EasyFormsItemUi();
		itemUi.setType(item.getType());
		if (item instanceof final EasyFormsTemplateItemField field) {
			itemUi.setFieldCode(field.getCode());
			itemUi.setFieldType(field.getFieldTypeName());
			itemUi.setIsSystem(field.isSystem());
			itemUi.setIsMandatory(field.isMandatory());
			itemUi.setDefaultValue(field.getDefaultValue() == null ? null : field.getDefaultValue().toString());

			itemUi.setMaxItems(field.getMaxItems());

			updateItemForType(itemUi);

			itemUi.setParameters(field.getParameters());
			if (field.getValidators() != null) {
				itemUi.setFieldValidatorSelection(field.getValidators().stream().map(EasyFormsTemplateFieldValidator::getName).toList());
			}
		} else if (item instanceof final EasyFormsTemplateItemBlock block) {
			itemUi.setCondition(block.getCondition());
		} else if (item instanceof final EasyFormsTemplateItemStatic staticItem) {
			// nothing (text is handled outside this method)
		} else {
			throw new VSystemException("Unsupported class of type " + item.getClass().getName());
		}
		return itemUi;
	}

	private AbstractEasyFormsTemplateItem mergeUiToItem(final AbstractEasyFormsTemplateItem previousItem, final EasyFormsItemUi uiItem, final DtList<EasyFormsLabelUi> labels,
			final UiMessageStack uiMessageStack) {
		final Class<? extends AbstractEasyFormsTemplateItem> itemClass = ItemType.getClassFromString(uiItem.getType());

		final AbstractEasyFormsTemplateItem item;
		if (previousItem == null) {
			item = ClassUtil.newInstance(itemClass);
		} else {
			Assertion.check().isTrue(previousItem.getClass().equals(itemClass), "Class type mismatch");
			item = previousItem;
		}

		if (item instanceof final EasyFormsTemplateItemField field) {
			if (!field.isSystem()) {
				// system fields are code and type fixed
				field.setCode(uiItem.getFieldCode());
				field.setFieldTypeName(uiItem.getFieldType());
			}
			field.setLabel(getFromEditLabelText(labels, EasyFormsLabelUi::getLabel));
			field.setTooltip(getFromEditLabelText(labels, EasyFormsLabelUi::getTooltip));
			field.setMandatory(uiItem.getIsMandatory());
			field.setMaxItems(uiItem.getMaxItems());

			final var fieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(uiItem.getFieldType(), EasyFormsFieldTypeDefinition.class);
			if (fieldTypeDefinition.getParamTemplate() != null) {
				easyFormsRunnerServices.formatAndCheckFormulaire(uiItem, uiItem.getParameters(), fieldTypeDefinition.getParamTemplate(), getUiMessageStack());
				field.setParameters(uiItem.getParameters());
			} else {
				field.setParameters(null);
			}

			field.setValidators(uiItem.getFieldValidatorSelection().stream().map(EasyFormsTemplateFieldValidator::new).toList());

			// handle default value, all previous configuration is used to check the default value validity (except for mandatory, we skip verification if default value is empty)
			if (!uiItem.getIsList() && !StringUtil.isBlank(uiItem.getDefaultValue())) {
				field.setDefaultValue(easyFormsRunnerServices.formatAndCheckSingleField(uiItem, EasyFormsItemUiFields.defaultValue.name(), field, uiItem.getDefaultValue(), uiMessageStack));
				if (uiMessageStack.hasErrors()) {
					throw new ValidationUserException();
				}
			} else {
				field.setDefaultValue(null);
			}
		} else if (item instanceof final EasyFormsTemplateItemBlock block) {
			block.setCondition(uiItem.getCondition());
		} else if (item instanceof final EasyFormsTemplateItemStatic staticItem) {
			staticItem.setText(getFromEditLabelText(labels, EasyFormsLabelUi::getText));
		} else {
			throw new VSystemException("Unsupported class of type " + item.getClass().getName());
		}

		return item;
	}

	private void setEmptyEditLabelText(final ViewContext viewContext, final List<String> supportedLang) {
		final var list = new DtList<>(EasyFormsLabelUi.class);
		for (final String lang : supportedLang) {
			final var newLabel = new EasyFormsLabelUi();
			newLabel.setLang(lang);
			list.add(newLabel);
		}
		viewContext.publishDtListModifiable(editLabelTextKey, list);
	}

	private void setSectionEditLabelText(final ViewContext viewContext, final List<String> supportedLang, final EasyFormsTemplateSection section) {
		final var list = new DtList<>(EasyFormsLabelUi.class);
		final var labels = section.getLabel();
		for (final String lang : supportedLang) {
			final var newLabel = new EasyFormsLabelUi();
			newLabel.setLang(lang);
			newLabel.setLabel(labels.get(lang));
			list.add(newLabel);
		}
		viewContext.publishDtListModifiable(editLabelTextKey, list);
	}

	private void setItemEditLabelText(final ViewContext viewContext, final List<String> supportedLang, final AbstractEasyFormsTemplateItem item) {
		final var list = new DtList<>(EasyFormsLabelUi.class);
		if (item instanceof final EasyFormsTemplateItemField field) {
			final var labels = field.getLabel();
			final var tooltips = field.getTooltip();
			for (final String lang : supportedLang) {
				final var newLabel = new EasyFormsLabelUi();
				newLabel.setLang(lang);
				newLabel.setLabel(labels.get(lang));
				if (tooltips != null) {
					newLabel.setTooltip(tooltips.get(lang));
				}
				list.add(newLabel);
			}
		} else if (item instanceof final EasyFormsTemplateItemStatic staticItem) {
			final var labels = staticItem.getText();
			for (final String lang : supportedLang) {
				final var newLabel = new EasyFormsLabelUi();
				newLabel.setLang(lang);
				newLabel.setText(labels.get(lang));
				list.add(newLabel);
			}
		}

		viewContext.publishDtListModifiable(editLabelTextKey, list);
	}

	private Map<String, String> getFromEditLabelText(final DtList<EasyFormsLabelUi> list, final Function<EasyFormsLabelUi, String> fieldAccessor) {
		final var labels = new HashMap<String, String>();
		for (final EasyFormsLabelUi label : list) {
			labels.put(label.getLang(), fieldAccessor.apply(label));
		}
		return labels;
	}

	// ****
	// * Sections
	// ****
	@PostMapping("/_addSection")
	public ViewContext addNewSection(final ViewContext viewContext,
			@ViewAttribute("efoAllLang") final List<String> supportedLang) {

		final var sectionUi = new EasyFormsSectionUi();

		viewContext.publishDto(editSectionKey, sectionUi);
		setEmptyEditLabelText(viewContext, supportedLang);
		return viewContext;
	}

	@PostMapping("/_editSection")
	public ViewContext editSection(final ViewContext viewContext,
			@ViewAttribute("efo") final EasyForm efo,
			@RequestParam("sectionIndex") final Integer sectionIndex,
			@ViewAttribute("efoAllLang") final List<String> supportedLang) {

		final var editedSection = efo.getTemplate().getSections().get(sectionIndex.intValue());
		final var sectionUi = new EasyFormsSectionUi();
		sectionUi.setCode(editedSection.getCode());
		sectionUi.setCondition(editedSection.getCondition());
		sectionUi.setHaveSystemField(editedSection.haveSystemField());

		viewContext.publishDto(editSectionKey, sectionUi);
		setSectionEditLabelText(viewContext, supportedLang, editedSection);
		return viewContext;
	}

	@PostMapping("/_deleteSection")
	public ViewContext deleteSection(final ViewContext viewContext,
			@ViewAttribute("efo") final EasyForm efo,
			@ViewAttribute("additionalContext") final Map<String, Serializable> additionalContext,
			@RequestParam("sectionIndex") final Integer sectionIndex) {

		final var removed = efo.getTemplate().getSections().remove(sectionIndex.intValue());
		if (removed.haveSystemField()) {
			throw new VUserException("Cannot delete section with system field");
		}

		viewContext.publishDto(efoKey, efo)
				.publishRef(messageKey, LocaleMessageText.of(Resources.EfDesignerSectionDeleted).getDisplay()); // Vertigo should handle flash messages through uiMessageStack
		updateFormContext(viewContext, efo, additionalContext);
		updateValidatorEfoLabels(viewContext, efo);
		return viewContext;
	}

	@PostMapping("/_moveSection")
	public ViewContext moveSection(final ViewContext viewContext,
			@ViewAttribute("efo") final EasyForm efo,
			@RequestParam("fromSectionIndex") final Integer fromSectionIndex,
			@RequestParam("toSectionIndex") final Integer toSectionIndex) {

		final var sections = efo.getTemplate().getSections();

		final var toMove = sections.remove(fromSectionIndex.intValue());
		sections.add(toSectionIndex.intValue(), toMove);

		viewContext.publishDto(efoKey, efo)
				.publishRef(messageKey, LocaleMessageText.of(Resources.EfDesignerSectionMoved).getDisplay()); // Vertigo should handle flash messages through uiMessageStack
		updateValidatorEfoLabels(viewContext, efo);
		return viewContext;
	}

	@PostMapping("/_saveSection")
	public ViewContext saveSection(final ViewContext viewContext,
			@ViewAttribute("efo") final EasyForm efo,
			@RequestParam("sectionIndex") final Integer sectionIndex,
			@ViewAttribute("editSection") final EasyFormsSectionUi editSectionUi,
			@ViewAttribute("additionalContext") final Map<String, Serializable> additionalContext,
			@ViewAttribute("editLabelText") final DtList<EasyFormsLabelUi> labels,
			final UiMessageStack uiMessageStack) {

		easyFormsDesignerServices.checkUpdateSection(efo.getTemplate(), sectionIndex, editSectionUi, labels, additionalContext, uiMessageStack);

		final boolean isNew = sectionIndex == -1;
		final EasyFormsTemplateSection section;
		if (isNew) {
			section = new EasyFormsTemplateSection();
			efo.getTemplate().getSections().add(section);
		} else {
			section = efo.getTemplate().getSections().get(sectionIndex.intValue());
		}

		section.setCode(editSectionUi.getCode());
		section.setLabel(getFromEditLabelText(labels, EasyFormsLabelUi::getLabel));
		section.setCondition(editSectionUi.getCondition());

		viewContext.publishDto(efoKey, efo)
				.publishRef(messageKey, LocaleMessageText.of(Resources.EfDesignerSectionValidated).getDisplay()); // Vertigo should handle flash messages through uiMessageStack
		updateFormContext(viewContext, efo, additionalContext);

		return viewContext;

	}

	// update context to show context helper, for display condition, in section and block detail
	private void updateFormContext(final ViewContext viewContext, final EasyForm efo, final Map<String, Serializable> additionalContext) {
		final var contextDescription = easyFormsDesignerServices.buildContextDescription(efo.getTemplate(), additionalContext);

		final var tree = new Tree();
		contextDescription.getContextMap().entrySet().stream()
				.sorted(Comparator.comparing(Entry::getKey))
				.forEach(entry -> {
					addKeyToNodes(tree.getChildren(), entry.getKey(), entry.getKey(), entry.getValue());
				});

		viewContext.publishRef(contextTreeKey, tree);
	}

	private void addKeyToNodes(final List<TreeNode> nodes, final String fullKey, final String key, final Class<?> clazz) {
		if (!key.contains(".")) {
			final var newNode = new TreeNode();
			newNode.setLabel(key + " (" + clazz.getSimpleName() + ")");
			newNode.setValue("#" + fullKey + "#");
			nodes.add(newNode);
		} else {
			final var keySplit = key.split("\\.", 2);
			final var newNode = getOrCreateNode(nodes, keySplit[0]);
			addKeyToNodes(newNode.getChildren(), fullKey, keySplit[1], clazz);
		}
	}

	private TreeNode getOrCreateNode(final List<TreeNode> nodes, final String key) {
		for (final TreeNode node : nodes) {
			if (node.getLabel().equals(key)) {
				return node;
			}
		}
		final var node = new TreeNode();
		node.setLabel(key);
		node.setExpandable(true);
		nodes.add(node);
		return node;
	}

	// ****
	// * Items
	// ****
	@PostMapping("/_addItem")
	public ViewContext addNewItem(final ViewContext viewContext,
			@RequestParam("type") final String typeString,
			@ViewAttribute("efoAllLang") final List<String> supportedLang) {

		final var type = ItemType.valueOf(typeString);

		viewContext.publishDto(editItemKey, buildNewItemUi(type))
				.publishDtList(editFieldValidatorTypesKey, new DtList<>(EasyFormsFieldValidatorTypeUi.class));
		setEmptyEditLabelText(viewContext, supportedLang);
		return viewContext;
	}

	@PostMapping("/_deleteItem")
	public ViewContext deleteItem(final ViewContext viewContext,
			@ViewAttribute("efo") final EasyForm efo,
			@ViewAttribute("additionalContext") final Map<String, Serializable> additionalContext,
			@RequestParam("sectionIndex") final Integer sectionIndex,
			@RequestParam("editIndex") final Integer editIndex,
			@RequestParam("editIndex2") final Optional<Integer> editIndex2) {

		final var sectionItems = efo.getTemplate().getSections().get(sectionIndex.intValue()).getItems();
		final var items = resolveLocalItems(editIndex, editIndex2, sectionItems);
		final var removed = items.remove(editIndex2.orElse(editIndex).intValue());

		if (removed instanceof final EasyFormsTemplateItemField removedField && removedField.isSystem()) {
			throw new VUserException("Cannot delete system field");
		} else if (removed instanceof final EasyFormsTemplateItemBlock removedBlock && removedBlock.haveSystemField()) {
			throw new VUserException("Cannot delete block with system field");
		}

		viewContext.publishDto(efoKey, efo)
				.publishRef(messageKey, LocaleMessageText.of(Resources.EfDesignerItemDeleted).getDisplay()); // Vertigo should handle flash messages through uiMessageStack
		updateFormContext(viewContext, efo, additionalContext);
		updateValidatorEfoLabels(viewContext, efo);
		return viewContext;
	}

	private static void updateValidatorEfoLabels(final ViewContext viewContext, final EasyForm efo) {
		final Map<String, List<String>> validatorEfoLabels = new HashMap<>();
		final var sections = efo.getTemplate().getSections();
		for (int i = 0; i < sections.size(); i++) {
			final var items = sections.get(i).getItems();
			for (int j = 0; j < items.size(); j++) {
				final var item = items.get(j);
				if (item instanceof final EasyFormsTemplateItemField field && field.getValidators() != null) {
					validatorEfoLabels.put(computeValidatorEfoLabelsKey(i, j, Optional.empty()), getValidatorsLabels(field));
				} else if (item instanceof final EasyFormsTemplateItemBlock block) {
					for (int k = 0; k < block.getItems().size(); k++) {
						final var blocItem = block.getItems().get(k);
						if (blocItem instanceof final EasyFormsTemplateItemField field && field.getValidators() != null) {
							validatorEfoLabels.put(computeValidatorEfoLabelsKey(i, j, Optional.of(k)), getValidatorsLabels(field));
						}
					}
				}
			}
		}
		viewContext.publishRef(validatorEfoLabelsKey, (Serializable) validatorEfoLabels);
	}

	private static List<String> getValidatorsLabels(final EasyFormsTemplateItemField field) {
		return field.getValidators().stream()
				.map(validator -> {
					final var validatorType = Node.getNode().getDefinitionSpace().resolve(validator.getName(), EasyFormsFieldValidatorTypeDefinition.class);
					return validatorType.getParameterizedLabel(null);
				})
				.toList();
	}

	private static String computeValidatorEfoLabelsKey(final Integer sectionIndex, final Integer editIndex, final Optional<Integer> editIndex2) {
		return sectionIndex + ";" + editIndex + editIndex2.map(e -> ";" + e).orElse("");
	}

	@PostMapping("/_moveItem")
	public ViewContext moveItem(final ViewContext viewContext,
			@ViewAttribute("efo") final EasyForm efo,
			@RequestParam("sectionIndex") final Integer sectionIndex,
			@RequestParam("fromIndex") final Integer fromIndex,
			@RequestParam("fromIndex2") final Optional<Integer> fromIndex2,
			@RequestParam("toIndex") final Integer toIndex,
			@RequestParam("toIndex2") final Optional<Integer> toIndex2) {

		final var sectionItems = efo.getTemplate().getSections().get(sectionIndex.intValue()).getItems();

		final var fromItems = resolveLocalItems(fromIndex, fromIndex2, sectionItems);
		final var localFromIndex = fromIndex2.orElse(fromIndex).intValue();

		final var toItems = resolveLocalItems(toIndex, toIndex2, sectionItems);
		final var localToIndex = toIndex2.orElse(toIndex).intValue();

		final var toMove = fromItems.remove(localFromIndex);
		toItems.add(localToIndex, toMove);

		viewContext.publishDto(efoKey, efo)
				.publishRef(messageKey, LocaleMessageText.of(Resources.EfDesignerItemMoved).getDisplay()); // Vertigo should handle flash messages through uiMessageStack
		updateValidatorEfoLabels(viewContext, efo);
		return viewContext;
	}

	@PostMapping("/_editItem")
	public ViewContext editItem(final ViewContext viewContext,
			@ViewAttribute("efo") final EasyForm efo,
			@RequestParam("sectionIndex") final Integer sectionIndex,
			@RequestParam("editIndex") final Integer editIndex,
			@RequestParam("editIndex2") final Optional<Integer> editIndex2,
			@ViewAttribute("efoAllLang") final List<String> supportedLang) {

		var editedItem = efo.getTemplate().getSections().get(sectionIndex.intValue()).getItems().get(editIndex.intValue());
		if (editIndex2.isPresent()) {
			Assertion.check().isTrue(EasyFormsTemplateItemBlock.class.equals(editedItem.getClass()), "Type mismatch");
			editedItem = ((EasyFormsTemplateItemBlock) editedItem).getItems().get(editIndex2.get());
		}

		final var editedItemUi = toItemUi(editedItem);
		viewContext.publishDto(editItemKey, editedItemUi);
		loadValidatorsByType(viewContext, easyFormsDesignerServices.getFieldValidatorTypeUiList(), editedItemUi);
		setItemEditLabelText(viewContext, supportedLang, editedItem);

		return viewContext;
	}

	// ****
	// * Item detail
	// ****

	@PostMapping("/_refreshItem") // when editing field, change field type
	public ViewContext refreshItem(final ViewContext viewContext,
			@ViewAttribute("efo") final EasyForm efo,
			@RequestParam("sectionIndex") final Integer sectionIndex,
			@RequestParam("doUpdateCode") final Boolean updateCode,
			@Validate({}) @ViewAttribute("editItem") final EasyFormsItemUi editItem) { // do not validate editItem

		final var section = efo.getTemplate().getSections().get(sectionIndex.intValue());

		// set default code if not customized by user
		if (Boolean.TRUE.equals(updateCode)) {
			editItem.setFieldCode(computeDefaultFieldCode(section, editItem));
		}

		// add possible validators
		loadValidatorsByType(viewContext, easyFormsDesignerServices.getFieldValidatorTypeUiList(), editItem);

		updateItemForType(editItem);

		viewContext.publishDto(editItemKey, editItem);
		return viewContext;
	}

	private void updateItemForType(final EasyFormsItemUi itemUi) {
		final var fieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(itemUi.getFieldType(), EasyFormsFieldTypeDefinition.class);

		itemUi.setIsList(fieldTypeDefinition.isList());

		// add default values for field type parameters
		if (fieldTypeDefinition.getParamTemplate() != null) {
			itemUi.setParameters(easyFormsRunnerServices.getDefaultDataValues(fieldTypeDefinition.getParamTemplate()));
		}
	}

	@PostMapping("/_saveItem")
	public ViewContext saveItem(final ViewContext viewContext,
			@ViewAttribute("efo") final EasyForm efo,
			@RequestParam("sectionIndex") final Integer sectionIndex,
			@RequestParam("editIndex") final Integer editIndex,
			@RequestParam("editIndex2") final Optional<Integer> editIndex2,
			@Validate({ DefaultDtObjectValidator.class, EditItemValidator.class }) @ViewAttribute("editItem") final EasyFormsItemUi editUiItem,
			@ViewAttribute("editLabelText") final DtList<EasyFormsLabelUi> labels,
			@ViewAttribute("additionalContext") final Map<String, Serializable> additionalContext,
			final UiMessageStack uiMessageStack) {

		final List<AbstractEasyFormsTemplateItem> sectionItems = efo.getTemplate().getSections().get(sectionIndex.intValue()).getItems();
		// check code unicity in section
		easyFormsDesignerServices.checkUpdateField(efo.getTemplate(), sectionItems, editIndex, editIndex2, editUiItem, labels, additionalContext, uiMessageStack);

		final List<AbstractEasyFormsTemplateItem> items = resolveLocalItems(editIndex, editIndex2, sectionItems);

		AbstractEasyFormsTemplateItem editItem;
		if (editIndex2.orElse(editIndex) == -1) {
			editItem = mergeUiToItem(null, editUiItem, labels, uiMessageStack);
			items.add(editItem);
		} else {
			editItem = mergeUiToItem(items.get(editIndex2.orElse(editIndex)), editUiItem, labels, uiMessageStack);
		}

		viewContext.publishDto(efoKey, efo)
				.publishRef(messageKey, LocaleMessageText.of(Resources.EfDesignerItemValidated).getDisplay()); // Vertigo should handle flash messages through uiMessageStack
		updateFormContext(viewContext, efo, additionalContext);
		updateValidatorEfoLabels(viewContext, efo);

		return viewContext;
	}

	private List<AbstractEasyFormsTemplateItem> resolveLocalItems(final Integer editIndex, final Optional<Integer> editIndex2, final List<AbstractEasyFormsTemplateItem> sectionItems) {
		final List<AbstractEasyFormsTemplateItem> items;
		if (editIndex == -1 || editIndex2.isEmpty()) {
			items = sectionItems;
		} else {
			final var item = sectionItems.get(editIndex);
			Assertion.check().isTrue(EasyFormsTemplateItemBlock.class.equals(item.getClass()), "Type mismatch");
			items = ((EasyFormsTemplateItemBlock) item).getItems();
		}
		return items;
	}

	public Long save(final ViewContext viewContext) {
		final var efo = viewContext.readDto(efoKey, getUiMessageStack());
		return easyFormsDesignerServices.saveForm(efo);
	}

	protected static void loadValidatorsByType(final ViewContext viewContext,
			final DtList<EasyFormsFieldValidatorTypeUi> fieldValidators,
			final EasyFormsItemUi editItem) {
		final DtList<EasyFormsFieldValidatorTypeUi> fieldValidatorsByType = fieldValidators.stream()
				.filter(c -> c.getFieldTypes().contains(editItem.getFieldType()))
				.collect(VCollectors.toDtList(EasyFormsFieldValidatorTypeUi.class));

		viewContext.publishDtList(editFieldValidatorTypesKey, fieldValidatorsByType);
	}

	protected String computeDefaultFieldCode(final EasyFormsTemplateSection section, final EasyFormsItemUi editedField) {
		final var prefixFieldCode = StringUtil.first2LowerCase(editedField.getFieldType().substring(5));
		final var pattern = Pattern.compile("^" + prefixFieldCode + "[0-9]*$");

		final Optional<Integer> lastMatchingOpt = section.getAllFields().stream()
				.filter(f -> pattern.matcher(f.getCode()).matches())
				.map(f -> {
					if (prefixFieldCode.length() == f.getCode().length()) {
						return 1;
					}
					return Integer.valueOf(f.getCode().substring(prefixFieldCode.length()));
				})
				.sorted(Comparator.reverseOrder())
				.findFirst();
		if (lastMatchingOpt.isPresent()) {
			return prefixFieldCode + (lastMatchingOpt.get() + 1);
		} else {
			return prefixFieldCode;
		}
	}

	public static class EditItemValidator extends AbstractDtObjectValidator<EasyFormsItemUi> {

		@Override
		protected List<DataFieldName<EasyFormsItemUi>> getFieldsToNullCheck(final EasyFormsItemUi dtObject) {
			switch (ItemType.valueOf(dtObject.getType())) {
				case FIELD:
					return List.of(EasyFormsItemUiFields.fieldCode,
							EasyFormsItemUiFields.fieldType,
							EasyFormsItemUiFields.isMandatory);
				default:
					return List.of();
			}
		}

	}

}
