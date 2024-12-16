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
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.vertigo.core.lang.VSystemException;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.datastore.filestore.model.VFile;
import io.vertigo.easyforms.runner.services.IEasyFormsRunnerServices;
import io.vertigo.ui.core.UiFileInfo;
import io.vertigo.ui.impl.springmvc.argumentresolvers.ViewAttribute;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;
import io.vertigo.vega.webservice.stereotype.QueryParam;

@Controller
@RequestMapping("/easyforms/form")
public class EasyFormsFileUploadController {

	@Inject
	private IEasyFormsRunnerServices easyFormsRunnerServices;

	@GetMapping("/upload/download/{fileUri}")
	public VFile downloadFile(@PathVariable("fileUri") final FileInfoURI fileInfoUri) {
		return easyFormsRunnerServices.downloadFile(fileInfoUri);
	}

	@GetMapping("/upload")
	public UiFileInfo loadUiFileInfo(@QueryParam("file") final FileInfoURI fileInfoUri) {
		return easyFormsRunnerServices.getFileInfo(fileInfoUri);
	}

	@GetMapping("/upload/fileInfos")
	public List<UiFileInfo> loadUiFileInfos(@QueryParam("file") final List<FileInfoURI> fileInfoUris) {
		return easyFormsRunnerServices.getFileInfos(fileInfoUris);
	}

	@DeleteMapping("/upload")
	public FileInfoURI removeFile(@QueryParam("file") final FileInfoURI fileInfoUri) {
		// fileStoreManager.delete(fileInfoUri);
		// Don't remove file now : it may be needed if user go back before saving
		return fileInfoUri; // if no return, you must get the response. Prefer to return old uri.
	}

	@Controller
	@RequestMapping("/easyforms/form")
	public class EasyFormsFileUploadControllerWithContext extends AbstractVSpringMvcController {

		@PostMapping("/upload")
		public FileInfoURI uploadFile(
				@ViewAttribute("efoSupportedFileExtensions") final Set<String> acceptedExtensions,
				@ViewAttribute("efoMaxFileFileSize") final Long maxFileSize,
				@QueryParam("file") final VFile vFile) {

			if (!acceptedExtensions.isEmpty()) {
				// we have to check the file extension
				final var fileName = vFile.getFileName().toLowerCase();
				final var dotIndex = fileName.lastIndexOf('.');
				if (dotIndex == -1 || !acceptedExtensions.contains(fileName.substring(dotIndex))) {
					throw new VSystemException("File extension not allowed");
				}
			}

			if (maxFileSize != -1L && vFile.getLength() > maxFileSize * 1024 * 1024) {
				throw new VSystemException("File size too big");
			}
			return easyFormsRunnerServices.saveTmpFile(vFile).getURI();
		}

	}

}
