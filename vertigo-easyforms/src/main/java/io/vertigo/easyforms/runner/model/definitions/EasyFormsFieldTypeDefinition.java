package io.vertigo.easyforms.runner.model.definitions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

@DefinitionPrefix(EasyFormsFieldTypeDefinition.PREFIX)
public final class EasyFormsFieldTypeDefinition extends AbstractDefinition<EasyFormsFieldTypeDefinition> {

	public static final String PREFIX = "EfFty";

	private final String category;
	private final String smartTypeName;
	private final String uiComponentName;
	private final Object defaultValue;
	private final EasyFormsData uiParameters; // configure uiComponent
	private final EasyFormsTemplate paramTemplate; // expose parameters to designer UI
	private final boolean isList;
	private final Function<EasyFormsTemplateItemField, List<Constraint>> constraintProviders;

	private EasyFormsFieldTypeDefinition(final String name, final String category, final String smartTypeName, final String uiComponentName, final Object defaultValue,
			final Map<String, Object> uiParameters,
			final EasyFormsTemplate paramTemplate, final boolean isList, final Function<EasyFormsTemplateItemField, List<Constraint>> constraintProviders) {
		super(name);
		//---
		this.category = category;
		this.smartTypeName = smartTypeName;
		this.uiComponentName = uiComponentName;
		this.defaultValue = defaultValue;
		this.uiParameters = new EasyFormsData(uiParameters);
		this.paramTemplate = paramTemplate;
		this.isList = isList;
		this.constraintProviders = constraintProviders;
	}

	public static EasyFormsFieldTypeDefinition of(final String name, final String category, final String smartTypeName, final String uiComponentName, final Object defaultValue,
			final Map<String, Object> uiParameters, final EasyFormsTemplate paramTemplate, final boolean isList, final Function<EasyFormsTemplateItemField, List<Constraint>> constraintProviders) {
		return new EasyFormsFieldTypeDefinition(name, category, SmartTypeDefinition.PREFIX + smartTypeName, uiComponentName, defaultValue, uiParameters, paramTemplate, isList, constraintProviders);
	}

	public static EasyFormsFieldTypeDefinition resolve(final String name) {
		return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsFieldTypeDefinition.class);
	}

	public String getCategory() {
		return category;
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

	public boolean isList() {
		return isList;
	}

	public List<Constraint> getConstraints(final EasyFormsTemplateItemField value) {
		if (constraintProviders == null) {
			return List.of();
		}
		return constraintProviders.apply(value);
	}

}
