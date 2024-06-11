package io.vertigo.easyforms.impl.runner.pack.provider.fieldtype;

import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.RadioCheckUiComponent;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public class CustomListFieldType implements IEasyFormsFieldTypeDefinitionSupplier {

	private final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent;

	public CustomListFieldType(final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
		this.uiComponent = uiComponent;
	}

	@Override
	public EasyFormsSmartTypes getSmartType() {
		return EasyFormsSmartTypes.EfLabel;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return uiComponent;
	}

	@Override
	public Map<String, Object> getUiParams() {
		if (uiComponent == UiComponentEnum.RADIO || uiComponent == UiComponentEnum.CHECKBOX) {
			return Map.of(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
					RadioCheckUiComponent.LAYOUT, "horizontal");
		}
		return Map.of(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME);
	}

	@Override
	public List<AbstractEasyFormsTemplateItem> getExposedComponentParams() {
		if (uiComponent == UiComponentEnum.RADIO || uiComponent == UiComponentEnum.CHECKBOX) {
			return List.of(
					new EasyFormsTemplateItemField(RadioCheckUiComponent.LAYOUT, FieldTypeEnum.INTERNAL_LAYOUT)
							.withParameters(Map.of(RadioCheckUiComponent.LAYOUT, "horizontal")),
					new EasyFormsTemplateItemField(IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME, FieldTypeEnum.INTERNAL_MAP).withMandatory());
		}
		return List.of(new EasyFormsTemplateItemField(IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME, FieldTypeEnum.INTERNAL_MAP).withMandatory());
	}

	@Override
	public boolean isList() {
		return uiComponent == UiComponentEnum.CHECKBOX;
	}

}