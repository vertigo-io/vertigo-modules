package io.vertigo.easyforms.impl.runner.util;

import java.util.Set;
import java.util.stream.Collectors;

import io.vertigo.core.lang.Assertion;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.util.DataModelUtil;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.util.UiUtil;
import io.vertigo.vega.webservice.model.UiList;

public class EasyFormsControllerUtil {

	private EasyFormsControllerUtil() {
		// Utility class
	}

	public static void addRequiredContext(final ViewContext viewContext, final EasyFormsTemplate easyFormsTemplate, final boolean pushToFront) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		final Set<String> listSuppliers = easyFormsTemplate.getSections().stream()
				.flatMap(s -> s.getAllFields().stream())
				.map(easyFormsUiUtil::getParametersForField)
				.flatMap(p -> p.entrySet().stream()) // stream all parameters for all fields
				.filter(p -> IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER.equals(p.getKey())) // get custom list configuration
				.map(p -> p.getValue().toString())
				.collect(Collectors.toUnmodifiableSet()); // get distinct values

		// Handle master data lists (add to context and push to front context)
		listSuppliers.stream()
				.filter(p -> p.startsWith(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_REF_PREFIX))
				.map(p -> p.substring(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_REF_PREFIX.length())) // get Mda class name
				.forEach(mdlClass -> {
					// add to back context
					final var ctxKey = ViewContextKey.<Entity>of(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_REF_CTX_NAME_PREFIX + mdlClass);
					viewContext.publishMdl(ctxKey, DataModelUtil.findDataDefinition(mdlClass), null);
					if (pushToFront) {
						// add to front context
						addListToFrontCtx(viewContext, ctxKey.get());
					}
				});

		if (pushToFront) {
			// Force context references to be pushed to front context
			listSuppliers.stream()
					.filter(p -> p.startsWith(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_CTX_PREFIX))
					.map(p -> p.substring(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_CTX_PREFIX.length())) // get ctx name
					.forEach(ctxKey -> {
						Assertion.check()
								.isTrue(viewContext.containsKey(ctxKey), "Context key '{0}' not found.", ctxKey)
								.isTrue(viewContext.get(ctxKey) instanceof UiList, "Context key '{0}' is not a list.", ctxKey);
						addListToFrontCtx(viewContext, ctxKey);
					});
		}
	}

	private static void addListToFrontCtx(final ViewContext viewContext, final String ctxKey) {
		viewContext.asMap().addKeyForClient(ctxKey, UiUtil.getIdField(ctxKey), null, false); // expose key attribute
		viewContext.asMap().addKeyForClient(ctxKey, UiUtil.getDisplayField(ctxKey), null, false); // expose display attribute
	}

}
