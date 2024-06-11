package io.vertigo.easyforms.impl.runner.pack.provider.uicomponent;

import java.util.List;

import io.vertigo.easyforms.impl.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public class RadioCheckUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

	public static final String LAYOUT = "layout";

	@Override
	public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
		return List.of(
				IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_FIELD_PARAM,
				new EasyFormsTemplateItemField(LAYOUT, FieldTypeEnum.INTERNAL_LAYOUT));
	}

}