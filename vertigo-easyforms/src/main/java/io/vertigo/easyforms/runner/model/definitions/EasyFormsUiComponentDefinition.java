package io.vertigo.easyforms.runner.model.definitions;

import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;

@DefinitionPrefix(EasyFormsUiComponentDefinition.PREFIX)
public class EasyFormsUiComponentDefinition extends AbstractDefinition<EasyFormsUiComponentDefinition> {

	public static final String PREFIX = "EfUic";

	/**
	 * Theses parameters are not exposed by default in the UI.
	 * FieldType MUST copy from this template what he wants to expose.
	 */
	private final EasyFormsTemplate parameters;

	private EasyFormsUiComponentDefinition(final String name, final EasyFormsTemplate parameters) {
		super(name);
		//---
		this.parameters = parameters;
	}

	public static EasyFormsUiComponentDefinition of(final String name, final EasyFormsTemplate parameters) {
		return new EasyFormsUiComponentDefinition(name, parameters);
	}

	public static EasyFormsUiComponentDefinition resolve(final String name) {
		return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsUiComponentDefinition.class);
	}

	public EasyFormsTemplate getParameters() {
		return parameters;
	}

}
