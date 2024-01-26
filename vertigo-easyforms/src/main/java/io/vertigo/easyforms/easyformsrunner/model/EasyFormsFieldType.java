package io.vertigo.easyforms.easyformsrunner.model;

import java.io.Serializable;
import java.util.Map;

import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;

@DefinitionPrefix(EasyFormsFieldType.PREFIX)
public class EasyFormsFieldType extends AbstractDefinition<EasyFormsFieldType> {

	public static final String PREFIX = "EfFty";

	private final String smartTypeName;
	private final String uiComponentName;
	private final Map<String, Serializable> uiParameters; // configure uiComponent
	private final EasyFormsTemplate paramTemplate; // expose parameters to designer UI

	private EasyFormsFieldType(final String name, final String smartTypeName, final String uiComponentName, final Map<String, Serializable> uiParameters, final EasyFormsTemplate paramTemplate) {
		super(name);
		//---
		this.smartTypeName = smartTypeName;
		this.uiComponentName = uiComponentName;
		this.uiParameters = uiParameters;
		this.paramTemplate = paramTemplate;
	}

	public static EasyFormsFieldType of(final String name, final String smartTypeName, final String uiComponentName) {
		return new EasyFormsFieldType(name, SmartTypeDefinition.PREFIX + smartTypeName, uiComponentName, Map.of(), null);
	}

	public static EasyFormsFieldType of(final String name, final String smartTypeName, final String uiComponentName, final Map<String, Serializable> uiParameters) {
		return new EasyFormsFieldType(name, SmartTypeDefinition.PREFIX + smartTypeName, uiComponentName, uiParameters, null);
	}

	public static EasyFormsFieldType of(final String name, final String smartTypeName, final String uiComponentName, final Map<String, Serializable> uiParameters,
			final EasyFormsTemplate paramTemplate) {
		return new EasyFormsFieldType(name, SmartTypeDefinition.PREFIX + smartTypeName, uiComponentName, uiParameters, paramTemplate);
	}

	public static EasyFormsFieldType resolve(final String name) {
		return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsFieldType.class);
	}

	public String getSmartTypeName() {
		return smartTypeName;
	}

	public String getUiComponentName() {
		return uiComponentName;
	}

	public Map<String, Serializable> getUiParameters() {
		return uiParameters;
	}

	public EasyFormsTemplate getParamTemplate() {
		return paramTemplate;
	}

	public String getLabel() {
		return StringUtil.camelToConstCase(getName()) + "_LABEL";
	}

}
