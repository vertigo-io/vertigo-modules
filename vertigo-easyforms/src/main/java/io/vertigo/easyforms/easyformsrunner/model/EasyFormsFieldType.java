package io.vertigo.easyforms.easyformsrunner.model;

import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;

@DefinitionPrefix(EasyFormsFieldType.PREFIX)
public class EasyFormsFieldType extends AbstractDefinition<EasyFormsFieldType> {

	public static final String PREFIX = "EfFty";

	private final String label;
	private final String smartType;
	private final String uiComponent;
	private final String uiAutocompleteInputAttribute;

	private EasyFormsFieldType(final String name, final String label, final String smartType, final String uiComponent, final String uiAutocompleteInputAttribute) {
		super(PREFIX + name);
		//---
		this.label = label;
		this.smartType = smartType;
		this.uiComponent = uiComponent;
		this.uiAutocompleteInputAttribute = uiAutocompleteInputAttribute;
	}

	public static EasyFormsFieldType of(final String name, final String label, final String smartType, final String uiComponent, final String uiAutocompleteInputAttribute) {
		return new EasyFormsFieldType(name, label, SmartTypeDefinition.PREFIX + smartType, uiComponent, uiAutocompleteInputAttribute);
	}

	public static EasyFormsFieldType resolve(final String name) {
		if (name.startsWith(PREFIX)) {
			return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsFieldType.class);
		}
		return Node.getNode().getDefinitionSpace().resolve(PREFIX + name, EasyFormsFieldType.class);
	}

	public String getLabel() {
		return label;
	}

	public String getSmartType() {
		return smartType;
	}

	public String getUiComponent() {
		return uiComponent;
	}

	public String getUiAutocompleteInputAttribute() {
		return uiAutocompleteInputAttribute;
	}

}
