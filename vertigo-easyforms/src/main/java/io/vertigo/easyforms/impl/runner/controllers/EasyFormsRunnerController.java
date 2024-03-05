package io.vertigo.easyforms.impl.runner.controllers;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.data.util.DataModelUtil;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.impl.runner.services.EasyFormsRunnerServices;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.impl.runner.util.EasyFormsUiUtil;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;
import io.vertigo.ui.impl.springmvc.util.UiUtil;
import io.vertigo.vega.webservice.model.UiList;

@Controller
public class EasyFormsRunnerController {

	@Inject
	private EasyFormsRunnerServices easyFormsRunnerServices;

	private static final ViewContextKey<EasyFormsUiUtil> efoUiUtilKey = ViewContextKey.of("efoUiUtil");

	public void initReadContext(final ViewContext viewContext, final UID<EasyForm> efoUid, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		viewContext.publishRef(efoUiUtilKey, easyFormsUiUtil);

		final var easyForm = easyFormsRunnerServices.getEasyFormById(efoUid);
		viewContext.publishRef(templateKey, easyForm.getTemplate());

		addRequiredContext(viewContext, easyForm, false);
	}

	public void initEditContext(final ViewContext viewContext, final UID<EasyForm> efoUid, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		viewContext.publishRef(efoUiUtilKey, easyFormsUiUtil);

		final var easyForm = easyFormsRunnerServices.getEasyFormById(efoUid);

		viewContext.publishRef(templateKey, easyForm.getTemplate());

		// Add master data list needed for fields to context
		addRequiredContext(viewContext, easyForm, true);
	}

	private void addRequiredContext(final ViewContext viewContext, final EasyForm easyForm, final boolean pushToFront) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		final Set<String> listSuppliers = easyForm.getTemplate().getFields().stream()
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

	/**
	 * Get default template values. initReadContext or initEditContext must have been previously called.
	 *
	 * @param viewContext viewContext
	 * @param templateKey ViewContextKey where the template is published
	 */
	public EasyFormsData getDefaultDataValues(final ViewContext viewContext, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final EasyFormsTemplate easyFormsTemplate = (EasyFormsTemplate) viewContext.get(templateKey.get());

		final var fieldTypeParameters = new EasyFormsData();

		for (final var paramField : easyFormsTemplate.getFields()) {
			final var paramFieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(paramField.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
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

	/**
	 * Validate EasyFormData
	 *
	 * @param viewContext viewContext
	 * @param templateKey ViewContextKey where the template is published
	 * @param formOwner entity holding the form
	 * @param formDataAccessor accessor to the form data on the formOwner
	 * @param <E> Entity type
	 */
	public <E extends Entity> void checkForm(final ViewContext viewContext, final ViewContextKey<EasyFormsTemplate> templateKey, final E formOwner, final Function<E, EasyFormsData> formDataAccessor) {
		final EasyFormsTemplate easyFormsTemplate = (EasyFormsTemplate) viewContext.get(templateKey.get());

		easyFormsRunnerServices.checkFormulaire(formOwner, formDataAccessor.apply(formOwner), easyFormsTemplate, UiRequestUtil.obtainCurrentUiMessageStack());
	}
}
