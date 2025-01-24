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
package io.vertigo.easyforms.runner.util;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.easyforms.designer.services.EasyFormsDesignerServices;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateSection;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.model.ui.EasyFormsListItem;
import io.vertigo.easyforms.runner.services.EasyFormsRunnerServices;
import io.vertigo.easyforms.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;
import io.vertigo.ui.impl.springmvc.util.UiUtil;
import io.vertigo.vega.webservice.model.UiList;
import io.vertigo.vega.webservice.model.UiObject;

public final class EasyFormsUiUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public EasyFormsFieldTypeDefinition getFieldTypeByName(final String fieldTypeName) {
		return Node.getNode().getDefinitionSpace().resolve(fieldTypeName, EasyFormsFieldTypeDefinition.class);
	}

	/**
	 * @param item The template item
	 * @return maxLength of the field
	 */
	public Integer maxLength(final AbstractEasyFormsTemplateItem item) {
		if (item instanceof final EasyFormsTemplateItemField field) {
			final Integer smartTypeMaxLength = Node.getNode().getDefinitionSpace().resolve(getFieldTypeByName(field.getFieldTypeName()).getSmartTypeName(), SmartTypeDefinition.class)
					.getProperties().getValue(DtProperty.MAX_LENGTH);
			final Long fieldMaxLength = field.getParameters() == null ? null : (Long) field.getParameters().get(DtProperty.MAX_LENGTH.getName());
			return min(smartTypeMaxLength, fieldMaxLength);
		}
		return null;
	}

	private Integer min(final Integer int1, final Long int2) {
		if (int2 == null) {
			return int1;
		} else if (int1 == null) {
			return int2.intValue();
		}
		return Math.min(int1, int2.intValue());
	}

	public LinkedHashMap<String, LinkedHashMap<String, Object>> getEasyFormRead(final EasyFormsTemplate easyFormsTemplate, final String objectKey, final String field, final String row) {
		final var object = UiRequestUtil.getCurrentViewContext().get(objectKey);
		if (row == null && object instanceof final UiObject<?> uiObject) {
			final EasyFormsData easyForm = uiObject.getTypedValue(field, EasyFormsData.class);
			return getEasyFormRead(easyFormsTemplate, easyForm);
		} else if (row != null && object instanceof final UiList<?> uiList) {
			final EasyFormsData easyForm = uiList.get(Integer.valueOf(row)).getTypedValue(field, EasyFormsData.class);
			return getEasyFormRead(easyFormsTemplate, easyForm);
		}
		throw new VSystemException("Unsupported object for easy form data.");
	}

	public LinkedHashMap<String, LinkedHashMap<String, Object>> getEasyFormRead(final EasyFormsTemplate easyFormsTemplate, final EasyFormsData easyForm) {
		final var easyFormsRunnerServices = Node.getNode().getComponentSpace().resolve(EasyFormsRunnerServices.class);

		return easyFormsRunnerServices.getEasyFormRead(easyFormsTemplate, easyForm, true);
	}

	public String getDynamicListForField(final EasyFormsTemplateItemField field) {
		return getDynamicListForField(field, null, null);
	}

	public String getDynamicListForField(final EasyFormsTemplateItemField field, final String filterFunction, final String searchValue) {
		final var easyFormsRunnerServices = Node.getNode().getComponentSpace().resolve(EasyFormsRunnerServices.class);
		final var fieldType = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);

		final var resolvedParameters = EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());

		final String listSupplier = (String) resolvedParameters.get(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER);
		final var ctxNameOpt = easyFormsRunnerServices.resolveCtxName(listSupplier);

		if (ctxNameOpt.isPresent()) {
			return listFromContext(ctxNameOpt.get(), filterFunction, searchValue);
		} else {
			return EasyFormsListItem.ofCollection(resolvedParameters.getOrDefault(listSupplier, List.of())).toString();
		}
	}

	private String listFromContext(final String ctxKeyName, final String filterFunction, final String searchValue) {
		final String idField = UiUtil.getIdField(ctxKeyName);
		final String displayField = UiUtil.getDisplayField(ctxKeyName);

		return "transformListForSelection('" + ctxKeyName + "', '" + idField + "', '" + displayField + "', " + filterFunction + ", " + searchValue + ")";
	}

	public EasyFormsData getParametersForField(final EasyFormsTemplateItemField field) {
		final var fieldType = getFieldTypeByName(field.getFieldTypeName());
		return EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());
	}

	public String resolveModelName(final EasyFormsTemplate template, final EasyFormsTemplateSection section, final EasyFormsTemplateItemField field) {
		return "slotProps.formData['" + (template.useSections() ? section.getCode() + "']['" : "") + field.getCode() + "']";
	}

	public String resolveCodeName(final EasyFormsTemplate template, final EasyFormsTemplateSection section, final EasyFormsTemplateItemField field) {
		return (template.useSections() ? section.getCode() + "--" : "") + field.getCode().replace("_", ""); // remove _ as it is reserved for qualifiers
	}

	public String convertConditionToJs(final String context, final String condition) {
		if (StringUtil.isBlank(condition)) {
			return "true";
		}

		return condition
				.replaceAll("#" + EasyFormsDesignerServices.FORM_INTERNAL_CTX_NAME + "\\.([a-zA-Z0-9_\\-\\.]+)#", "vueData.$1") // #ctx.xxx# => vueData.xxx
				.replaceAll("#([a-zA-Z0-9_\\-\\.]+)#", context + ".$1") // #xxx# => vueData.object.field.xxx
				.replaceAll("(?i) and ", " && ") // and => &&
				.replaceAll("(?i) or ", " || ") // or => ||
				.replaceAll("([^!><])=", "$1=="); // = => ==
	}

	public String resolveLabel(final Map<String, String> labels, final List<String> supportedLang, final Boolean isI18n) {
		if (labels == null) {
			return null;
		}
		if (Boolean.TRUE.equals(isI18n)) {
			return LocaleMessageText.of(() -> labels.get("i18n")).getDisplay();
		}

		return getEasyFormsRunnerManager().resolveTextForUserlang(labels);
	}

	private EasyFormsRunnerManager getEasyFormsRunnerManager() {
		return Node.getNode().getComponentSpace().resolve(EasyFormsRunnerManager.class);
	}
}
