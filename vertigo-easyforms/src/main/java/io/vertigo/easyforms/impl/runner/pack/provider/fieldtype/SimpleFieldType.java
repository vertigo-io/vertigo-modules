package io.vertigo.easyforms.impl.runner.pack.provider.fieldtype;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;

public class SimpleFieldType implements IEasyFormsFieldTypeDefinitionSupplier {
	private final String category;
	private final EasyFormsSmartTypes smartType;
	private final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent;

	public SimpleFieldType(final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
		this(DEFAULT_CATEGORY, smartType, uiComponent);
	}

	public SimpleFieldType(final String category, final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
		this.category = category;
		this.smartType = smartType;
		this.uiComponent = uiComponent;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public EasyFormsSmartTypes getSmartType() {
		return smartType;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return uiComponent;
	}
}