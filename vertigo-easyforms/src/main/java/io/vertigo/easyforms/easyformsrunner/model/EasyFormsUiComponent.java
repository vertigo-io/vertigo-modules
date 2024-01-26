package io.vertigo.easyforms.easyformsrunner.model;

import java.util.List;

import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;

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

	/**
	 * Helper class.
	 */
	public static record UiComponentParam(
			String fieldCode,
			EnumDefinition<EasyFormsFieldType, ?> fieldTypeEnum,
			String tooltip,
			boolean isMandatory,
			List<String> fieldValidators) {

		public UiComponentParam(final String fieldCode, final EnumDefinition<EasyFormsFieldType, ?> fieldTypeEnum, final String tooltip, final boolean isMandatory) {
			this(fieldCode, fieldTypeEnum, tooltip, isMandatory, List.of());
		}

	}

}
