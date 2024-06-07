package io.vertigo.easyforms.runner;

import java.util.List;
import java.util.Map;

import io.vertigo.core.node.component.Manager;
import io.vertigo.core.node.config.discovery.NotDiscoverable;
import io.vertigo.datamodel.smarttype.definitions.FormatterException;
import io.vertigo.datastore.filestore.definitions.FileInfoDefinition;
import io.vertigo.datastore.filestore.model.FileInfo;
import io.vertigo.datastore.filestore.model.VFile;
import io.vertigo.easyforms.runner.model.data.EasyFormsDataDescriptor;

@NotDiscoverable
public interface EasyFormsRunnerManager extends Manager {

	Object formatField(final EasyFormsDataDescriptor fieldDescriptor, final Object inputValue) throws FormatterException;

	List<String> validateField(final EasyFormsDataDescriptor fieldDescriptor, final Object value, final Map<String, Object> context);

	FileInfo createStdFileInfo(VFile vFile);

	FileInfo createTmpFileInfo(VFile vFile);

	boolean isTmpFileInfo(FileInfoDefinition fileInfoDefinition);

	boolean isStdFileInfo(FileInfoDefinition fileInfoDefinition);
}
