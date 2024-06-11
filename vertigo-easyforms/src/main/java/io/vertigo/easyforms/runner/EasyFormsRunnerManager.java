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

/**
 * This interface defines the contract for the EasyFormsRunnerManager.
 * It is not automatically discoverable by Vertigo, we need to declare the feature easyforms to provide parameters.
 */
@NotDiscoverable
public interface EasyFormsRunnerManager extends Manager {

	/**
	 * Formats a field based on the provided field descriptor and input value.
	 *
	 * @param fieldDescriptor The descriptor of the field to be formatted.
	 * @param inputValue The value to be formatted.
	 * @return The formatted object.
	 * @throws FormatterException If an error occurs during formatting.
	 */
	Object formatField(final EasyFormsDataDescriptor fieldDescriptor, final Object inputValue) throws FormatterException;

	/**
	 * Validates a field based on the provided field descriptor, value, and context.
	 *
	 * @param fieldDescriptor The descriptor of the field to be validated.
	 * @param value The value to be validated.
	 * @param context The context in which the validation is performed.
	 * @return A list of validation error messages, if any.
	 */
	List<String> validateField(final EasyFormsDataDescriptor fieldDescriptor, final Object value, final Map<String, Object> context);

	/**
	 * Retrieves the list of supported languages.
	 *
	 * @return A list of supported languages.
	 */
	List<String> getSupportedLang();

	/**
	 * Resolves a text for the provided labels based on the user's language. If the user's language is empty, the first supported language is used.
	 *
	 * @param labels The labels to be resolved.
	 * @return The resolved text.
	 */
	String resolveTextForUserlang(Map<String, String> labels);

	/**
	 * Creates a standard FileInfo object from the provided VFile.
	 *
	 * @param vFile The VFile to be converted into a FileInfo.
	 * @return The created FileInfo object.
	 */
	FileInfo createStdFileInfo(VFile vFile);

	/**
	 * Creates a temporary FileInfo object from the provided VFile.
	 *
	 * @param vFile The VFile to be converted into a FileInfo.
	 * @return The created FileInfo object.
	 */
	FileInfo createTmpFileInfo(VFile vFile);

	/**
	 * Checks if the provided FileInfoDefinition corresponds to a temporary FileInfo.
	 *
	 * @param fileInfoDefinition The FileInfoDefinition to be checked.
	 * @return True if the FileInfoDefinition corresponds to a temporary FileInfo, false otherwise.
	 */
	boolean isTmpFileInfo(FileInfoDefinition fileInfoDefinition);

	/**
	 * Checks if the provided FileInfoDefinition corresponds to a standard FileInfo.
	 *
	 * @param fileInfoDefinition The FileInfoDefinition to be checked.
	 * @return True if the FileInfoDefinition corresponds to a standard FileInfo, false otherwise.
	 */
	boolean isStdFileInfo(FileInfoDefinition fileInfoDefinition);

}
