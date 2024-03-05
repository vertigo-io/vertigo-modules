package io.vertigo.easyforms.runner;

import java.util.List;
import java.util.Map;

import io.vertigo.core.node.component.Manager;
import io.vertigo.datamodel.smarttype.definitions.FormatterException;
import io.vertigo.easyforms.runner.model.data.EasyFormsDataDescriptor;

public interface EasyFormsRunnerManager extends Manager {

	Object formatField(final EasyFormsDataDescriptor fieldDescriptor, final Object inputValue) throws FormatterException;

	List<String> validateField(final EasyFormsDataDescriptor fieldDescriptor, final Object value, final Map<String, Object> context);
}
