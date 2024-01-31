package io.vertigo.easyforms.impl.easyformsrunner.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.vertigo.core.node.Node;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.datamodel.structure.definitions.DtProperty;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsListItem;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate.Field;
import io.vertigo.easyforms.easyformsrunner.model.IEasyFormsUiComponentSupplier;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;
import io.vertigo.vega.webservice.model.UiList;

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

	public List<EasyFormsListItem> getDynamicList(final EasyFormsTemplate easyFormsTemplate, final String fieldCode) {
		final var field = easyFormsTemplate.getFields().stream()
				.filter(f -> f.getCode().equals(fieldCode))
				.findFirst().orElseThrow();

		final var fieldType = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldType.class);

		final var resolvedParameters = new HashMap<String, Object>();
		if (fieldType.getUiParameters() != null) {
			resolvedParameters.putAll(fieldType.getUiParameters());
		}
		if (field.getParameters() != null) {
			resolvedParameters.putAll(field.getParameters());
		}

		final String listSupplier = (String) resolvedParameters.get(IEasyFormsUiComponentSupplier.LIST_SUPPLIER);

		if (listSupplier.startsWith("ref:")) {
			// TODO : Liste de ref
		} else if (listSupplier.startsWith("ctx:")) {
			final var ctxName = listSupplier.substring(4);
			final var viewContext = UiRequestUtil.getCurrentViewContext();
			final UiList<?> list = (UiList<?>) viewContext.get(ctxName);
			// TODO
		} else {
			return EasyFormsListItem.ofCollection(resolvedParameters.getOrDefault(listSupplier, List.of()));
		}

		return List.of(new EasyFormsListItem("TODO", "Todo")); // TODO, gérer les cas de list supplier built in (Map en dur "customList", liste de ref prefix "ref:" ?, liste du contexte prefixe "ctx:" ?)
	}

}
