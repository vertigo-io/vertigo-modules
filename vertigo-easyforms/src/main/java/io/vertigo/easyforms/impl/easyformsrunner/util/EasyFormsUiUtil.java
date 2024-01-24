package io.vertigo.easyforms.impl.easyformsrunner.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.vertigo.core.node.Node;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.datamodel.structure.definitions.DtProperty;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate.Field;

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
			return Node.getNode().getDefinitionSpace().resolve(getFieldTypeByName(fieldTypeName).getSmartType(), SmartTypeDefinition.class)
					.getProperties().getValue(DtProperty.MAX_LENGTH);
		}
		return null;
	}

	public LinkedHashMap<String, String> getEasyForm(final EasyFormsTemplate easyFormsTemplate, final EasyFormsData easyForm) {
		final var easyFormDisplay = new LinkedHashMap<String, String>();
		final Map<String, String> outOfEasyFormTemplate = new HashMap<>(easyForm);

		for (final Field field : easyFormsTemplate.getFields()) { // order is important
			final var fieldCode = field.getFieldCode();
			easyFormDisplay.put(field.getLabel(), outOfEasyFormTemplate.get(fieldCode));
			outOfEasyFormTemplate.remove(fieldCode);
		}
		for (final Entry<String, String> champ : outOfEasyFormTemplate.entrySet()) {
			easyFormDisplay.put(champ.getKey() + " (old)", champ.getValue());
		}
		return easyFormDisplay;
	}

}
