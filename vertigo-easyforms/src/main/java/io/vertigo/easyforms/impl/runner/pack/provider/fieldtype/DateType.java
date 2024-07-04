package io.vertigo.easyforms.impl.runner.pack.provider.fieldtype;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;

public class DateType implements IEasyFormsFieldTypeDefinitionSupplier {

	@Override
	public EasyFormsSmartTypes getSmartType() {
		return EasyFormsSmartTypes.EfDate;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return UiComponentEnum.DATE;
	}

}
