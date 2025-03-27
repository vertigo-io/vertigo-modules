/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.easyforms.runner.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;

import io.vertigo.core.node.Node;
import io.vertigo.datamodel.data.definitions.DataFieldName;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.pack.provider.uicomponent.FileUiComponent;
import io.vertigo.easyforms.runner.services.EasyFormsRunnerServices;
import io.vertigo.easyforms.runner.util.EasyFormsControllerUtil;
import io.vertigo.easyforms.runner.util.EasyFormsUiUtil;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.controller.VSpringMvcUiMessageStack;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;
import io.vertigo.vega.webservice.validation.UiMessageStack;
import io.vertigo.vega.webservice.validation.ValidationUserException;

@Controller
public final class EasyFormsRunnerController {

	@Inject
	private EasyFormsRunnerServices easyFormsRunnerServices;

	@Inject
	private EasyFormsRunnerManager easyFormsRunnerManager;

	private static final ViewContextKey<EasyFormsUiUtil> efoUiUtilKey = ViewContextKey.of("efoUiUtil");

	private static final ViewContextKey<ArrayList<String>> efoSupportedLang = ViewContextKey.of("efoAllLang");

	private static final ViewContextKey<HashSet<String>> efoSupportedFileExtensions = ViewContextKey.of("efoSupportedFileExtensions");
	private static final ViewContextKey<Boolean> efoAllFileExtensions = ViewContextKey.of("efoAllFileExtensions");
	private static final ViewContextKey<Long> efoMaxFileFileSize = ViewContextKey.of("efoMaxFileFileSize");

	/**
	 * Initialize the context for reading a form.
	 *
	 * @param viewContext Current viewContext
	 * @param efoUid UID of the form
	 * @param templateKey ViewContextKey where the template is published
	 */
	public void initReadContext(final ViewContext viewContext, final UID<EasyForm> efoUid, final ViewContextKey<EasyFormsTemplate> templateKey) {
		final var easyForm = easyFormsRunnerServices.getEasyFormById(efoUid);

		final ArrayList<String> supportedLang = new ArrayList<>(easyFormsRunnerManager.getSupportedLang()); // needed to be ArrayList to be serializable

		viewContext.publishRef(efoUiUtilKey, new EasyFormsUiUtil())
				.publishRef(templateKey, easyForm.getTemplate())
				.publishRef(efoSupportedLang, supportedLang);

		EasyFormsControllerUtil.addRequiredContext(viewContext, easyForm.getTemplate(), false);
	}

	/**
	 * Initialize the context for reading multiple forms.
	 *
	 * @param viewContext Current viewContext
	 * @param efoUidList List of UID of the forms
	 * @param templateKey ViewContextKey where the template is published
	 */
	public void initMultipleReadContext(final ViewContext viewContext, final Collection<UID<EasyForm>> efoUidList, final ViewContextKey<ArrayList<EasyFormsTemplate>> templateKey) {
		final var easyForms = easyFormsRunnerServices.getEasyFormListByIds(efoUidList);
		final var templates = easyForms.stream()
				.map(EasyForm::getTemplate)
				.toList();

		final ArrayList<String> supportedLang = new ArrayList<>(easyFormsRunnerManager.getSupportedLang()); // needed to be ArrayList to be serializable

		viewContext.publishRef(efoUiUtilKey, new EasyFormsUiUtil())
				.publishRef(templateKey, new ArrayList<>(templates))
				.publishRef(efoSupportedLang, supportedLang);

		for (final var easyForm : easyForms) {
			EasyFormsControllerUtil.addRequiredContext(viewContext, easyForm.getTemplate(), false);
		}
	}

	/**
	 * Initialize the context for editing a form.
	 *
	 * @param viewContext Current viewContext
	 * @param efoUid UID of the form
	 * @param templateKey ViewContextKey where the template is published
	 * @param formulaireResponse Data of the form (user input)
	 * @param isCreation if the form is being created
	 * @param additionalContextKeys additional context keys needed for the form (eg, resolving conditional blocs)
	 */
	public void initEditContext(final ViewContext viewContext,
			final UID<EasyForm> efoUid, final ViewContextKey<EasyFormsTemplate> templateKey,
			final EasyFormsData formulaireResponse, final boolean isCreation, final ViewContextKey<?>... additionalContextKeys) {

		final var easyForm = easyFormsRunnerServices.getEasyFormById(efoUid);

		final ArrayList<String> supportedLang = new ArrayList<>(easyFormsRunnerManager.getSupportedLang()); // needed to be ArrayList to be serializable

		// if new form, add all default values, if not, add only values on hidden fields
		final var template = easyForm.getTemplate();
		if (isCreation) {
			formulaireResponse.putAll(easyFormsRunnerServices.getDefaultDataValues(template, viewContext.asMap()));
		} else {
			easyFormsRunnerServices.setDefaultValuesOnHidden(template, formulaireResponse, viewContext.asMap());
		}

		// save globally accepted extensions for file upload on the whole form as a security measure (we cant target a specific field here so we check global coherence, fine check is done upon save)
		saveFileUploadMaxConstraints(viewContext, template);

		// publish all needed data to the viewContext
		viewContext.publishRef(efoUiUtilKey, new EasyFormsUiUtil())
				.publishRef(templateKey, template)
				.publishRef(efoSupportedLang, supportedLang);

		// Add master data list needed for fields to context
		EasyFormsControllerUtil.addRequiredContext(viewContext, template, true);

		for (final var additionalContextKey : additionalContextKeys) {
			EasyFormsControllerUtil.addToFrontCtx(viewContext, additionalContextKey.get());
		}
	}

	private void saveFileUploadMaxConstraints(final ViewContext viewContext, final EasyFormsTemplate template) {
		Boolean allFileExtensions = viewContext.containsKey(efoAllFileExtensions) ? (Boolean) viewContext.get(efoAllFileExtensions) : Boolean.FALSE;
		final HashSet<String> acceptedExtensions = viewContext.containsKey(efoSupportedFileExtensions) ? (HashSet<String>) viewContext.get(efoSupportedFileExtensions) : new HashSet<>();
		Long maxFileSize = viewContext.containsKey(efoMaxFileFileSize) ? (Long) viewContext.get(efoMaxFileFileSize) : 0L;

		for (final var section : template.getSections()) {
			for (final var field : section.getAllFields()) {
				final var parameters = field.getParameters();
				final var fieldDefinition = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
				if (parameters != null && "EfUicFile".equals(fieldDefinition.getUiComponentName())) {
					// when a field use file uploader ui component
					if (parameters.containsKey(FileUiComponent.ACCEPT) && Boolean.FALSE.equals(allFileExtensions)) {
						final var extentions = ((String) parameters.get(FileUiComponent.ACCEPT)).trim().split("\\s*,\\s*");
						if (extentions.length == 0) {
							allFileExtensions = Boolean.TRUE;
							viewContext.publishRef(efoAllFileExtensions, Boolean.TRUE);
							acceptedExtensions.clear();
						} else {
							Collections.addAll(acceptedExtensions, extentions);
						}
					}
					if (!maxFileSize.equals(-1L)) {
						if (parameters.containsKey(FileUiComponent.MAX_FILE_SIZE)) {
							maxFileSize = Math.max(maxFileSize, (Long) parameters.get(FileUiComponent.MAX_FILE_SIZE));
						} else {
							maxFileSize = -1L; // no limit
						}
					}
				}
			}
		}

		viewContext
				.publishRef(efoSupportedFileExtensions, acceptedExtensions)
				.publishRef(efoMaxFileFileSize, maxFileSize);
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
	 * Validate EasyFormData with the given template and process UI data conversion.
	 * Can be only used with EasyFormsData coming from UI, not from database.
	 *
	 * @param easyFormsTemplate template to check
	 * @param formOwner entity holding the form
	 * @param formDataFieldName field name of the form in the entity
	 * @param throwUserErrors if true, throw UserException if errors
	 * @param <E> Entity type
	 * @return if the form is valid
	 */
	public <E extends Entity> boolean checkAndProcessUiFormResponse(final EasyFormsTemplate easyFormsTemplate, final E formOwner, final DataFieldName<E> formDataFieldName,
			final boolean throwUserErrors) {
		// do not use the actual message stack if we don't want to throw user errors
		final UiMessageStack uiMessageStack = throwUserErrors ? UiRequestUtil.obtainCurrentUiMessageStack() : new VSpringMvcUiMessageStack();

		easyFormsRunnerServices.formatAndCheckFormulaire(formOwner, formDataFieldName, easyFormsTemplate, uiMessageStack, UiRequestUtil.getCurrentViewContext().asMap());
		if (throwUserErrors && uiMessageStack.hasErrors()) {
			throw new ValidationUserException();
		}

		return !uiMessageStack.hasErrors();
	}
}
