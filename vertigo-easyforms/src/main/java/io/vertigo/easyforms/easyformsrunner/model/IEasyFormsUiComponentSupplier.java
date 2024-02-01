package io.vertigo.easyforms.easyformsrunner.model;

import java.util.List;

import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsUiComponent.UiComponentParam;

@FunctionalInterface
public interface IEasyFormsUiComponentSupplier {

	public static final IEasyFormsUiComponentSupplier NO_PARAM = List::of;

	public static final String LIST_SUPPLIER = "uiListSupplier";
	public static final String CUSTOM_LIST_ARG_NAME = "customList";
	public static final UiComponentParam LIST_SUPPLIER_FIELD_PARAM = new UiComponentParam(LIST_SUPPLIER, null, null, true);

	public static final String LIST_SUPPLIER_REF_PREFIX = "ref:";
	public static final String LIST_SUPPLIER_CTX_PREFIX = "ctx:";
	public static final String LIST_SUPPLIER_REF_CTX_NAME_PREFIX = "efoMdl";

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
					StringUtil.camelToConstCase(definitionName) + '_' + StringUtil.camelToConstCase(uiComponentParam.fieldCode()) + "_LABEL",
					uiComponentParam.tooltip(),
					true,
					uiComponentParam.isMandatory(),
					null, uiComponentParam.fieldValidators());
		}

		return EasyFormsUiComponent.of(definitionName, easyFormsTemplateBuilder.build());
	}

	public abstract List<UiComponentParam> getUiComponentParams();

	public static String getMdlSupplier(final Class<? extends Entity> clazz) {
		return LIST_SUPPLIER_REF_PREFIX + clazz.getSimpleName();
	}

	public static String getCtxSupplier(final String ctxName) {
		return LIST_SUPPLIER_CTX_PREFIX + ctxName;
	}

}
