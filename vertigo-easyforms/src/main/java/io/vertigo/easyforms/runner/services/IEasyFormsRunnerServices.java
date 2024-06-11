package io.vertigo.easyforms.runner.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datastore.filestore.model.FileInfo;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.datastore.filestore.model.VFile;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateSection;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.vega.webservice.validation.UiMessageStack;

public interface IEasyFormsRunnerServices extends Component {

	/**
	 * Retrieves an EasyForm by its unique identifier.
	 *
	 * @param efoUid The unique identifier of the EasyForm.
	 * @return The EasyForm associated with the given unique identifier.
	 */
	EasyForm getEasyFormById(UID<EasyForm> efoUid);

	/**
	 * Formats and checks the form data.
	 *
	 * @param formOwner The owner of the form.
	 * @param formData The data of the form.
	 * @param formTempalte The template of the form.
	 * @param uiMessageStack The stack of UI messages.
	 */
	void formatAndCheckFormulaire(DataObject formOwner, EasyFormsData formData, EasyFormsTemplate formTempalte, UiMessageStack uiMessageStack);

	/**
	 * Retrieves all fields from a given section of the form.
	 *
	 * @param section The section of the form.
	 * @return A list of all fields in the given section.
	 */
	List<EasyFormsTemplateItemField> getAllFieldsFromSection(EasyFormsTemplateSection section);

	/**
	 * Retrieves the default data values for a given form template.
	 *
	 * @param easyFormsTemplate The form template.
	 * @return The default data values for the given form template.
	 */
	EasyFormsData getDefaultDataValues(EasyFormsTemplate easyFormsTemplate);

	/**
	 * Creates a temporary FileInfo from a given VFile.
	 *
	 * @param vFile The VFile.
	 * @return The temporary FileInfo.
	 */
	FileInfo createTmpFileInfo(VFile vFile);

	/**
	 * Saves a temporary file from a given VFile.
	 *
	 * @param vFile The VFile.
	 * @return The FileInfo of the saved file.
	 */
	FileInfo saveTmpFile(VFile vFile);

	/**
	 * Downloads a file from a given FileInfoURI.
	 *
	 * @param fileInfoUri The FileInfoURI of the file to download.
	 * @return The downloaded VFile.
	 */
	VFile downloadFile(FileInfoURI fileInfoUri);

	/**
	 * Retrieves the read form of an EasyForm from a given template and data.
	 *
	 * @param easyFormsTemplate The form template.
	 * @param easyForm The form data.
	 * @return The read form of the EasyForm.
	 */
	LinkedHashMap<String, LinkedHashMap<String, Object>> getEasyFormRead(EasyFormsTemplate easyFormsTemplate, EasyFormsData easyForm);

	/**
	 * Resolves the context name from a given list supplier.
	 *
	 * @param listSupplier The list supplier.
	 * @return The context name, if it can be resolved.
	 */
	Optional<String> resolveCtxName(String listSupplier);

	/**
	 * Persists the files from the new form data and removes any files from the old form data that are not present in the new form data.
	 *
	 * @param oldDataOpt The old form data.
	 * @param newData The new form data.
	 */
	void persistFiles(Optional<EasyFormsData> oldDataOpt, EasyFormsData newData);

}
