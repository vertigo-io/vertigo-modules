package io.vertigo.easyforms.easyformsrunner.model;

import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.datamodel.structure.model.DtObject;

@DefinitionPrefix(EasyFormsFieldType.PREFIX)
public class EasyFormsFieldType extends AbstractDefinition<EasyFormsFieldType> {

	public static final String PREFIX = "EfTch";

	private final String label;
	private final String smartType;
	private final String uiComponent;
	private final String uiAutocompleteInputAttribute;
	/**
	 * Specify list name of possible values (actual values may be in a sub list)
	 */
	private final String listName;

	private EasyFormsFieldType(final String nom, final String label, final String smartType, final String uiComponent, final String uiAutocompleteInputAttribute) {
		this(nom, label, smartType, uiComponent, uiAutocompleteInputAttribute, null);
	}

	private EasyFormsFieldType(final String nom, final String label, final String smartType, final String uiComponent, final String uiAutocompleteInputAttribute, final String listName) {
		super(PREFIX + nom);
		//---
		this.label = label;
		this.smartType = smartType;
		this.uiComponent = uiComponent;
		this.uiAutocompleteInputAttribute = uiAutocompleteInputAttribute;
		this.listName = listName;
	}

	public static EasyFormsFieldType of(final String nom, final String label, final String smartType, final String uiComponent, final String uiAutocompleteInputAttribute) {
		return new EasyFormsFieldType(nom, label, "STy" + smartType, uiComponent, uiAutocompleteInputAttribute);
	}

	public static EasyFormsFieldType of(final String nom, final String label, final String smartType, final String uiComponent, final String uiAutocompleteInputAttribute,
			final Class<? extends DtObject> listClass) {
		return new EasyFormsFieldType(nom, label, "STy" + smartType, uiComponent, uiAutocompleteInputAttribute, listClass == null ? null : listClass.getSimpleName());
	}

	public static EasyFormsFieldType resolve(final String nom) {
		if (nom.startsWith(PREFIX)) {
			return Node.getNode().getDefinitionSpace().resolve(nom, EasyFormsFieldType.class);
		}
		return Node.getNode().getDefinitionSpace().resolve(PREFIX + nom, EasyFormsFieldType.class);
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

	public boolean isList() {
		return listName != null;
	}

	public boolean isListOfType(final String classSimpleName) {
		return isList() && listName.equals(classSimpleName);
	}

}
