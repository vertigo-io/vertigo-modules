package io.vertigo.easyforms.runner.services;

import java.util.List;

import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateSection;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.vega.webservice.validation.UiMessageStack;

public interface IEasyFormsRunnerServices extends Component {

	EasyForm getEasyFormById(UID<EasyForm> efoUid);

	void formatAndCheckFormulaire(DataObject formOwner, EasyFormsData formData, EasyFormsTemplate formTempalte, UiMessageStack uiMessageStack);

	List<EasyFormsTemplateItemField> getAllFieldsFromSection(EasyFormsTemplateSection section);

	EasyFormsData getDefaultDataValues(EasyFormsTemplate easyFormsTemplate);

}
