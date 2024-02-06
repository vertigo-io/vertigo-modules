package io.vertigo.easyforms.easyformsrunner.model.template;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EasyFormsTemplate implements Serializable {

	private final List<EasyFormsTemplateField> fields;

	private static final long serialVersionUID = 1L;

	public EasyFormsTemplate(final List<EasyFormsTemplateField> fields) {
		this.fields = Collections.unmodifiableList(fields);
	}

	public List<EasyFormsTemplateField> getFields() {
		return fields.stream().sorted(Comparator.comparing(EasyFormsTemplateField::getOrder)).toList();
	}

}
