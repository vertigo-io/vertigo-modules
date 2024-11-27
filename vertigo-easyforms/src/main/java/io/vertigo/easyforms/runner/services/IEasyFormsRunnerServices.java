/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.easyforms.runner.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.data.definitions.DataFieldName;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datastore.filestore.model.FileInfo;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.datastore.filestore.model.VFile;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.ui.core.UiFileInfo;
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
	 * Retrieves a list of EasyForms by their unique identifiers.
	 *
	 * @param efoUids The unique identifiers of the EasyForms.
	 * @return A list of EasyForms associated with the given unique identifiers.
	 */
	DtList<EasyForm> getEasyFormListByIds(Collection<UID<EasyForm>> efoUids);

	/**
	 * Formats and checks the form data.
	 *
	 * @param formOwner The owner of the form.
	 * @param formDataFieldName The field name of the form data.
	 * @param formTempalte The template of the form.
	 * @param uiMessageStack The stack of UI messages.
	 * @param contextValues The context values.
	 */
	<E extends DataObject> void formatAndCheckFormulaire(E formOwner, final DataFieldName<E> formDataFieldName, EasyFormsTemplate formTempalte, UiMessageStack uiMessageStack,
			Map<String, Serializable> contextValues);

	/**
	 * Formats and checks a single field of the form data.
	 *
	 * @param formOwner The owner of the form.
	 * @param fieldCode The code of the field.
	 * @param field The field to format and check.
	 * @param inputValue The value of the field.
	 * @param uiMessageStack The stack of UI messages.
	 * @return The formatted and checked value of the field.
	 */
	Object formatAndCheckSingleField(DataObject formOwner, String fieldCode, EasyFormsTemplateItemField field, Object inputValue, UiMessageStack uiMessageStack);

	/**
	 * Retrieves the default data values for a given form template.
	 *
	 * @param easyFormsTemplate The form template.
	 * @param contextValues The context values.
	 * @return The default data values for the given form template.
	 */
	EasyFormsData getDefaultDataValues(EasyFormsTemplate easyFormsTemplate, Map<String, Serializable> contextValues);

	/**
	 * Sets the default values on hidden fields.
	 *
	 * @param formTempalte The form template.
	 * @param formData The form data
	 * @param contextValues The context values.
	 */
	void setDefaultValuesOnHidden(EasyFormsTemplate formTempalte, EasyFormsData formData, Map<String, Serializable> contextValues);

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
	 * Retrieves the list of UiFileInfo from a given list of FileInfoURI.
	 *
	 * @param fileInfoUris The list of FileInfoURI.
	 * @return The list of UiFileInfo.
	 */
	List<UiFileInfo> getFileInfos(List<FileInfoURI> fileInfoUris);

	/**
	 * Retrieves the UiFileInfo from a given FileInfoURI.
	 *
	 * @param fileInfoUri The FileInfoURI.
	 * @return The UiFileInfo
	 */
	UiFileInfo getFileInfo(FileInfoURI fileInfoUri);

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
