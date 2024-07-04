package io.vertigo.easyforms.impl.runner.controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;

import io.vertigo.datamodel.data.definitions.DataFieldName;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.impl.runner.util.EasyFormsControllerUtil;
import io.vertigo.easyforms.impl.runner.util.EasyFormsUiUtil;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.services.IEasyFormsRunnerServices;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;

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

		EasyFormsControllerUtil.addRequiredContext(viewContext, easyForm.getTemplate(), false);
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
			EasyFormsControllerUtil.addRequiredContext(viewContext, easyForm.getTemplate(), false);
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
		EasyFormsControllerUtil.addRequiredContext(viewContext, easyForm.getTemplate(), true);
	}

	/**
	 * Get default template values. initReadContext or initEditContext must have been previously called.
	 *
	 * @param viewContext viewContext
	 * @param templateKey ViewContextKey where the template is published
	 */
	public EasyFormsData getDefaultDataValues(final ViewContext viewContext, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final EasyFormsTemplate easyFormsTemplate = (EasyFormsTemplate) viewContext.get(templateKey.get());

		return easyFormsRunnerServices.getDefaultDataValues(easyFormsTemplate, viewContext.asMap());
	}

	/**
	 * Validate EasyFormData
	 *
	 * @param easyFormsTemplate template to check
	 * @param formOwner entity holding the form
	 * @param formDataAccessor accessor to the form data on the formOwner
	 * @param <E> Entity type
	 */
	public <E extends Entity> void checkForm(final EasyFormsTemplate easyFormsTemplate, final E formOwner, final DataFieldName<E> formDataFieldName) {
		easyFormsRunnerServices.formatAndCheckFormulaire(formOwner, formDataFieldName, easyFormsTemplate, UiRequestUtil.obtainCurrentUiMessageStack());
	}
}
