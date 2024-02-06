package io.vertigo.easyforms.easyformsrunner.model.definitions;

import java.util.Map;

import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;

@DefinitionPrefix(EasyFormsFieldType.PREFIX)
public class EasyFormsFieldType extends AbstractDefinition<EasyFormsFieldType> {

	public static final String PREFIX = "EfFty";

	private final String smartTypeName;
	private final String uiComponentName;
	private final Object defaultValue;
	private final EasyFormsData uiParameters; // configure uiComponent
	private final EasyFormsTemplate paramTemplate; // expose parameters to designer UI

	private EasyFormsFieldType(final String name, final String smartTypeName, final String uiComponentName, final Object defaultValue, final Map<String, Object> uiParameters,
			final EasyFormsTemplate paramTemplate) {
		super(name);
		//---
		this.smartTypeName = smartTypeName;
		this.uiComponentName = uiComponentName;
		this.defaultValue = defaultValue;
		this.uiParameters = new EasyFormsData(uiParameters);
		this.paramTemplate = paramTemplate;
	}

	public static EasyFormsFieldType of(final String name, final String smartTypeName, final String uiComponentName, final Object defaultValue, final Map<String, Object> uiParameters,
			final EasyFormsTemplate paramTemplate) {
		return new EasyFormsFieldType(name, SmartTypeDefinition.PREFIX + smartTypeName, uiComponentName, defaultValue, uiParameters, paramTemplate);
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

	public Object getDefaultValue() {
		return defaultValue;
	}

	public EasyFormsData getUiParameters() {
		return uiParameters;
	}

	public EasyFormsTemplate getParamTemplate() {
		return paramTemplate;
	}

	public String getLabel() {
		return LocaleMessageText.of(() -> getName() + "Label").getDisplay();
	}

}
