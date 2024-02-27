package io.vertigo.easyforms.easyformsrunner.services;

import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;
import io.vertigo.vega.webservice.validation.UiMessageStack;

public interface IEasyFormsRunnerServices extends Component {

	EasyForm getEasyFormById(UID<EasyForm> efoUid);

	void checkFormulaire(Entity formOwner, EasyFormsData formData, EasyFormsTemplate formTempalte, UiMessageStack uiMessageStack);
}
