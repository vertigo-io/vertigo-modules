package io.vertigo.easyforms.impl.runner.pack.provider.fieldtype;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.RadioCheckUiComponent;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.ui.EasyFormsListItem;

public class YesNoFieldType implements IEasyFormsFieldTypeDefinitionSupplier {
	@Override
	public EasyFormsSmartTypes getSmartType() {
		return EasyFormsSmartTypes.EfBoolean;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return UiComponentEnum.RADIO;
	}

	@Override
	public Map<String, Object> getUiParams() {
		return Map.of(
				RadioCheckUiComponent.LAYOUT, RadioCheckUiComponent.VALUE_HORIZONTAL,
				IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
				IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
				(Serializable) List.of(
						new EasyFormsListItem(true, "#{EfTrue}"),
						new EasyFormsListItem(false, "#{EfFalse}")));
	}
}
