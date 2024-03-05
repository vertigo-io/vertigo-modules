package io.vertigo.easyforms.impl.runner.suppliers;

import java.util.List;

import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateField;

@FunctionalInterface
public interface IEasyFormsUiComponentDefinitionSupplier {

	public static final IEasyFormsUiComponentDefinitionSupplier NO_PARAM = List::of;

	public static final String LIST_SUPPLIER = "uiListSupplier";
	public static final String CUSTOM_LIST_ARG_NAME = "customList";
	public static final EasyFormsTemplateField LIST_SUPPLIER_FIELD_PARAM = new EasyFormsTemplateField(LIST_SUPPLIER, "").withMandatory();

	public static final String LIST_SUPPLIER_REF_PREFIX = "ref:";
	public static final String LIST_SUPPLIER_CTX_PREFIX = "ctx:";
	public static final String LIST_SUPPLIER_REF_CTX_NAME_PREFIX = "efoMdl";

	public default EasyFormsUiComponentDefinition get(final String definitionName) {
		final var uiComponentParams = getUiComponentParams();

		if (uiComponentParams == null || uiComponentParams.isEmpty()) {
			return EasyFormsUiComponentDefinition.of(definitionName, null);
		}

		for (final var uiComponentParam : uiComponentParams) {
			// default i18n label for ui component
			uiComponentParam
					.withLabel(definitionName + '$' + uiComponentParam.getCode() + "Label");
		}

		return EasyFormsUiComponentDefinition.of(definitionName, new EasyFormsTemplate(uiComponentParams));
	}

	public abstract List<EasyFormsTemplateField> getUiComponentParams();

	public static String getMdlSupplier(final Class<? extends Entity> clazz) {
		return LIST_SUPPLIER_REF_PREFIX + clazz.getSimpleName();
	}

	public static String getCtxSupplier(final String ctxName) {
		return LIST_SUPPLIER_CTX_PREFIX + ctxName;
	}

}
