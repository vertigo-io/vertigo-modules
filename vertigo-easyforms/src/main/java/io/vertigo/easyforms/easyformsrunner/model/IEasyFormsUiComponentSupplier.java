package io.vertigo.easyforms.easyformsrunner.model;

import java.util.List;

import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate.Field;

@FunctionalInterface
public interface IEasyFormsUiComponentSupplier {

	public static final IEasyFormsUiComponentSupplier NO_PARAM = List::of;

	public static final String LIST_SUPPLIER = "uiListSupplier";
	public static final String CUSTOM_LIST_ARG_NAME = "customList";
	public static final Field LIST_SUPPLIER_FIELD_PARAM = new Field(LIST_SUPPLIER, "").withMandatory();

	public static final String LIST_SUPPLIER_REF_PREFIX = "ref:";
	public static final String LIST_SUPPLIER_CTX_PREFIX = "ctx:";
	public static final String LIST_SUPPLIER_REF_CTX_NAME_PREFIX = "efoMdl";

	public default EasyFormsUiComponent get(final String definitionName) {
		final var uiComponentParams = getUiComponentParams();

		if (uiComponentParams == null || uiComponentParams.isEmpty()) {
			return EasyFormsUiComponent.of(definitionName, null);
		}

		var i = 0;
		for (final var uiComponentParam : uiComponentParams) {
			uiComponentParam
					.withOrder(i)
					.withLabel(definitionName + '$' + uiComponentParam.getCode() + "Label");
			i++;
		}

		return EasyFormsUiComponent.of(definitionName, new EasyFormsTemplate(uiComponentParams));
	}

	public abstract List<Field> getUiComponentParams();

	public static String getMdlSupplier(final Class<? extends Entity> clazz) {
		return LIST_SUPPLIER_REF_PREFIX + clazz.getSimpleName();
	}

	public static String getCtxSupplier(final String ctxName) {
		return LIST_SUPPLIER_CTX_PREFIX + ctxName;
	}

}
