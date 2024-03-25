package io.vertigo.easyforms.runner.model.template;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class EasyFormsTemplate implements Serializable {

	private final List<EasyFormsTemplateSection> sections;

	private static final long serialVersionUID = 1L;

	public EasyFormsTemplate(final List<EasyFormsTemplateSection> sections) {
		this.sections = Collections.unmodifiableList(sections);
	}

	public List<EasyFormsTemplateSection> getSections() {
		return sections;
	}

	public boolean useSections() {
		return !(sections.size() == 1 && sections.get(0).getCode() == null);
	}

}
