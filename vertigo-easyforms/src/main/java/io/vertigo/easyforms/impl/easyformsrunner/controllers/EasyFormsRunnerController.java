package io.vertigo.easyforms.impl.easyformsrunner.controllers;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.data.util.DataModelUtil;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.definitions.IEasyFormsUiComponentSupplier;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.impl.easyformsdesigner.services.EasyFormsDesignerServices;
import io.vertigo.easyforms.impl.easyformsrunner.util.EasyFormsUiUtil;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.util.UiUtil;
import io.vertigo.vega.webservice.model.UiList;

@Controller
public class EasyFormsRunnerController {

	@Inject
	private EasyFormsDesignerServices easyFormsDesignerServices;

	private static final ViewContextKey<EasyFormsUiUtil> efoUiUtilKey = ViewContextKey.of("efoUiUtil");

	public void initReadContext(final ViewContext viewContext, final UID<EasyForm> efoUid, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		viewContext.publishRef(efoUiUtilKey, easyFormsUiUtil);

		final var easyForm = easyFormsDesignerServices.getEasyFormById(efoUid);
		viewContext.publishRef(templateKey, easyForm.getTemplate());

		addRequiredContext(viewContext, easyForm, false);
	}

	public void initEditContext(final ViewContext viewContext, final UID<EasyForm> efoUid, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		viewContext.publishRef(efoUiUtilKey, easyFormsUiUtil);

		final var easyForm = easyFormsDesignerServices.getEasyFormById(efoUid);

		viewContext.publishRef(templateKey, easyForm.getTemplate());

		// Add master data list needed for fields to context
		addRequiredContext(viewContext, easyForm, true);
	}

	private void addRequiredContext(final ViewContext viewContext, final EasyForm easyForm, final boolean pushToFront) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		final Set<String> listSuppliers = easyForm.getTemplate().getFields().stream()
				.map(easyFormsUiUtil::getParametersForField)
				.flatMap(p -> p.entrySet().stream()) // stream all parameters for all fields
				.filter(p -> IEasyFormsUiComponentSupplier.LIST_SUPPLIER.equals(p.getKey())) // get custom list configuration
				.map(p -> p.getValue().toString())
				.collect(Collectors.toUnmodifiableSet()); // get distinct values

		// Handle master data lists (add to context and push to front context)
		listSuppliers.stream()
				.filter(p -> p.startsWith(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_REF_PREFIX))
				.map(p -> p.substring(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_REF_PREFIX.length())) // get Mda class name
				.forEach(mdlClass -> {
					// add to back context
					final var ctxKey = ViewContextKey.<Entity>of(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_REF_CTX_NAME_PREFIX + mdlClass);
					viewContext.publishMdl(ctxKey, DataModelUtil.findDataDefinition(mdlClass), null);
					if (pushToFront) {
						// add to front context
						addListToFrontCtx(viewContext, ctxKey.get());
					}
				});

		if (pushToFront) {
			// Force context references to be pushed to front context
			listSuppliers.stream()
					.filter(p -> p.startsWith(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_CTX_PREFIX))
					.map(p -> p.substring(IEasyFormsUiComponentSupplier.LIST_SUPPLIER_CTX_PREFIX.length())) // get ctx name
					.forEach(ctxKey -> {
						Assertion.check()
								.isTrue(viewContext.containsKey(ctxKey), "Context key '{0}' not found.", ctxKey)
								.isTrue(viewContext.get(ctxKey) instanceof UiList, "Context key '{0}' is not a list.", ctxKey);
						addListToFrontCtx(viewContext, ctxKey);
					});
		}
	}

	/**
	 * Get default template values. initReadContext or initEditContext must have been previously called.
	 *
	 * @param viewContext viewContext
	 * @param templateKey ViewContextKey where the template will be published
	 */
	public EasyFormsData getDefaultDataValues(final ViewContext viewContext, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final EasyFormsTemplate easyFormsTemplate = (EasyFormsTemplate) viewContext.get(templateKey.get());

		final var fieldTypeParameters = new EasyFormsData();

		for (final var paramField : easyFormsTemplate.getFields()) {
			final var paramFieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(paramField.getFieldTypeName(), EasyFormsFieldType.class);
			if (paramFieldTypeDefinition.getDefaultValue() != null) {
				fieldTypeParameters.put(paramField.getCode(), paramFieldTypeDefinition.getDefaultValue());
			}
		}
		return fieldTypeParameters;
	}

	private void addListToFrontCtx(final ViewContext viewContext, final String ctxKey) {
		viewContext.asMap().addKeyForClient(ctxKey, UiUtil.getIdField(ctxKey), null, false); // expose key attribute
		viewContext.asMap().addKeyForClient(ctxKey, UiUtil.getDisplayField(ctxKey), null, false); // expose display attribute
	}
}
