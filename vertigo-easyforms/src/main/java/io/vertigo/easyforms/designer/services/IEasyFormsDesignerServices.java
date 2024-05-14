package io.vertigo.easyforms.designer.services;

import java.util.List;
import java.util.Optional;

import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi;
import io.vertigo.easyforms.domain.EasyFormsItemUi;
import io.vertigo.easyforms.domain.EasyFormsSectionUi;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.vega.webservice.validation.UiMessageStack;

public interface IEasyFormsDesignerServices extends Component {

	DtList<EasyFormsFieldTypeUi> getFieldTypeUiList();

	DtList<EasyFormsFieldValidatorTypeUi> getFieldValidatorTypeUiList();

	void checkUpdateSection(EasyFormsTemplate easyFormsTemplate, Integer editIndex, EasyFormsSectionUi sectionEdit, UiMessageStack uiMessageStack);

	void checkUpdateField(EasyFormsTemplate easyFormsTemplate, List<AbstractEasyFormsTemplateItem> items, Integer editIndex, Optional<Integer> editIndex2, EasyFormsItemUi fieldEdit,
			UiMessageStack uiMessageStack);

	Long saveForm(EasyForm easyForm);

	EasyForm createEasyForm(EasyForm easyForm);

}
