package io.vertigo.easyforms.impl.runner.pack.provider.uicomponent;

import java.util.List;

import io.vertigo.easyforms.impl.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public class FileUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

	public static final String MAX_SIZE = "maxSize";
	public static final String MAX_FILE_SIZE = "maxFileSize";
	public static final String ACCEPT = "accept";

	@Override
	public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
		return List.of(
				new EasyFormsTemplateItemField(MAX_SIZE, FieldTypeEnum.COUNT_STRICT),
				new EasyFormsTemplateItemField(ACCEPT, FieldTypeEnum.INTERNAL_EXTENSIONS));
	}

}