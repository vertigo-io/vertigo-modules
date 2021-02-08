/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2021, Vertigo.io, team@vertigo.io
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
package io.vertigo.ui.core;

import java.io.Serializable;

import io.vertigo.core.lang.Assertion;
import io.vertigo.datastore.filestore.model.FileInfo;
import io.vertigo.datastore.filestore.model.FileInfoURI;

/**
 * Ui data of File.
 * @author npiedeloup
 */
public final class UiFileInfo<F extends FileInfo> implements Serializable {

	private transient FileInfoURI uri;
	private final String fileUri; //match quasar file api
	private final String name;
	private final String type;
	private final Long size;
	private final String __status = "uploaded";

	/**
	 * Constructeur.
	 * @param fileInfo fileInfo
	 */
	public UiFileInfo(final F fileInfo) {
		Assertion.check().isNotNull(fileInfo);
		Assertion.check().isNotNull(fileInfo.getURI(), "Only already stored FileInfo could be put in context : the file data wasn't kept in context. You may use a temp storage.");
		//-----
		this.uri = fileInfo.getURI();
		this.fileUri = ProtectedValueUtil.generateProtectedValue(uri);
		this.name = fileInfo.getVFile().getFileName();
		this.type = fileInfo.getVFile().getMimeType();
		this.size = fileInfo.getVFile().getLength();
	}

	// ==========================================================================

	public FileInfoURI getURI() {
		if (uri == null) {
			uri = ProtectedValueUtil.readProtectedValue(fileUri, FileInfoURI.class);
		}
		return uri;
	}

	public String getFileUri() {
		return fileUri;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Long getSize() {
		return size;
	}

	public String getStatus() {
		return __status;
	}
}