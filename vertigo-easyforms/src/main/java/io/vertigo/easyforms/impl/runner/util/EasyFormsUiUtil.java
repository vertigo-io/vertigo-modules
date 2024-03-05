package io.vertigo.easyforms.impl.runner.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateField;
import io.vertigo.easyforms.runner.model.ui.EasyFormsListItem;
import io.vertigo.ui.core.AbstractUiListUnmodifiable;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;
import io.vertigo.ui.impl.springmvc.util.UiUtil;
import io.vertigo.vega.webservice.model.UiList;
import io.vertigo.vega.webservice.model.UiObject;

public final class EasyFormsUiUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Gson GSON = new GsonBuilder()
			.serializeNulls()
			.create();

	public EasyFormsFieldTypeDefinition getFieldTypeByName(final String fieldTypeName) {
		return Node.getNode().getDefinitionSpace().resolve(fieldTypeName, EasyFormsFieldTypeDefinition.class);
	}

	/**
	 * @param fieldTypeName Name of the field type
	 * @return maxLength of the field
	 */
	public Integer smartTypeMaxLength(final String fieldTypeName) {
		if (fieldTypeName != null) {
			return Node.getNode().getDefinitionSpace().resolve(getFieldTypeByName(fieldTypeName).getSmartTypeName(), SmartTypeDefinition.class)
					.getProperties().getValue(DtProperty.MAX_LENGTH);
		}
		return null;
	}

	public String getEasyFormRead(final EasyFormsTemplate easyFormsTemplate, final String objectKey, final String field, final String row) {
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

	public String getEasyFormRead(final EasyFormsTemplate easyFormsTemplate, final EasyFormsData easyForm) {
		final var easyFormDisplay = new LinkedHashMap<String, Object>();
		final Map<String, Object> outOfEasyFormData = new HashMap<>(easyForm);

		for (final EasyFormsTemplateField field : easyFormsTemplate.getFields()) { // order is important
			final var fieldCode = field.getCode();

			final var fieldType = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
			final var resolvedParameters = EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());
			final String listSupplier = (String) resolvedParameters.get(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER);

			final Object rawValue = outOfEasyFormData.get(fieldCode);
			if (listSupplier == null) {
				// basic value
				easyFormDisplay.put(field.getLabel(), rawValue);
			} else {
				// value selected from list
				if (rawValue instanceof final List<?> rawList) {
					// multi-selection
					easyFormDisplay.put(field.getLabel(),
							rawList.stream()
									.map(i -> getListDisplayValue(resolvedParameters, i))
									.collect(Collectors.joining(", ")));

				} else {
					// single selection
					easyFormDisplay.put(field.getLabel(), getListDisplayValue(resolvedParameters, rawValue));
				}
			}
			outOfEasyFormData.remove(fieldCode);
		}
		for (final Entry<String, Object> champ : outOfEasyFormData.entrySet()) {
			easyFormDisplay.put(champ.getKey() + " (old)", champ.getValue());
		}

		return GSON.toJson(easyFormDisplay);
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
					.map(EasyFormsListItem::label)
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

	public String getDynamicListForField(final EasyFormsTemplateField field) {
		return getDynamicListForField(field, null);
	}

	public String getDynamicListForField(final EasyFormsTemplateField field, final String searchValue) {
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

	public EasyFormsData getParametersForField(final EasyFormsTemplateField field) {
		final var fieldType = getFieldTypeByName(field.getFieldTypeName());
		return EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());
	}

}
