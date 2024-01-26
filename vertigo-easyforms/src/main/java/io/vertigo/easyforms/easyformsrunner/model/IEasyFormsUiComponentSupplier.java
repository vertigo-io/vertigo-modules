package io.vertigo.easyforms.easyformsrunner.model;

import java.util.List;

import io.vertigo.easyforms.easyformsrunner.model.EasyFormsUiComponent.UiComponentParam;

@FunctionalInterface
public interface IEasyFormsUiComponentSupplier {

	public static final IEasyFormsUiComponentSupplier NO_PARAM = List::of;

	public static final String LIST_SUPPLIER = "uiListSupplier";
	public static final String CUSTOM_LIST_ARG_NAME = "customList";
	public static final UiComponentParam LIST_SUPPLIER_FIELD_PARAM = new UiComponentParam(LIST_SUPPLIER, null, null, true);

	public default EasyFormsUiComponent get(final String definitionName) {
		final var uiComponentParams = getUiComponentParams();

		if (uiComponentParams == null || uiComponentParams.isEmpty()) {
			return EasyFormsUiComponent.of(definitionName, null);
		}

		final var easyFormsTemplateBuilder = new EasyFormsTemplateBuilder();
		for (final var uiComponentParam : uiComponentParams) {
			easyFormsTemplateBuilder.addField(
					uiComponentParam.fieldCode(),
					uiComponentParam.fieldTypeEnum(),
					uiComponentParam.fieldCode(),
					uiComponentParam.tooltip(),
					true,
					uiComponentParam.isMandatory(),
					uiComponentParam.fieldValidators());
		}

		return EasyFormsUiComponent.of(definitionName, easyFormsTemplateBuilder.build());
	}

	public abstract List<UiComponentParam> getUiComponentParams();

}
