package io.vertigo.easyforms.easyformsrunner.model.template;

import java.io.Serializable;

public class EasyFormsTemplateFieldValidator implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;
	private EasyFormsData parameters;

	public EasyFormsTemplateFieldValidator(final String code) {
		this.name = code;
	}

	public String getName() {
		return name;
	}

	public EasyFormsData getParameters() {
		return parameters;
	}

	public EasyFormsTemplateFieldValidator withParameters(final EasyFormsData parameters) {
		this.parameters = parameters;
		return this;
	}

}
