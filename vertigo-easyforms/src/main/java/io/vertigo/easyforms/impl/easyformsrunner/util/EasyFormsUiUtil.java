package io.vertigo.easyforms.impl.easyformsrunner.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsListItem;
import io.vertigo.easyforms.easyformsrunner.model.definitions.IEasyFormsUiComponentSupplier;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateField;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;
import io.vertigo.ui.impl.springmvc.util.UiUtil;
import io.vertigo.vega.webservice.model.UiList;
import io.vertigo.vega.webservice.model.UiObject;

public class EasyFormsUiUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Gson GSON = new GsonBuilder()
			.serializeNulls()
			.create();

	public EasyFormsFieldType getFieldTypeByName(final String fieldTypeName) {
		return Node.getNode().getDefinitionSpace().resolve(fieldTypeName, EasyFormsFieldType.class);
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
		final Map<String, Object> outOfEasyFormTemplate = new HashMap<>(easyForm);

		for (final EasyFormsTemplateField field : easyFormsTemplate.getFields()) { // order is important
			final var fieldCode = field.getCode();
			easyFormDisplay.put(field.getLabel(), outOfEasyFormTemplate.get(fieldCode));
			outOfEasyFormTemplate.remove(fieldCode);
		}
		for (final Entry<String, Object> champ : outOfEasyFormTemplate.entrySet()) {
			easyFormDisplay.put(champ.getKey() + " (old)", champ.getValue().toString());
		}

		return GSON.toJson(easyFormDisplay);
	}

	public String getDynamicListForField(final EasyFormsTemplateField field) {
		final var fieldType = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldType.class);

		final var resolvedParameters = EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());

		final String listSupplier = (String) resolvedParameters.get(IEasyFormsUiComponentSupplier.LIST_SUPPLIER);

		if (listSupplier.startsWith(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_REF_PREFIX)) {
			final var entityName = listSupplier.substring(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_REF_PREFIX.length());
			// TODO, handle MDL code (an other separator can be usefull but for configuration an other attribute is better...)
			return listFromContext(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_REF_CTX_NAME_PREFIX + entityName);
		} else if (listSupplier.startsWith(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_CTX_PREFIX)) {
			final var ctxKeyName = listSupplier.substring(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_CTX_PREFIX.length());
			return listFromContext(ctxKeyName);
		} else {
			return EasyFormsListItem.ofCollection(resolvedParameters.getOrDefault(listSupplier, List.of())).toString();
		}
	}

	private String listFromContext(final String ctxKeyName) {
		final String idField = UiUtil.getIdField(ctxKeyName);
		final String displayField = UiUtil.getDisplayField(ctxKeyName);

		return "transformListForSelection('" + ctxKeyName + "', '" + idField + "', '" + displayField + "', null, null)";
	}

	public EasyFormsData getParametersForField(final EasyFormsTemplateField field) {
		final var fieldType = getFieldTypeByName(field.getFieldTypeName());
		return EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());
	}

}
