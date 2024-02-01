package io.vertigo.easyforms.impl.easyformsrunner.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.vertigo.core.node.Node;
import io.vertigo.datamodel.data.definitions.DtProperty;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsListItem;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsParameterData;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate.Field;
import io.vertigo.easyforms.easyformsrunner.model.IEasyFormsUiComponentSupplier;
import io.vertigo.ui.impl.springmvc.util.UiUtil;

public class EasyFormsUiUtil implements Serializable {

	private static final long serialVersionUID = 1L;

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

	public LinkedHashMap<String, String> getEasyForm(final EasyFormsTemplate easyFormsTemplate, final EasyFormsData easyForm) {
		final var easyFormDisplay = new LinkedHashMap<String, String>();
		final Map<String, String> outOfEasyFormTemplate = new HashMap<>(easyForm);

		for (final Field field : easyFormsTemplate.getFields()) { // order is important
			final var fieldCode = field.getCode();
			easyFormDisplay.put(field.getLabel(), outOfEasyFormTemplate.get(fieldCode));
			outOfEasyFormTemplate.remove(fieldCode);
		}
		for (final Entry<String, String> champ : outOfEasyFormTemplate.entrySet()) {
			easyFormDisplay.put(champ.getKey() + " (old)", champ.getValue());
		}
		return easyFormDisplay;
	}

	public String getDynamicListForField(final Field field) {
		final var fieldType = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldType.class);

		final var resolvedParameters = EasyFormsParameterData.combine(fieldType.getUiParameters(), field.getParameters());

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

	public EasyFormsParameterData getParametersForField(final Field field) {
		final var fieldType = getFieldTypeByName(field.getFieldTypeName());
		return EasyFormsParameterData.combine(fieldType.getUiParameters(), field.getParameters());
	}

}
