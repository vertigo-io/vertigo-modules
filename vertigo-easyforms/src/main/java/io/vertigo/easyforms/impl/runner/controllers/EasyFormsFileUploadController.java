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
package io.vertigo.easyforms.impl.runner.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.vertigo.datastore.filestore.FileStoreManager;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.datastore.filestore.model.VFile;
import io.vertigo.easyforms.runner.services.IEasyFormsRunnerServices;
import io.vertigo.ui.core.UiFileInfo;
import io.vertigo.vega.webservice.stereotype.QueryParam;

@Controller
@RequestMapping("/easyforms/form")
public class EasyFormsFileUploadController {

	@Inject
	private IEasyFormsRunnerServices easyFormsRunnerServices;

	@Inject
	private FileStoreManager fileStoreManager;

	@GetMapping("/upload/download/{fileUri}")
	public VFile downloadFile(@PathVariable("fileUri") final FileInfoURI fileInfoUri) {
		return easyFormsRunnerServices.downloadFile(fileInfoUri);
	}

	@GetMapping("/upload")
	public UiFileInfo loadUiFileInfo(@QueryParam("file") final FileInfoURI fileInfoUri) {
		final var fileInfo = fileStoreManager.read(fileInfoUri);
		return new UiFileInfo<>(fileInfo);
	}

	@GetMapping("/upload/fileInfos")
	public List<UiFileInfo> loadUiFileInfos(@QueryParam("file") final List<FileInfoURI> fileInfoUris) {
		return fileInfoUris
				.stream()
				.map(fileStoreManager::read)
				.map(UiFileInfo::new)
				.collect(Collectors.toList());
	}

	@PostMapping("/upload")
	public FileInfoURI uploadFile(@QueryParam("file") final VFile vFile) {
		return easyFormsRunnerServices.saveTmpFile(vFile).getURI();
	}

	@DeleteMapping("/upload")
	public FileInfoURI removeFile(@QueryParam("file") final FileInfoURI fileInfoUri) {
		// fileStoreManager.delete(fileInfoUri);
		// Don't remove file now : it may be needed if user go back before saving
		return fileInfoUri; // if no return, you must get the response. Prefer to return old uri.
	}

}
