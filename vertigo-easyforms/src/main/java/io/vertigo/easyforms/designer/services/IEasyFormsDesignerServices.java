package io.vertigo.easyforms.designer.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi;
import io.vertigo.easyforms.domain.EasyFormsItemUi;
import io.vertigo.easyforms.domain.EasyFormsLabelUi;
import io.vertigo.easyforms.domain.EasyFormsSectionUi;
import io.vertigo.easyforms.impl.runner.rule.FormContextDescription;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.vega.webservice.validation.UiMessageStack;

public interface IEasyFormsDesignerServices extends Component {

	/**
	 * Get the list of all field types.
	 *
	 * @return A list of all field types.
	 */
	DtList<EasyFormsFieldTypeUi> getFieldTypeUiList();

	/**
	 * Get the list of all field validator types.
	 *
	 * @return A list of all field validator types.
	 */
	DtList<EasyFormsFieldValidatorTypeUi> getFieldValidatorTypeUiList();

	/**
	 * Build a context description for the EasyFormsTemplate.
	 *
	 * @param easyFormsTemplate The EasyFormsTemplate to build the context for.
	 * @return The context description.
	 */
	FormContextDescription buildContextDescription(EasyFormsTemplate easyFormsTemplate);

	/**
	 * Build a context description for the EasyFormsTemplate with additional context.
	 *
	 * @param easyFormsTemplate The EasyFormsTemplate to build the context for.
	 * @param additionalContext Additional context for the description that will appear in 'ctx' section.
	 * @return The context description.
	 */
	FormContextDescription buildContextDescription(EasyFormsTemplate easyFormsTemplate, Map<String, Serializable> additionalContext);

	/**
	 * Check and update a section of the EasyFormsTemplate.
	 *
	 * @param easyFormsTemplate The EasyFormsTemplate to update.
	 * @param editIndex The index of the section to update.
	 * @param sectionEdit The new section data.
	 * @param labels The labels for the section.
	 * @param additionalContext Additional context for the update.
	 * @param uiMessageStack The UI message stack for the update.
	 */
	void checkUpdateSection(EasyFormsTemplate easyFormsTemplate, Integer editIndex, EasyFormsSectionUi sectionEdit, DtList<EasyFormsLabelUi> labels, Map<String, Serializable> additionalContext,
			UiMessageStack uiMessageStack);

	/**
	 * Check and update a field of the EasyFormsTemplate.
	 *
	 * @param easyFormsTemplate The EasyFormsTemplate to update.
	 * @param items The items in the EasyFormsTemplate.
	 * @param editIndex The index of the field to update.
	 * @param editIndex2 The secondary index of the field to update.
	 * @param fieldEdit The new field data.
	 * @param labels The labels for the field.
	 * @param additionalContext Additional context for the update.
	 * @param uiMessageStack The UI message stack for the update.
	 */
	void checkUpdateField(EasyFormsTemplate easyFormsTemplate, List<AbstractEasyFormsTemplateItem> items, Integer editIndex, Optional<Integer> editIndex2, EasyFormsItemUi fieldEdit,
			DtList<EasyFormsLabelUi> labels, final Map<String, Serializable> additionalContext, UiMessageStack uiMessageStack);

	/**
	 * Save an EasyForm.
	 *
	 * @param easyForm The EasyForm to save.
	 * @return The ID of the saved EasyForm.
	 */
	Long saveForm(EasyForm easyForm);

	/**
	 * Create a new EasyForm.
	 *
	 * @param easyForm The EasyForm to create.
	 * @return The created EasyForm.
	 */
	EasyForm createEasyForm(EasyForm easyForm);

}
