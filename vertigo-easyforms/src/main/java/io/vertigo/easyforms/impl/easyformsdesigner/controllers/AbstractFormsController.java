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

import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.structure.util.VCollectors;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsFieldConstraintUiFields;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldConstraintUi;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldUi;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldType;
import io.vertigo.easyforms.impl.easyformsdesigner.services.EasyFormsDesignerServices;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.argumentresolvers.ViewAttribute;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;
import io.vertigo.vega.webservice.validation.UiMessageStack;

@Controller
@RequestMapping("/easyforms/designer")
public class AbstractFormsController extends AbstractVSpringMvcController {

	private static final ViewContextKey<EasyForm> efoKey = ViewContextKey.of("efo");
	private static final ViewContextKey<EasyFormsFieldTypeUi> fieldTypesKey = ViewContextKey.of("fieldTypes");
	private static final ViewContextKey<EasyFormsFieldConstraintUi> fieldConstraintsKey = ViewContextKey.of("fieldConstraints");
	private static final ViewContextKey<EasyFormsFieldUi> fieldsKey = ViewContextKey.of("fields");
	private static final ViewContextKey<EasyFormsFieldConstraintUi> editFieldConstraintsKey = ViewContextKey.of("editFieldConstraints");
	private static final ViewContextKey<EasyFormsFieldUi> editFieldKey = ViewContextKey.of("editField");

	@Inject
	private EasyFormsDesignerServices easyFormsAdminServices;

	public void initContext(final ViewContext viewContext, final Optional<UID<EasyForm>> efoIdOpt) {
		viewContext.publishDtList(fieldTypesKey, easyFormsAdminServices.getFieldTypeUiList());
		viewContext.publishDtList(fieldConstraintsKey, EasyFormsFieldConstraintUiFields.code, easyFormsAdminServices.getFieldConstraintUiList());
		viewContext.publishDtList(editFieldConstraintsKey, new DtList<>(EasyFormsFieldConstraintUi.class));
		//---
		viewContext.publishDto(efoKey, efoIdOpt
				.map(easyFormsAdminServices::getEasyFormById)
				.orElseGet(EasyForm::new));
		viewContext.publishDtList(fieldsKey, efoIdOpt
				.map(easyFormsAdminServices::getFieldUiListByEasyFormId)
				.orElseGet(() -> new DtList<>(EasyFormsFieldUi.class)));
		viewContext.publishDto(editFieldKey, buildChampUi());
	}

	private static EasyFormsFieldUi buildChampUi() {
		final var champUi = new EasyFormsFieldUi();
		champUi.setIsDefault(false);
		champUi.setIsMandatory(false);
		return champUi;
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
		viewContext.publishDto(editFieldKey, buildChampUi());
		viewContext.publishDtList(editFieldConstraintsKey, new DtList<>(EasyFormsFieldConstraintUi.class));
		return viewContext;
	}

	@PostMapping("/_editItem")
	public ViewContext editItem(final ViewContext viewContext,
			@RequestParam("editIndex") final Integer editIndex,
			@ViewAttribute("fields") final DtList<EasyFormsFieldUi> fields) {
		final var editedChamp = fields.get(editIndex);
		viewContext.publishDto(editFieldKey, editedChamp);
		loadControlesByType(viewContext, easyFormsAdminServices.getFieldConstraintUiList(), editedChamp);
		return viewContext;
	}

	@PostMapping("/_refreshItem")
	public ViewContext refreshItem(final ViewContext viewContext,
			@RequestParam("fieldType") final String fieldType,
			@ViewAttribute("editField") final EasyFormsFieldUi editField,
			@ViewAttribute("fields") final DtList<EasyFormsFieldUi> fields) {

		editField.setFieldType(fieldType);
		loadControlesByType(viewContext, easyFormsAdminServices.getFieldConstraintUiList(), editField);
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

		easyFormsAdminServices.checkUpdateField(fields, editIndex, editField, uiMessageStack);

		editField.setFieldTypeLabel(EasyFormsFieldType.resolve(editField.getFieldType()).getLabel());
		if (editIndex == -1) {
			fields.add(editField);
		} else {
			fields.set(editIndex, editField);
		}

		viewContext.publishDtList(fieldsKey, fields);
		viewContext.publishDto(editFieldKey, buildChampUi());
		return viewContext;
	}

	public Long save(final ViewContext viewContext) {
		final var mfo = viewContext.readDto(efoKey, getUiMessageStack());
		final var fields = viewContext.readDtList(fieldsKey, getUiMessageStack());
		return easyFormsAdminServices.saveNewForm(mfo, fields);
	}

	protected static void loadControlesByType(final ViewContext viewContext,
			final DtList<EasyFormsFieldConstraintUi> fieldConstraints,
			final EasyFormsFieldUi editField) {
		final DtList<EasyFormsFieldConstraintUi> fieldConstraintsByType = fieldConstraints.stream()
				.filter(c -> c.getFieldTypes().contains(EasyFormsFieldType.PREFIX + editField.getFieldType()))
				.collect(VCollectors.toDtList(EasyFormsFieldConstraintUi.class));

		viewContext.publishDtList(editFieldConstraintsKey, fieldConstraintsByType);
	}

	protected static String computeDefaultFieldCode(final DtList<EasyFormsFieldUi> fields, final EasyFormsFieldUi editedField) {
		final var prefixFieldCode = StringUtil.first2LowerCase(editedField.getFieldType());
		final var pattern = Pattern.compile("^" + prefixFieldCode + "[0-9]*$");
		final Optional<Integer> lastMatchingOpt = fields.stream()
				.filter(champ -> pattern.matcher(champ.getFieldCode()).matches())
				.map(champ -> {
					if (prefixFieldCode.length() == champ.getFieldCode().length()) {
						return 1;
					}
					return Integer.valueOf(champ.getFieldCode().substring(prefixFieldCode.length()));
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
