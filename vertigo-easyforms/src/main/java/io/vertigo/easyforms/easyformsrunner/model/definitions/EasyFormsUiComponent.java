package io.vertigo.easyforms.easyformsrunner.model.definitions;

import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;

@DefinitionPrefix(EasyFormsUiComponent.PREFIX)
public class EasyFormsUiComponent extends AbstractDefinition<EasyFormsUiComponent> {

	public static final String PREFIX = "EfUic";

	/**
	 * Theses parameters are not exposed by default in the UI.
	 * FieldType MUST copy from this template what he wants to expose.
	 */
	private final EasyFormsTemplate parameters;

	private EasyFormsUiComponent(final String name, final EasyFormsTemplate parameters) {
		super(name);
		//---
		this.parameters = parameters;
	}

	public static EasyFormsUiComponent of(final String name, final EasyFormsTemplate parameters) {
		return new EasyFormsUiComponent(name, parameters);
	}

	public static EasyFormsUiComponent resolve(final String name) {
		return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsUiComponent.class);
	}

	public EasyFormsTemplate getParameters() {
		return parameters;
	}

}
