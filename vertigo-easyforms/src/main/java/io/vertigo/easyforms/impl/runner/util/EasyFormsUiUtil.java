package io.vertigo.easyforms.impl.runner.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.node.Node;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.impl.designer.services.EasyFormsDesignerServices;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateSection;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.model.ui.EasyFormsListItem;
import io.vertigo.easyforms.runner.services.IEasyFormsRunnerServices;
import io.vertigo.ui.core.AbstractUiListUnmodifiable;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;
import io.vertigo.ui.impl.springmvc.util.UiUtil;
import io.vertigo.vega.webservice.model.UiList;
import io.vertigo.vega.webservice.model.UiObject;

public final class EasyFormsUiUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public EasyFormsFieldTypeDefinition getFieldTypeByName(final String fieldTypeName) {
		return Node.getNode().getDefinitionSpace().resolve(fieldTypeName, EasyFormsFieldTypeDefinition.class);
	}

	/**
	 * @param item The template item
	 * @return maxLength of the field
	 */
	public Integer maxLength(final AbstractEasyFormsTemplateItem item) {
		if (item instanceof final EasyFormsTemplateItemField field) {
			final Integer smartTypeMaxLength = Node.getNode().getDefinitionSpace().resolve(getFieldTypeByName(field.getFieldTypeName()).getSmartTypeName(), SmartTypeDefinition.class)
					.getProperties().getValue(DtProperty.MAX_LENGTH);
			final Long fieldMaxLength = field.getParameters() == null ? null : (Long) field.getParameters().get(DtProperty.MAX_LENGTH.getName());
			return min(smartTypeMaxLength, fieldMaxLength);
		}
		return null;
	}

	private Integer min(final Integer int1, final Long int2) {
		if (int2 == null) {
			return int1;
		} else if (int1 == null) {
			return int2.intValue();
		}
		return Math.min(int1, int2.intValue());
	}

	public LinkedHashMap<String, LinkedHashMap<String, Object>> getEasyFormRead(final EasyFormsTemplate easyFormsTemplate, final String objectKey, final String field, final String row) {
		final var object = UiRequestUtil.getCurrentViewContext().get(objectKey);
		if (row == null && object instanceof final UiObject<?> uiObject) {
			final EasyFormsData easyForm = uiObject.getTypedValue(field, EasyFormsData.class);
			return getEasyFormRead(easyFormsTemplate, easyForm);
		} else if (row != null && object instanceof final UiList<?> uiList) {
			final EasyFormsData easyForm = uiList.get(Integer.valueOf(row)).getTypedValue(field, EasyFormsData.class);
			return getEasyFormRead(easyFormsTemplate, easyForm);
		}
		throw new VSystemException("Unsupported object for easy form data.");
	}

	public LinkedHashMap<String, LinkedHashMap<String, Object>> getEasyFormRead(final EasyFormsTemplate easyFormsTemplate, final EasyFormsData easyForm) {
		final var easyFormDisplay = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
		final var outOfSections = new LinkedHashSet<>(easyForm.keySet());

		// We use display order from template
		for (final EasyFormsTemplateSection section : easyFormsTemplate.getSections()) {

			outOfSections.remove(section.getCode());
			final var easyFormSectionData = (Map<String, Object>) easyForm.get(section.getCode());

			// TODO check section display condition or if contains actual data ?
			// Idea : have an option to choose behavior (eg : display empty data if it is shown in edit mode)
			if (easyFormSectionData != null) {

				final var sectionDisplay = new LinkedHashMap<String, Object>();
				easyFormDisplay.put(section.getLabel(), sectionDisplay);
				final var outOfSectionData = new HashMap<>(easyFormSectionData);

				for (final EasyFormsTemplateItemField field : getAllFieldsForSection(section)) {
					final var fieldCode = field.getCode();
					final Object rawValue = easyFormSectionData.get(fieldCode);

					// TODO check block display condition or if contains actual data ? (impact on getAllFieldsForSection to check block condition)
					// Idea : have an option to choose behavior (eg : display empty data if it is shown in edit mode)
					if (rawValue != null) {

						final var fieldType = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
						final var resolvedParameters = EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());
						final String listSupplier = (String) resolvedParameters.get(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER);

						if (listSupplier == null) {
							// basic value
							final var smartType = Node.getNode().getDefinitionSpace().resolve(fieldType.getSmartTypeName(), SmartTypeDefinition.class);
							final var smartTypeManager = Node.getNode().getComponentSpace().resolve(SmartTypeManager.class);
							String displayValue;
							if (rawValue instanceof final String str) {
								displayValue = str;
							} else {
								displayValue = smartTypeManager.valueToString(smartType, rawValue);
							}
							sectionDisplay.put(field.getLabel(), displayValue);
						} else {
							// value selected from list
							if (rawValue instanceof final List<?> rawList) {
								// multi-selection
								sectionDisplay.put(field.getLabel(),
										rawList.stream()
												.map(i -> getListDisplayValue(resolvedParameters, i))
												.collect(Collectors.joining(", ")));

							} else {
								// single selection
								sectionDisplay.put(field.getLabel(), getListDisplayValue(resolvedParameters, rawValue));
							}
						}
						outOfSectionData.remove(fieldCode);
					}
				}

				// add old section data (code + value)
				for (final Entry<String, Object> champ : outOfSectionData.entrySet()) {
					sectionDisplay.put(champ.getKey() + " (old)", champ.getValue());
				}
			}
		}
		// add old sections
		for (final String oldSection : outOfSections) {
			final var oldSectionData = (Map<String, Object>) easyForm.get(oldSection);
			final var sectionDisplay = new LinkedHashMap<String, Object>();
			easyFormDisplay.put(oldSection + " (old)", sectionDisplay);

			for (final Entry<String, Object> champ : oldSectionData.entrySet()) {
				sectionDisplay.put(champ.getKey(), champ.getValue());
			}
		}

		return easyFormDisplay;
	}

	public static List<EasyFormsTemplateItemField> getAllFieldsForSection(final EasyFormsTemplateSection section) {
		final var easyFormsRunnerServices = Node.getNode().getComponentSpace().resolve(IEasyFormsRunnerServices.class);
		return easyFormsRunnerServices.getAllFieldsFromSection(section);
	}

	private String getListDisplayValue(final EasyFormsData resolvedParameters, final Object rawValue) {
		if (rawValue == null) {
			return "";
		}

		final String listSupplier = (String) resolvedParameters.get(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER);
		final var ctxNameOpt = resolveCtxName(listSupplier);
		if (ctxNameOpt.isPresent()) {
			return getValueFromContext(ctxNameOpt.get(), rawValue);
		} else {
			return EasyFormsListItem.ofCollection(resolvedParameters.getOrDefault(listSupplier, List.of()))
					.stream()
					.filter(item -> Objects.equals(item.value(), rawValue))
					.map(EasyFormsListItem::getDisplayLabel)
					.findFirst()
					.orElse(rawValue.toString());
		}
	}

	private String getValueFromContext(final String ctxKey, final Object value) {
		final var uiList = (AbstractUiListUnmodifiable<?>) UiRequestUtil.getCurrentViewContext().getUiList(() -> ctxKey);
		final var dtDefinition = uiList.getDtDefinition();
		final var idField = dtDefinition.getIdField().get();
		final var displayField = dtDefinition.getDisplayField().get();

		return uiList.getById(idField.name(), (Serializable) value).getSingleInputValue(displayField.name());
	}

	public String getDynamicListForField(final EasyFormsTemplateItemField field) {
		return getDynamicListForField(field, null);
	}

	public String getDynamicListForField(final EasyFormsTemplateItemField field, final String searchValue) {
		final var fieldType = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);

		final var resolvedParameters = EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());

		final String listSupplier = (String) resolvedParameters.get(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER);
		final var ctxNameOpt = resolveCtxName(listSupplier);

		if (ctxNameOpt.isPresent()) {
			return listFromContext(ctxNameOpt.get(), searchValue);
		} else {
			return EasyFormsListItem.ofCollection(resolvedParameters.getOrDefault(listSupplier, List.of())).toString();
		}
	}

	private Optional<String> resolveCtxName(final String listSupplier) {
		if (listSupplier.startsWith(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_REF_PREFIX)) {
			final var entityName = listSupplier.substring(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_REF_PREFIX.length());
			return Optional.of(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_REF_CTX_NAME_PREFIX + entityName);
		} else if (listSupplier.startsWith(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_CTX_PREFIX)) {
			return Optional.of(listSupplier.substring(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_CTX_PREFIX.length()));
		}
		return Optional.empty();
	}

	private String listFromContext(final String ctxKeyName, final String searchValue) {
		final String idField = UiUtil.getIdField(ctxKeyName);
		final String displayField = UiUtil.getDisplayField(ctxKeyName);

		return "transformListForSelection('" + ctxKeyName + "', '" + idField + "', '" + displayField + "', null, " + searchValue + ")";
	}

	public EasyFormsData getParametersForField(final EasyFormsTemplateItemField field) {
		final var fieldType = getFieldTypeByName(field.getFieldTypeName());
		return EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());
	}

	public String resolveModelName(final EasyFormsTemplate template, final EasyFormsTemplateSection section, final EasyFormsTemplateItemField field) {
		return "slotProps.formData['" + (template.useSections() ? section.getCode() + "']['" : "") + field.getCode() + "']";
	}

	public String convertConditionToJs(final String context, final String condition) {
		if (StringUtil.isBlank(condition)) {
			return "true";
		}

		return condition
				.replaceAll("#" + EasyFormsDesignerServices.FORM_INTERNAL_CTX_NAME + "\\.([a-zA-Z0-9_\\-\\.]+)#", "vueData.$1") // #ctx.xxx# => vueData.xxx
				.replaceAll("#([a-zA-Z0-9_\\-\\.]+)#", context + ".$1") // #xxx# => vueData.object.field.xxx
				.replaceAll("(?i) and ", " && ")
				.replaceAll("(?i) or ", " || ")
				.replaceAll("([^!><])=", "$1===")
				.replaceAll("!=", "!==");
	}

}
