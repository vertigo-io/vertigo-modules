package io.vertigo.easyforms.easyformsrunner.model.template;

import java.io.Serializable;

public class EasyFormsTemplateFieldValidator implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;
	private EasyFormsData _parameters;

	public EasyFormsTemplateFieldValidator(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public EasyFormsData getParameters() {
		return _parameters;
	}

	public EasyFormsTemplateFieldValidator withParameters(final EasyFormsData parameters) {
		this._parameters = parameters;
		return this;
	}

}
