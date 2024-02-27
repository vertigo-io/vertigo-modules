/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.easyforms.impl.easyformsdesigner.controllers;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.vertigo.core.node.Node;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.data.util.VCollectors;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsFieldValidatorTypeUiFields;
import io.vertigo.easyforms.easyformsdesigner.services.IEasyFormsDesignerServices;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldUi;
import io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi;
import io.vertigo.easyforms.domain.EasyFormsTemplateFieldValidatorUi;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldValidatorTypeDefinition;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.ui.EasyFormsTemplateFieldValidatorUiList;
import io.vertigo.easyforms.impl.easyformsrunner.services.EasyFormsRunnerServices;
import io.vertigo.easyforms.impl.easyformsrunner.util.EasyFormsUiUtil;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.argumentresolvers.ViewAttribute;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;
import io.vertigo.vega.webservice.validation.UiMessageStack;

@Controller
@RequestMapping("/easyforms/designer")
public class EasyFormsDesignerController extends AbstractVSpringMvcController {

	private static final ViewContextKey<EasyForm> efoKey = ViewContextKey.of("efo");
	private static final ViewContextKey<EasyFormsFieldTypeUi> fieldTypesKey = ViewContextKey.of("fieldTypes");
	private static final ViewContextKey<Serializable> fieldTypesTemplateKey = ViewContextKey.of("fieldTypesTemplate");
	private static final ViewContextKey<EasyFormsFieldValidatorTypeUi> fieldValidatorsKey = ViewContextKey.of("fieldValidators");
	private static final ViewContextKey<EasyFormsFieldUi> fieldsKey = ViewContextKey.of("fields");
	private static final ViewContextKey<EasyFormsFieldValidatorTypeUi> editFieldValidatorTypesKey = ViewContextKey.of("editFieldValidatorTypes");
	private static final ViewContextKey<EasyFormsFieldUi> editFieldKey = ViewContextKey.of("editField");

	private static final ViewContextKey<EasyFormsUiUtil> efoUiUtilKey = ViewContextKey.of("efoUiUtil");

	@Inject
	private IEasyFormsDesignerServices easyFormsDesignerServices;

	@Inject
	private EasyFormsRunnerServices easyFormsRunnerServices;

	public void initContext(final ViewContext viewContext, final Optional<UID<EasyForm>> efoIdOpt) {
		final var fieldTypeUiList = easyFormsDesignerServices.getFieldTypeUiList();
		fieldTypeUiList.sort(Comparator.comparing(EasyFormsFieldTypeUi::getLabel));

		viewContext.publishDtList(fieldTypesKey, fieldTypeUiList);
		viewContext.publishRef(fieldTypesTemplateKey,
				(Serializable) fieldTypeUiList.stream()
						.filter(EasyFormsFieldTypeUi::getHasTemplate)
						.collect(Collectors.toMap(EasyFormsFieldTypeUi::getName, EasyFormsFieldTypeUi::getParamTemplate)));
		viewContext.publishDtList(fieldValidatorsKey, EasyFormsFieldValidatorTypeUiFields.name, easyFormsDesignerServices.getFieldValidatorTypeUiList());
		viewContext.publishDtList(editFieldValidatorTypesKey, new DtList<>(EasyFormsFieldValidatorTypeUi.class));
		//---
		final EasyForm easyForm = efoIdOpt
				.map(easyFormsRunnerServices::getEasyFormById)
				.orElseGet(EasyForm::new);
		viewContext.publishDto(efoKey, easyForm);
		viewContext.publishDtList(fieldsKey, easyFormsDesignerServices.getFieldUiListByEasyForm(easyForm));
		viewContext.publishDto(editFieldKey, buildFieldUi());

		viewContext.publishRef(efoUiUtilKey, new EasyFormsUiUtil());
	}

	private static EasyFormsFieldUi buildFieldUi() {
		final var fieldUi = new EasyFormsFieldUi();
		fieldUi.setIsDefault(false);
		fieldUi.setIsMandatory(false);
		return fieldUi;
	}

	@PostMapping("/_deleteItem")
	public ViewContext deleteItem(final ViewContext viewContext,
			@RequestParam("editIndex") final Integer editIndex,
			@ViewAttribute("fields") final DtList<EasyFormsFieldUi> fields) {

		fields.remove(editIndex.intValue());
		viewContext.publishDtList(fieldsKey, fields);
		return viewContext;
	}

	@PostMapping("/_addItem")
	public ViewContext addNewItem(final ViewContext viewContext) {
		viewContext.publishDto(editFieldKey, buildFieldUi());
		viewContext.publishDtList(editFieldValidatorTypesKey, new DtList<>(EasyFormsFieldValidatorTypeUi.class));
		return viewContext;
	}

	@PostMapping("/_editItem")
	public ViewContext editItem(final ViewContext viewContext,
			@RequestParam("editIndex") final Integer editIndex,
			@ViewAttribute("fields") final DtList<EasyFormsFieldUi> fields) {

		final var editedField = fields.get(editIndex);
		editedField.setFieldValidatorSelection(editedField.getFieldValidators().stream().map(EasyFormsTemplateFieldValidatorUi::getValidatorTypeName).toList());

		viewContext.publishDto(editFieldKey, editedField);
		loadControlsByType(viewContext, easyFormsDesignerServices.getFieldValidatorTypeUiList(), editedField);
		return viewContext;
	}

	@PostMapping("/_refreshItem")
	public ViewContext refreshItem(final ViewContext viewContext,
			@RequestParam("fieldType") final String fieldType,
			@ViewAttribute("editField") final EasyFormsFieldUi editField,
			@ViewAttribute("fields") final DtList<EasyFormsFieldUi> fields) {

		editField.setFieldType(fieldType);

		// add default values for field type parameters
		final var fieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(fieldType, EasyFormsFieldTypeDefinition.class);
		if (fieldTypeDefinition.getParamTemplate() != null) {
			final var fieldTypeParameters = new EasyFormsData();
			for (final var paramField : fieldTypeDefinition.getParamTemplate().getFields()) {
				final var paramFieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(paramField.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
				if (paramFieldTypeDefinition.getDefaultValue() != null) {
					fieldTypeParameters.put(paramField.getCode(), paramFieldTypeDefinition.getDefaultValue());
				}
			}
			editField.setParameters(fieldTypeParameters);
		}

		loadControlsByType(viewContext, easyFormsDesignerServices.getFieldValidatorTypeUiList(), editField);
		editField.setFieldCode(computeDefaultFieldCode(fields, editField));
		viewContext.publishDto(editFieldKey, editField);
		return viewContext;
	}

	@PostMapping("/_moveItem")
	public ViewContext moveItem(final ViewContext viewContext,
			@RequestParam("editIndex") final int editIndex,
			@RequestParam("offset") final int offset,
			@ViewAttribute("fields") final DtList<EasyFormsFieldUi> fields) {
		final var toMove = fields.remove(editIndex);
		fields.add(editIndex + offset, toMove);
		viewContext.publishDtList(fieldsKey, fields);
		return viewContext;
	}

	@PostMapping("/_saveItem")
	public ViewContext saveItem(final ViewContext viewContext,
			@RequestParam("editIndex") final Integer editIndex,
			@ViewAttribute("fields") final DtList<EasyFormsFieldUi> fields,
			@ViewAttribute("editField") final EasyFormsFieldUi editField,
			final UiMessageStack uiMessageStack) {

		easyFormsDesignerServices.checkUpdateField(fields, editIndex, editField, uiMessageStack);

		editField.setFieldTypeLabel(EasyFormsFieldTypeDefinition.resolve(editField.getFieldType()).getLabel());

		// Convert validator selection into real validator UI
		// This is necessary beacause actual UI is a simple list of validators with no params
		final var easyFormsTemplateFieldValidatorUiList = new EasyFormsTemplateFieldValidatorUiList(editField.getFieldValidatorSelection().size());
		for (final var validatorName : editField.getFieldValidatorSelection()) {
			final var validatorType = Node.getNode().getDefinitionSpace().resolve(validatorName, EasyFormsFieldValidatorTypeDefinition.class);
			final var validator = new EasyFormsTemplateFieldValidatorUi();
			validator.setValidatorTypeName(validatorName);
			validator.setLabel(validatorType.getLabel());
			validator.setParameterizedLabel(validatorType.getParameterizedLabel(validator.getParameters()));
			validator.setDescription(validatorType.getDescription());
			easyFormsTemplateFieldValidatorUiList.add(validator);
		}
		editField.setFieldValidators(easyFormsTemplateFieldValidatorUiList);

		if (editIndex == -1) {
			fields.add(editField);
		} else {
			fields.set(editIndex, editField);
		}

		viewContext.publishDtList(fieldsKey, fields);
		viewContext.publishDto(editFieldKey, buildFieldUi());
		return viewContext;
	}

	public Long save(final ViewContext viewContext) {
		final var efo = viewContext.readDto(efoKey, getUiMessageStack());
		final var fields = viewContext.readDtList(fieldsKey, getUiMessageStack());
		return easyFormsDesignerServices.saveNewForm(efo, fields);
	}

	protected static void loadControlsByType(final ViewContext viewContext,
			final DtList<EasyFormsFieldValidatorTypeUi> fieldValidators,
			final EasyFormsFieldUi editField) {
		final DtList<EasyFormsFieldValidatorTypeUi> fieldValidatorsByType = fieldValidators.stream()
				.filter(c -> c.getFieldTypes().contains(editField.getFieldType()))
				.collect(VCollectors.toDtList(EasyFormsFieldValidatorTypeUi.class));

		viewContext.publishDtList(editFieldValidatorTypesKey, fieldValidatorsByType);
	}

	protected static String computeDefaultFieldCode(final DtList<EasyFormsFieldUi> fields, final EasyFormsFieldUi editedField) {
		final var prefixFieldCode = StringUtil.first2LowerCase(editedField.getFieldType().substring(5));
		final var pattern = Pattern.compile("^" + prefixFieldCode + "[0-9]*$");
		final Optional<Integer> lastMatchingOpt = fields.stream()
				.filter(f -> pattern.matcher(f.getFieldCode()).matches())
				.map(f -> {
					if (prefixFieldCode.length() == f.getFieldCode().length()) {
						return 1;
					}
					return Integer.valueOf(f.getFieldCode().substring(prefixFieldCode.length()));
				})
				.sorted(Comparator.reverseOrder())
				.findFirst();
		if (lastMatchingOpt.isPresent()) {
			return prefixFieldCode + (lastMatchingOpt.get() + 1);
		} else {
			return prefixFieldCode;
		}
	}

}
