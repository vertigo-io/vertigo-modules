package io.vertigo.easyforms.impl.runner.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;

import io.vertigo.core.lang.Assertion;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.data.util.DataModelUtil;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.impl.runner.util.EasyFormsUiUtil;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.services.IEasyFormsRunnerServices;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;
import io.vertigo.ui.impl.springmvc.util.UiUtil;
import io.vertigo.vega.webservice.model.UiList;

@Controller
public final class EasyFormsRunnerController {

	@Inject
	private IEasyFormsRunnerServices easyFormsRunnerServices;

	@Inject
	private EasyFormsRunnerManager easyFormsRunnerManager;

	private static final ViewContextKey<EasyFormsUiUtil> efoUiUtilKey = ViewContextKey.of("efoUiUtil");

	private static final ViewContextKey<ArrayList<String>> efoSupportedLang = ViewContextKey.of("efoAllLang");

	public void initReadContext(final ViewContext viewContext, final UID<EasyForm> efoUid, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		final var easyForm = easyFormsRunnerServices.getEasyFormById(efoUid);

		final ArrayList<String> supportedLang = new ArrayList<>(easyFormsRunnerManager.getSupportedLang()); // needed to be ArrayList to be serializable

		viewContext.publishRef(efoUiUtilKey, easyFormsUiUtil)
				.publishRef(templateKey, easyForm.getTemplate())
				.publishRef(efoSupportedLang, supportedLang);

		addRequiredContext(viewContext, easyForm, false);
	}

	public void initMultipleReadContext(final ViewContext viewContext, final Collection<UID<EasyForm>> efoUidList, final ViewContextKey<ArrayList<EasyFormsTemplate>> templateKey) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		final var easyForms = easyFormsRunnerServices.getEasyFormListByIds(efoUidList);
		final var templates = easyForms.stream()
				.map(EasyForm::getTemplate)
				.toList();

		final ArrayList<String> supportedLang = new ArrayList<>(easyFormsRunnerManager.getSupportedLang()); // needed to be ArrayList to be serializable

		viewContext.publishRef(efoUiUtilKey, easyFormsUiUtil)
				.publishRef(templateKey, new ArrayList<>(templates))
				.publishRef(efoSupportedLang, supportedLang);

		for (final var easyForm : easyForms) {
			addRequiredContext(viewContext, easyForm, false);
		}
	}

	public void initEditContext(final ViewContext viewContext, final UID<EasyForm> efoUid, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		final var easyForm = easyFormsRunnerServices.getEasyFormById(efoUid);

		final ArrayList<String> supportedLang = new ArrayList<>(easyFormsRunnerManager.getSupportedLang()); // needed to be ArrayList to be serializable

		viewContext.publishRef(efoUiUtilKey, easyFormsUiUtil)
				.publishRef(templateKey, easyForm.getTemplate())
				.publishRef(efoSupportedLang, supportedLang);

		// Add master data list needed for fields to context
		addRequiredContext(viewContext, easyForm, true);
	}

	private void addRequiredContext(final ViewContext viewContext, final EasyForm easyForm, final boolean pushToFront) {
		final var easyFormsUiUtil = new EasyFormsUiUtil();
		final Set<String> listSuppliers = easyForm.getTemplate().getSections().stream()
				.flatMap(s -> easyFormsRunnerServices.getAllFieldsFromSection(s).stream())
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

		return easyFormsRunnerServices.getDefaultDataValues(easyFormsTemplate);
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

		easyFormsRunnerServices.formatAndCheckFormulaire(formOwner, formDataAccessor.apply(formOwner), easyFormsTemplate, UiRequestUtil.obtainCurrentUiMessageStack());
	}
}
