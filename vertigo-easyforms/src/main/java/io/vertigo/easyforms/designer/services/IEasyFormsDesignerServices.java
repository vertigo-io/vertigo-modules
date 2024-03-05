package io.vertigo.easyforms.designer.services;

import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldUi;
import io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi;
import io.vertigo.vega.webservice.validation.UiMessageStack;

public interface IEasyFormsDesignerServices extends Component {

	DtList<EasyFormsFieldTypeUi> getFieldTypeUiList();

	DtList<EasyFormsFieldValidatorTypeUi> getFieldValidatorTypeUiList();

	DtList<EasyFormsFieldUi> getFieldUiListByEasyForm(EasyForm easyForm);

	void checkUpdateField(DtList<EasyFormsFieldUi> fields, Integer editIndex, EasyFormsFieldUi fieldEdit, UiMessageStack uiMessageStack);

	Long saveNewForm(EasyForm easyForm, DtList<EasyFormsFieldUi> fields);

	EasyForm createEasyForm(EasyForm easyForm);

}
