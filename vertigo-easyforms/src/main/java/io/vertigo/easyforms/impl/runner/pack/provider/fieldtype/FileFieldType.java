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
package io.vertigo.easyforms.impl.runner.pack.provider.fieldtype;

import java.util.List;
import java.util.function.Function;

import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datastore.filestore.FileStoreManager;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.easyforms.impl.runner.Resources;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.constraint.EasyFormsConstraint;
import io.vertigo.easyforms.impl.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.FileUiComponent;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public class FileFieldType implements IEasyFormsFieldTypeDefinitionSupplier {

	@Override
	public EasyFormsSmartTypes getSmartType() {
		return EasyFormsSmartTypes.EfFileInfoURI;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return UiComponentEnum.FILE;
	}

	@Override
	public boolean isList() {
		return true;
	}

	@Override
	public Resources getMaxListSizeResource() {
		return Resources.EfUploadMaxCount;
	}

	@Override
	public Resources getMinListSizeResource() {
		return Resources.EfUploadMinCount;
	}

	@Override
	public List<AbstractEasyFormsTemplateItem> getExposedComponentParams() {
		return List.of(
				new EasyFormsTemplateItemField(FileUiComponent.MAX_FILE_SIZE, FieldTypeEnum.COUNT_STRICT),
				new EasyFormsTemplateItemField(FileUiComponent.MAX_SIZE, FieldTypeEnum.COUNT_STRICT),
				new EasyFormsTemplateItemField(FileUiComponent.ACCEPT, FieldTypeEnum.INTERNAL_EXTENSIONS));
	}

	@Override
	public Function<EasyFormsTemplateItemField, List<Constraint>> getConstraintsProvider() {
		return field -> List.of(
				new MaxFileSizeConstraint(field),
				new MaxSizeConstraint(field),
				new AcceptConstraint(field));
	}

	private static final class MaxFileSizeConstraint implements EasyFormsConstraint<Boolean, List<FileInfoURI>> {
		private final Long maxSizeMo;
		private final Long maxSize;

		public MaxFileSizeConstraint(final EasyFormsTemplateItemField field) {
			if (field.getParameters() == null) {
				maxSizeMo = null;
				maxSize = null;
			} else {
				maxSizeMo = (Long) field.getParameters().get(FileUiComponent.MAX_FILE_SIZE);
				if (maxSizeMo == null) {
					maxSize = null;
				} else {
					maxSize = maxSizeMo * 1024 * 1024;
				}
			}
		}

		@Override
		public boolean checkConstraint(final List<FileInfoURI> value) {
			if (maxSize == null) {
				return true;
			}

			final var fileStoreManager = Node.getNode().getComponentSpace().resolve(FileStoreManager.class);
			for (final FileInfoURI uri : value) {
				if (fileStoreManager.read(uri).getVFile().getLength() > maxSize) {
					return false;
				}
			}

			return true;
		}

		@Override
		public LocaleMessageText getErrorMessage() {
			return LocaleMessageText.of(Resources.EfUploadMaxFileSize, maxSizeMo);
		}
	}

	private static final class MaxSizeConstraint implements EasyFormsConstraint<Boolean, List<FileInfoURI>> {
		private final Long maxSizeMo;
		private final Long maxSize;

		public MaxSizeConstraint(final EasyFormsTemplateItemField field) {
			if (field.getParameters() == null) {
				maxSizeMo = null;
				maxSize = null;
			} else {
				maxSizeMo = (Long) field.getParameters().get(FileUiComponent.MAX_SIZE);
				if (maxSizeMo == null) {
					maxSize = null;
				} else {
					maxSize = maxSizeMo * 1024 * 1024;
				}
			}
		}

		@Override
		public boolean checkConstraint(final List<FileInfoURI> value) {
			if (maxSize == null) {
				return true;
			}

			final var fileStoreManager = Node.getNode().getComponentSpace().resolve(FileStoreManager.class);
			Long totalSize = 0L;
			for (final FileInfoURI uri : value) {
				totalSize += fileStoreManager.read(uri).getVFile().getLength();
			}

			return totalSize <= maxSize;
		}

		@Override
		public LocaleMessageText getErrorMessage() {
			return LocaleMessageText.of(Resources.EfUploadMaxSize, maxSizeMo);
		}
	}

	private static final class AcceptConstraint implements EasyFormsConstraint<Boolean, List<FileInfoURI>> {
		private final String[] extensions;

		public AcceptConstraint(final EasyFormsTemplateItemField field) {
			if (field.getParameters() == null) {
				extensions = null;
			} else {
				final var extensionParam = (String) field.getParameters().get(FileUiComponent.ACCEPT);
				if (extensionParam == null) {
					extensions = null;
				} else {
					extensions = extensionParam.trim().split("\\s*,\\s*");
				}
			}
		}

		@Override
		public boolean checkConstraint(final List<FileInfoURI> value) {
			if (extensions == null || extensions.length == 0) {
				return true;
			}

			final var fileStoreManager = Node.getNode().getComponentSpace().resolve(FileStoreManager.class);
			for (final FileInfoURI uri : value) {
				if (!isAccepted(fileStoreManager.read(uri).getVFile().getFileName())) {
					return false;
				}
			}

			return true;
		}

		@Override
		public LocaleMessageText getErrorMessage() {
			return LocaleMessageText.of(Resources.EfUploadAccept, String.join(", ", extensions));
		}

		private boolean isAccepted(final String fileName) {
			final var fileNameLower = fileName.toLowerCase();
			for (final String extension : extensions) {
				if (fileNameLower.endsWith(extension)) {
					return true;
				}
			}
			return false;
		}
	}

}
