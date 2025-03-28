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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertigo.account.security.UserSession;
import io.vertigo.account.security.VSecurityManager;
import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.criteria.Criterions;
import io.vertigo.datamodel.data.definitions.DataFieldName;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.DtListState;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.data.util.DataModelUtil;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.FormatterException;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.datastore.filestore.FileStoreManager;
import io.vertigo.datastore.filestore.model.FileInfo;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.datastore.filestore.model.VFile;
import io.vertigo.datastore.kvstore.KVStoreManager;
import io.vertigo.easyforms.dao.EasyFormDAO;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormFields;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.data.EasyFormsDataDescriptor;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldValidatorTypeDefinition;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateFieldValidator;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateSection;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemBlock;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.model.ui.EasyFormsListItem;
import io.vertigo.easyforms.runner.rule.EasyFormsRuleParser;
import io.vertigo.easyforms.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.util.ObjectUtil;
import io.vertigo.ui.core.AbstractUiListUnmodifiable;
import io.vertigo.ui.core.ProtectedValueUtil;
import io.vertigo.ui.core.UiFileInfo;
import io.vertigo.ui.impl.springmvc.util.UiRequestUtil;
import io.vertigo.vega.webservice.validation.UiMessageStack;

@Transactional
public class EasyFormsRunnerServices implements Component {

	private static final Logger LOG = LogManager.getLogger(EasyFormsRunnerServices.class);

	@Inject
	private EasyFormsRunnerManager easyFormsRunnerManager;

	@Inject
	private EasyFormDAO easyFormDAO;

	@Inject
	private FileStoreManager fileStoreManager;

	@Inject
	private SmartTypeManager smartTypeManager;

	@Inject
	private KVStoreManager kvStoreManager;

	@Inject
	private VSecurityManager securityManager;

	/**
	 * Retrieves an EasyForm by its unique identifier.
	 *
	 * @param efoUid The unique identifier of the EasyForm.
	 * @return The EasyForm associated with the given unique identifier.
	 */
	public EasyForm getEasyFormById(final UID<EasyForm> efoUid) {
		Assertion.check().isNotNull(efoUid);
		//---
		return easyFormDAO.get(efoUid);
	}

	/**
	 * Retrieves a list of EasyForms by their unique identifiers.
	 *
	 * @param efoUids The unique identifiers of the EasyForms.
	 * @return A list of EasyForms associated with the given unique identifiers.
	 */
	public DtList<EasyForm> getEasyFormListByIds(final Collection<UID<EasyForm>> efoUids) {
		Assertion.check().isNotNull(efoUids);
		//---
		final var ids = efoUids.stream()
				.map(UID::getId)
				.toArray(Long[]::new);
		// find all does not guarantee the order, so we need to sort it back to the original order
		final var easyFormMap = easyFormDAO.findAll(Criterions.in(EasyFormFields.efoId, (Serializable[]) ids), DtListState.of(null))
				.stream()
				.collect(Collectors.toMap(EasyForm::getEfoId, ef -> ef));
		final DtList<EasyForm> easyForms = new DtList<>(EasyForm.class);
		for (final var id : ids) {
			easyForms.add(easyFormMap.get(id));
		}
		return easyForms;
	}

	/**
	 * Formats and checks the form data.
	 *
	 * @param formOwner The owner of the form.
	 * @param formDataFieldName The field name of the form data.
	 * @param formTempalte The template of the form.
	 * @param uiMessageStack The stack of UI messages.
	 * @param contextValues The context values.
	 */
	public <E extends DataObject> void formatAndCheckFormulaire(final E formOwner, final DataFieldName<E> formDataFieldName, final EasyFormsTemplate formTempalte,
			final UiMessageStack uiMessageStack, final Map<String, Serializable> contextValues) {
		final var formDataRaw = DataModelUtil.findDataDefinition(formOwner).getField(formDataFieldName).getDataAccessor().getValue(formOwner);

		Assertion.check()
				.isFalse(formTempalte.getSections() == null || formTempalte.getSections().isEmpty(), "No form")
				.isFalse(StringUtil.isBlank(formTempalte.getSections().get(0).getCode()) && formTempalte.getSections().size() > 1, "If default section, it must be the only one")
				.isTrue(EasyFormsData.class.isAssignableFrom(formDataRaw.getClass()), "formDataFieldName must point to an EasyFormsData object");

		final EasyFormsData formData = (EasyFormsData) formDataRaw;
		final EasyFormsData formattedFormData = new EasyFormsData();
		for (final var section : formTempalte.getSections()) {
			// test section condition, else continue;
			if (!StringUtil.isBlank(section.getCondition())) {
				final var result = EasyFormsRuleParser.parseComparison(section.getCondition(), formattedFormData, contextValues);
				if (!result.isValid()) {
					LOG.error("Error parsing condition : '{}'. {}", section.getCondition(), result.getErrorMessage());
					continue; // assuming invalid condition is false
				} else if (!result.getResult()) {
					if (section.getCode() != null) {
						formattedFormData.put(section.getCode(), new EasyFormsData());
					}
					continue;
				}
			}

			final EasyFormsData formDataSection;
			final EasyFormsData formattedFormDataSection;
			if (section.getCode() == null) { // no section
				formDataSection = formData;
				formattedFormDataSection = formattedFormData;
			} else {
				formDataSection = new EasyFormsData((Map<String, Object>) formData.get(section.getCode()));
				formattedFormDataSection = new EasyFormsData();
				formattedFormData.put(section.getCode(), formattedFormDataSection);
			}

			for (final var elem : section.getItems()) {
				if (elem instanceof final EasyFormsTemplateItemBlock block) {
					// test block condition, else continue;
					if (!StringUtil.isBlank(block.getCondition())) {
						final var result = EasyFormsRuleParser.parseComparison(block.getCondition(), formattedFormData, contextValues);
						if (!result.isValid()) {
							LOG.error("Error parsing condition : '{}'. {}", block.getCondition(), result.getErrorMessage());
							continue; // assuming invalid condition is false
						} else if (!result.getResult()) {
							continue;
						}
					}

					for (final var elem2 : block.getItems()) {
						if (elem2 instanceof final EasyFormsTemplateItemField field) {
							final var formattedValue = formatAndCheckField(formTempalte, formDataFieldName.name(), section, field, formDataSection, contextValues, formOwner, uiMessageStack);
							formattedFormDataSection.put(field.getCode(), formattedValue);
						}
					}
				} else if (elem instanceof final EasyFormsTemplateItemField field) {
					final var formattedValue = formatAndCheckField(formTempalte, formDataFieldName.name(), section, field, formDataSection, contextValues, formOwner, uiMessageStack);
					formattedFormDataSection.put(field.getCode(), formattedValue);
				}
			}
		}

		formData.clear();
		formData.putAll(formattedFormData);

		if (uiMessageStack.hasErrors()) {
			// on error, we put back default values on hidden fields as we can continue to edit the form and therefore we need default values on these fields

			setDefaultValuesOnHidden(formTempalte, formData, contextValues);
		}
	}

	private Object formatAndCheckField(final EasyFormsTemplate formTempalte, final String formDataFieldName, final EasyFormsTemplateSection section, final EasyFormsTemplateItemField field,
			final EasyFormsData formData, final Map<String, Serializable> contextValues, final DataObject formOwner, final UiMessageStack uiMessageStack) {

		final var fieldCode = formDataFieldName + "--" + (formTempalte.useSections() ? section.getCode() + "--" : "") + field.getCode().replace("_", ""); // remove _ as it is reserved for qualifiers

		// format field (eg: Put last name in upper case)
		final var inputValue = formData.get(field.getCode());
		return formatAndCheckSingleField(formOwner, fieldCode, field, inputValue, formData, contextValues, uiMessageStack);
	}

	/**
	 * Formats and checks a single field of the form data.
	 *
	 * @param formOwner The owner of the form.
	 * @param fieldCode The code of the field.
	 * @param field The field to format and check.
	 * @param inputValue The value of the field.
	 * @param formData The form data.
	 * @param contextValues The context values.
	 * @param uiMessageStack The stack of UI messages.
	 * @return The formatted and checked value of the field.
	 */
	public Object formatAndCheckSingleField(final DataObject formOwner, final String fieldCode, final EasyFormsTemplateItemField field, final Object inputValue, final EasyFormsData formData,
			final Map<String, Serializable> contextValues, final UiMessageStack uiMessageStack) {
		final EasyFormsDataDescriptor fieldDescriptor = fieldToDataDescriptor(field);
		if (fieldDescriptor.isComputed()) {
			final var parseResult = EasyFormsRuleParser.parseCompute(field.getDefaultValue().toString(), formData, contextValues);
			if (parseResult.isValid()) {
				return parseResult.getResult();
			}
			uiMessageStack.error(parseResult.getErrorMessage(), formOwner, fieldCode);
			return inputValue;
		}
		Object typedValue;
		try {
			typedValue = easyFormsRunnerManager.formatField(fieldDescriptor, inputValue);
		} catch (final FormatterException e) {
			uiMessageStack.error(e.getMessageText().getDisplay(), formOwner, fieldCode);
			return inputValue;
		}

		// if formatter succeed, validate constraints
		final var errors = easyFormsRunnerManager.validateField(fieldDescriptor, typedValue);
		for (final var error : errors) {
			uiMessageStack.error(error, formOwner, fieldCode);
		}
		return typedValue;
	}

	private EasyFormsDataDescriptor fieldToDataDescriptor(final EasyFormsTemplateItemField field) {
		final var fieldType = EasyFormsFieldTypeDefinition.resolve(field.getFieldTypeName());
		final var smartTypeDefinition = getSmartTypeByName(fieldType.getSmartTypeName());
		final var cardinality = fieldType.isList() ? Cardinality.MANY : field.isMandatory() ? Cardinality.ONE : Cardinality.OPTIONAL_OR_NULLABLE;

		final List<Constraint> constraints;
		if (field.getValidators() == null) {
			constraints = List.of(); // defensive code as JSON could be missing this attribute
		} else {
			constraints = field.getValidators().stream()
					.map(EasyFormsRunnerServices::validatorToConstraint)
					.toList();
		}

		return new EasyFormsDataDescriptor(field.getCode(), smartTypeDefinition, cardinality, fieldType.getConstraints(field), constraints,
				fieldType.isList() && field.isMandatory() ? 1 : null, fieldType.isList() ? field.getMaxItems() : null, fieldType.getMinListSizeResource(), fieldType.getMaxListSizeResource(),
				fieldType.isComputed());
	}

	private static SmartTypeDefinition getSmartTypeByName(final String nomSmartType) {
		return Node.getNode().getDefinitionSpace().resolve(nomSmartType, SmartTypeDefinition.class);
	}

	/**
	 * Persists the files from the new form data and removes any files from the old form data that are not present in the new form data.
	 *
	 * @param oldDataOpt The old form data.
	 * @param newData The new form data.
	 */
	public void persistFiles(final Optional<EasyFormsData> oldDataOpt, final EasyFormsData newData) {
		final var oldData = oldDataOpt.orElse(new EasyFormsData());

		// get all old files
		final List<FileInfoURI> oldFiles = new ArrayList<>();
		forEachFiles(oldData, f -> {
			oldFiles.add(f);
			return f;
		});

		final List<FileInfoURI> newFiles = new ArrayList<>();
		forEachFiles(newData, f -> {
			// persist new files
			if (easyFormsRunnerManager.isTmpFileInfo(f.getDefinition())) {
				final FileInfo file = easyFormsRunnerManager.createStdFileInfo(fileStoreManager.read(f).getVFile());
				final FileInfoURI newUri = fileStoreManager.create(file).getURI();
				updateProtectedValue(f, newUri);
				oldFiles.add(f);
				newFiles.add(newUri);
				return newUri;
			}
			newFiles.add(f);
			return f;
		});

		// remove files not in new data
		oldFiles.removeAll(newFiles);
		for (final FileInfoURI fileInfoURI : oldFiles) {
			fileStoreManager.delete(fileInfoURI);
		}
	}

	// On autosave, we persist the file but we can't update vueData, so we keep the old protected value and update it to point to the new file
	private void updateProtectedValue(final FileInfoURI oldUri, final FileInfoURI newUri) {
		final var oldProtectedString = ProtectedValueUtil.generateProtectedValue(oldUri);
		kvStoreManager.put(ProtectedValueUtil.PROTECTED_VALUE_COLLECTION_NAME, oldProtectedString + getSessionIdIfExists(), newUri);
	}

	private String getSessionIdIfExists() {
		return securityManager.getCurrentUserSession()
				.map(UserSession::getSessionUUID)
				.map(u -> "-" + u)
				.orElse("");
	}

	private void forEachFiles(final EasyFormsData data, final UnaryOperator<FileInfoURI> fileProcessor) {
		for (final var section : data.values()) {
			for (final var entry : ((AbstractMap<String, Object>) section).entrySet()) {
				if (entry.getValue() instanceof final FileInfoURI f) {
					entry.setValue(fileProcessor.apply(f));
				} else if (entry.getValue() instanceof final List l) {
					final var iterator = l.listIterator();

					while (iterator.hasNext()) {
						final var item = iterator.next();
						if (item instanceof final FileInfoURI f) {
							iterator.set(fileProcessor.apply(f));
						}
					}
				}
			}
		}
	}

	private static Constraint validatorToConstraint(final EasyFormsTemplateFieldValidator validator) {
		final var validatorType = Node.getNode().getDefinitionSpace().resolve(validator.getName(), EasyFormsFieldValidatorTypeDefinition.class);
		// no use of validator parameters for now
		return validatorType.getConstraint();
	}

	/**
	 * Retrieves the default data values for a given form template.
	 *
	 * @param easyFormsTemplate The form template.
	 * @param contextValues The context values.
	 * @return The default data values for the given form template.
	 */
	public EasyFormsData getDefaultDataValues(final EasyFormsTemplate easyFormsTemplate, final Map<String, Serializable> contextData) {
		final var templateDefaultData = new EasyFormsData();

		for (final var section : easyFormsTemplate.getSections()) {
			final Map<String, Object> sectionData;
			if (easyFormsTemplate.useSections()) {
				sectionData = new HashMap<>();
				templateDefaultData.put(section.getCode(), sectionData);
			} else {
				sectionData = templateDefaultData;
			}

			for (final var field : section.getAllFields()) {
				if (field.getDefaultValue() != null) {
					// default value is set on field
					sectionData.put(field.getCode(), ObjectUtil.resolveDefaultValue(field.getDefaultValue(), templateDefaultData, contextData));
				} else {
					// default value is set on field type
					final var paramFieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
					if (paramFieldTypeDefinition.getDefaultValue() != null) {
						sectionData.put(field.getCode(), ObjectUtil.resolveDefaultValue(paramFieldTypeDefinition.getDefaultValue(), templateDefaultData, contextData));
					}
				}
			}
		}
		return templateDefaultData;
	}

	/**
	 * Sets the default values on hidden fields.
	 *
	 * @param formTempalte The form template.
	 * @param formData The form data
	 * @param contextValues The context values.
	 */
	public void setDefaultValuesOnHidden(final EasyFormsTemplate formTempalte, final EasyFormsData formData, final Map<String, Serializable> contextData) {
		for (final var section : formTempalte.getSections()) {
			boolean isSectionVisible = true;
			if (!StringUtil.isBlank(section.getCondition())) {
				final var result = EasyFormsRuleParser.parseComparison(section.getCondition(), formData, contextData);
				if (!result.isValid() || Boolean.FALSE.equals(result.getResult())) {
					isSectionVisible = false;
				}
			}
			final var fields = section.getAllFields();
			if (isSectionVisible) {
				fields.removeAll(section.getAllDisplayedFields(formData, contextData));
			}

			final var sectionData = (Map<String, Object>) formData.get(section.getCode());
			for (final var field : fields) {
				if (field.getDefaultValue() != null) {
					// default value is set on field
					sectionData.put(field.getCode(), ObjectUtil.resolveDefaultValue(field.getDefaultValue(), formData, contextData));
				} else {
					// default value is set on field type
					final var paramFieldTypeDefinition = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
					if (paramFieldTypeDefinition.getDefaultValue() != null) {
						sectionData.put(field.getCode(), ObjectUtil.resolveDefaultValue(paramFieldTypeDefinition.getDefaultValue(), formData, contextData));
					}
				}
			}
		}
	}

	/**
	 * Creates a temporary FileInfo from a given VFile.
	 *
	 * @param vFile The VFile.
	 * @return The temporary FileInfo.
	 */
	public FileInfo createTmpFileInfo(final VFile vFile) {
		return easyFormsRunnerManager.createTmpFileInfo(vFile);
	}

	/**
	 * Saves a temporary file from a given VFile.
	 *
	 * @param vFile The VFile.
	 * @return The FileInfo of the saved file.
	 */
	public FileInfo saveTmpFile(final VFile vFile) {
		final var tmpFile = createTmpFileInfo(vFile);
		return fileStoreManager.create(tmpFile);
	}

	/**
	 * Downloads a file from a given FileInfoURI.
	 *
	 * @param fileInfoUri The FileInfoURI of the file to download.
	 * @return The downloaded VFile.
	 */
	public VFile downloadFile(final FileInfoURI fileInfoUri) {
		final var fileInfo = fileStoreManager.read(fileInfoUri);
		return fileInfo.getVFile();
	}

	/**
	 * Retrieves the list of UiFileInfo from a given list of FileInfoURI.
	 *
	 * @param fileInfoUris The list of FileInfoURI.
	 * @return The list of UiFileInfo.
	 */
	public List<UiFileInfo> getFileInfos(final List<FileInfoURI> fileInfoUris) {
		return fileInfoUris
				.stream()
				.map(this::getFileInfo)
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves the UiFileInfo from a given FileInfoURI.
	 *
	 * @param fileInfoUri The FileInfoURI.
	 * @return The UiFileInfo
	 */
	public UiFileInfo getFileInfo(final FileInfoURI fileInfoUri) {
		final var fileInfo = fileStoreManager.read(fileInfoUri);
		return new UiFileInfo<>(fileInfo);
	}

	/**
	 * Retrieves the read form of an EasyForm from a given template and data.
	 *
	 * @param easyFormsTemplate The form template.
	 * @param easyForm The form data.
	 * @param context The context values.
	 * @param addEmptyFields Whether to add empty fields.
	 * @return The read form of the EasyForm. We uses a LinkedHashMap to keep the order of the fields.
	 */
	public LinkedHashMap<String, LinkedHashMap<String, Object>> getEasyFormRead(final EasyFormsTemplate easyFormsTemplate, final EasyFormsData easyForm, final Map<String, Serializable> context,
			final boolean addEmptyFields) {
		final var easyFormDisplay = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
		final var outOfSections = new LinkedHashSet<>(easyForm.keySet());

		processSections(easyFormsTemplate, easyForm, context, easyFormDisplay, outOfSections, addEmptyFields);
		processOldSections(easyForm, easyFormDisplay, outOfSections);

		return easyFormDisplay;
	}

	private void processSections(final EasyFormsTemplate easyFormsTemplate, final EasyFormsData easyForm, final Map<String, Serializable> context,
			final LinkedHashMap<String, LinkedHashMap<String, Object>> easyFormDisplay, final Set<String> outOfSections, final boolean addEmptyFields) {
		// We use display order from template
		for (final EasyFormsTemplateSection section : easyFormsTemplate.getSections()) {
			outOfSections.remove(section.getCode());
			if (!StringUtil.isBlank(section.getCondition())) {
				final var result = EasyFormsRuleParser.parseComparison(section.getCondition(), easyForm, context);
				if (!result.isValid() || Boolean.FALSE.equals(result.getResult())) {
					continue;
				}
			}
			final var easyFormSectionData = (Map<String, Object>) easyForm.get(section.getCode());

			if (easyFormSectionData != null || addEmptyFields) {
				final var sectionDisplay = new LinkedHashMap<String, Object>();
				easyFormDisplay.put(easyFormsRunnerManager.resolveTextForUserlang(section.getLabel()), sectionDisplay);
				final var outOfSectionData = new HashMap<>(easyFormSectionData);

				processFieldsInSection(section, easyForm, context, easyFormSectionData, sectionDisplay, outOfSectionData, addEmptyFields);
				processOldSectionData(outOfSectionData, sectionDisplay);
			}
		}
	}

	private void processFieldsInSection(final EasyFormsTemplateSection section, final EasyFormsData easyForm, final Map<String, Serializable> context, final Map<String, Object> easyFormSectionData,
			final LinkedHashMap<String, Object> sectionDisplay, final Map<String, Object> outOfSectionData, final boolean addEmptyFields) {
		for (final EasyFormsTemplateItemField field : section.getAllDisplayedFields(easyForm, context)) {
			final var fieldCode = field.getCode();
			final Object rawValue = easyFormSectionData == null ? null : easyFormSectionData.get(fieldCode);

			if (rawValue != null || addEmptyFields) {
				processField(field, rawValue, sectionDisplay);
				outOfSectionData.remove(fieldCode);
			}
		}
	}

	private void processField(final EasyFormsTemplateItemField field, final Object rawValue, final LinkedHashMap<String, Object> sectionDisplay) {
		final var fieldType = Node.getNode().getDefinitionSpace().resolve(field.getFieldTypeName(), EasyFormsFieldTypeDefinition.class);
		final var resolvedParameters = EasyFormsData.combine(fieldType.getUiParameters(), field.getParameters());
		final String listSupplier = (String) resolvedParameters.get(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER);

		if (listSupplier == null) {
			processBasicValue(field, rawValue, sectionDisplay, fieldType);
		} else {
			processValueSelectedFromList(field, rawValue, sectionDisplay, resolvedParameters);
		}
	}

	private void processBasicValue(final EasyFormsTemplateItemField field, final Object rawValue,
			final LinkedHashMap<String, Object> sectionDisplay, final EasyFormsFieldTypeDefinition fieldType) {
		final var smartType = Node.getNode().getDefinitionSpace().resolve(fieldType.getSmartTypeName(), SmartTypeDefinition.class);
		if (rawValue instanceof final List<?> rawList) {
			final var displayList = new ArrayList<>(rawList.size());
			for (final var raw : rawList) {
				final String displayValue = valueToString(smartType, raw);
				if (raw instanceof final FileInfoURI fileInfoURI) {
					displayList.add(getFileObj(displayValue, fileInfoURI));
				} else {
					displayList.add(getStrObj(displayValue));
				}
			}
			sectionDisplay.put(easyFormsRunnerManager.resolveTextForUserlang(field.getLabel()), displayList);
		} else {
			final String displayValue = valueToString(smartType, rawValue);
			sectionDisplay.put(easyFormsRunnerManager.resolveTextForUserlang(field.getLabel()), getStrObj(displayValue));
		}
	}

	private void processValueSelectedFromList(final EasyFormsTemplateItemField field, final Object rawValue,
			final LinkedHashMap<String, Object> sectionDisplay, final EasyFormsData resolvedParameters) {
		if (rawValue instanceof final List<?> rawList) {
			// multi-selection
			sectionDisplay.put(
					easyFormsRunnerManager.resolveTextForUserlang(field.getLabel()),
					getStrObj(
							rawList.stream()
									.map(i -> getListDisplayValue(resolvedParameters, i))
									.collect(Collectors.joining(", "))));

		} else {
			// single selection
			sectionDisplay.put(easyFormsRunnerManager.resolveTextForUserlang(field.getLabel()),
					getStrObj(getListDisplayValue(resolvedParameters, rawValue)));
		}
	}

	private void processOldSectionData(final Map<String, Object> outOfSectionData, final LinkedHashMap<String, Object> sectionDisplay) {
		// add old section data (code + value)
		for (final Entry<String, Object> champ : outOfSectionData.entrySet()) {
			sectionDisplay.put(champ.getKey() + " (old)", getStrObj(String.valueOf(champ.getValue())));
		}
	}

	private void processOldSections(final EasyFormsData easyForm, final LinkedHashMap<String, LinkedHashMap<String, Object>> easyFormDisplay, final Set<String> outOfSections) {
		// add old sections
		for (final String oldSection : outOfSections) {
			final var oldSectionData = (Map<String, Object>) easyForm.get(oldSection);
			final var sectionDisplay = new LinkedHashMap<String, Object>();
			easyFormDisplay.put(oldSection + " (old)", sectionDisplay);

			for (final Entry<String, Object> champ : oldSectionData.entrySet()) {
				sectionDisplay.put(champ.getKey(), getStrObj(String.valueOf(champ.getValue()))); // TODO manage FileInfoURI
			}
		}
	}

	private static Map<String, String> getStrObj(final String value) {
		return Map.of("label", value);
	}

	private Map<String, String> getFileObj(final String urn, final FileInfoURI fileInfoURI) {
		final var fileInfo = fileStoreManager.read(fileInfoURI);

		return Map.of("label", fileInfo.getVFile().getFileName(), "urn", urn);
	}

	private String valueToString(final SmartTypeDefinition smartType, final Object inputValue) {
		final var targetJavaClass = smartType.getJavaClass();
		if (!targetJavaClass.equals(String.class) && inputValue instanceof String) {
			return (String) inputValue; // not a valid value, we display it as is
		}
		var adapter = smartTypeManager.getTypeAdapters("easyForm").get(targetJavaClass);
		if (adapter == null) {
			adapter = smartTypeManager.getTypeAdapters("ui").get(targetJavaClass);
		}
		if (adapter != null) {
			return (String) adapter.toBasic(inputValue);
		} else {
			return smartTypeManager.valueToString(smartType, inputValue);
		}
	}

	private String getListDisplayValue(final EasyFormsData resolvedParameters, final Object rawValue) {
		if (rawValue == null) {
			return "";
		}

		final String listSupplier = (String) resolvedParameters.get(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER);
		final var ctxNameOpt = resolveCtxName(listSupplier);
		if (ctxNameOpt.isPresent()) {
			return getValueFromContext(ctxNameOpt.get(), rawValue);
		} else {
			return EasyFormsListItem.ofCollection(resolvedParameters.getOrDefault(listSupplier, List.of()))
					.stream()
					.filter(item -> Objects.equals(item.value(), rawValue))
					.map(EasyFormsListItem::getDisplayLabel)
					.findFirst()
					.orElse(rawValue.toString());
		}
	}

	/**
	 * Resolves the context name from a given list supplier.
	 *
	 * @param listSupplier The list supplier.
	 * @return The context name, if it can be resolved.
	 */
	public Optional<String> resolveCtxName(final String listSupplier) {
		if (listSupplier.startsWith(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_REF_PREFIX)) {
			final var entityName = listSupplier.substring(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_REF_PREFIX.length());
			return Optional.of(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_REF_CTX_NAME_PREFIX + entityName);
		} else if (listSupplier.startsWith(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_CTX_PREFIX)) {
			return Optional.of(listSupplier.substring(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_CTX_PREFIX.length()));
		}
		return Optional.empty();
	}

	private String getValueFromContext(final String ctxKey, final Object value) {
		final var uiList = (AbstractUiListUnmodifiable<?>) UiRequestUtil.getCurrentViewContext().getUiList(() -> ctxKey);
		final var dtDefinition = uiList.getDtDefinition();
		final var idField = dtDefinition.getIdField().get();
		final var displayField = dtDefinition.getDisplayField().get();

		return uiList.getById(idField.name(), (Serializable) value).getSingleInputValue(displayField.name());
	}

}
