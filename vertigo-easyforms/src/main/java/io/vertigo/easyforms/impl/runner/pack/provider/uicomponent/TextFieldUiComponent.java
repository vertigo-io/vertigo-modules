package io.vertigo.easyforms.impl.runner.pack.provider.uicomponent;

import java.util.List;

import io.vertigo.easyforms.impl.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public class TextFieldUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

	public static final String AUTOCOMPLETE = "textFieldAutocomplete";

	@Override
	public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
		return List.of(
				new EasyFormsTemplateItemField(AUTOCOMPLETE, FieldTypeEnum.CUSTOM_LIST_RADIO));
	}

}