package io.vertigo.easyforms.runner.model.template;

import java.io.Serializable;

public final class EasyFormsTemplateFieldValidator implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;
	private EasyFormsData parameters;

	public EasyFormsTemplateFieldValidator(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public EasyFormsData getParameters() {
		return parameters;
	}

	public EasyFormsTemplateFieldValidator withParameters(final EasyFormsData myParameters) {
		parameters = myParameters;
		return this;
	}

}
