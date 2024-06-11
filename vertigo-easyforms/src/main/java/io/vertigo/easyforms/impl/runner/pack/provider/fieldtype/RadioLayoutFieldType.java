package io.vertigo.easyforms.impl.runner.pack.provider.fieldtype;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.ui.EasyFormsListItem;

public class RadioLayoutFieldType implements IEasyFormsFieldTypeDefinitionSupplier {

	@Override
	public String getCategory() {
		return null; // no category means "hidden"
	}

	@Override
	public EasyFormsSmartTypes getSmartType() {
		return EasyFormsSmartTypes.EfLabel;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return UiComponentEnum.RADIO;
	}

	@Override
	public Map<String, Object> getUiParams() {
		return Map.of(
				IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
				IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
				(Serializable) List.of(
						new EasyFormsListItem("", "#{EfFtyCustomListRadio$layoutVerticalLabel}"),
						new EasyFormsListItem("horizontal", "#{EfFtyCustomListRadio$layoutHorizontalLabel}")));
	}

	@Override
	public Object getDefaultValue() {
		return ""; // Vertical
	}

}