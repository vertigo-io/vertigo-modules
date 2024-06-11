package io.vertigo.easyforms.impl.runner.pack.provider.fieldtype;

import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.TextFieldUiComponent;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;

public class AutocompleteFieldType implements IEasyFormsFieldTypeDefinitionSupplier {
	private final EasyFormsSmartTypes smartType;
	private final String uiAutocompleteInputAttribute;

	public AutocompleteFieldType(final EasyFormsSmartTypes smartType, final String uiAutocompleteInputAttribute) {
		this.smartType = smartType;
		this.uiAutocompleteInputAttribute = uiAutocompleteInputAttribute;
	}

	@Override
	public EasyFormsSmartTypes getSmartType() {
		return smartType;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return UiComponentEnum.TEXT_FIELD;
	}

	@Override
	public Map<String, Object> getUiParams() {
		return Map.of(TextFieldUiComponent.AUTOCOMPLETE, uiAutocompleteInputAttribute);
	}
}