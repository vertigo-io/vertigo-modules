package io.vertigo.easyforms.impl.runner.pack.provider.uicomponent;

import java.util.List;

import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.easyforms.impl.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public class TextAreaUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

	public static final String AUTOGROW = "autogrow";

	@Override
	public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
		return List.of(
				new EasyFormsTemplateItemField(DtProperty.MAX_LENGTH.getName(), FieldTypeEnum.COUNT_STRICT),
				new EasyFormsTemplateItemField(AUTOGROW, FieldTypeEnum.YES_NO));
	}

}