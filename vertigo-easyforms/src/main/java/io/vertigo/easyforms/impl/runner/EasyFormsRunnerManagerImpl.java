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
package io.vertigo.easyforms.impl.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import io.vertigo.account.security.UserSession;
import io.vertigo.account.security.VSecurityManager;
import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.locale.LocaleManager;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.core.node.config.discovery.NotDiscoverable;
import io.vertigo.core.param.ParamValue;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.ConstraintException;
import io.vertigo.datamodel.smarttype.definitions.FormatterException;
import io.vertigo.datastore.filestore.definitions.FileInfoDefinition;
import io.vertigo.datastore.filestore.model.FileInfo;
import io.vertigo.datastore.filestore.model.VFile;
import io.vertigo.datastore.impl.filestore.model.AbstractFileInfo;
import io.vertigo.easyforms.impl.runner.pack.EfPackResources;
import io.vertigo.easyforms.impl.runner.pack.constraint.EfConstraintResources;
import io.vertigo.easyforms.impl.runner.pack.formatter.EfFormatterResources;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.data.EasyFormsDataDescriptor;

@NotDiscoverable
public final class EasyFormsRunnerManagerImpl implements EasyFormsRunnerManager, Activeable {

	private static final String FORMAT_ERROR_MEASURE = "formatError";
	private static final String VALIDATION_ERROR_MEASURE = "validationError";

	private final LocaleManager localeManager;
	private final AnalyticsManager analyticsManager;
	private final SmartTypeManager smartTypeManager;
	private final VSecurityManager securityManager;

	private final String filestoreName;
	private final String tmpFilestoreName;

	private final List<String> languages;

	private FileInfoDefinition stdFileInfoDefinition;
	private FileInfoDefinition tmpFileInfoDefinition;

	@Inject
	public EasyFormsRunnerManagerImpl(
			final LocaleManager localeManager,
			final AnalyticsManager analyticsManager,
			final SmartTypeManager smartTypeManager,
			final VSecurityManager securityManager,
			@ParamValue("filestore.persist") final Optional<String> filestoreNameOpt,
			@ParamValue("filestore.tmp") final Optional<String> tmpFilestoreNameOpt,
			@ParamValue("languages") final Optional<String> languagesOpt) {

		Assertion.check()
				.isNotNull(localeManager)
				.isNotNull(analyticsManager)
				.isNotNull(smartTypeManager)
				.isNotNull(securityManager)
				.isNotNull(filestoreNameOpt)
				.isNotNull(tmpFilestoreNameOpt)
				.isNotNull(languagesOpt);

		this.localeManager = localeManager;
		this.analyticsManager = analyticsManager;
		this.smartTypeManager = smartTypeManager;
		this.securityManager = securityManager;

		filestoreName = filestoreNameOpt.orElse("main");
		tmpFilestoreName = tmpFilestoreNameOpt.orElse("main");

		languages = List.of(languagesOpt.orElse("fr").trim().split("\\s*,\\s*"));
	}

	@Override
	public void start() {
		final var fiDefList = Node.getNode().getDefinitionSpace().getAll(FileInfoDefinition.class);
		stdFileInfoDefinition = fiDefList.stream().filter(d -> d.getStoreName().equals(filestoreName)).findAny()
				.orElseThrow(() -> new VSystemException("No filestore found with name {0}", filestoreName));
		tmpFileInfoDefinition = fiDefList.stream().filter(d -> d.getStoreName().equals(tmpFilestoreName)).findAny()
				.orElseThrow(() -> new VSystemException("No filestore found with name {0}", tmpFilestoreName));

		localeManager.add("io.vertigo.easyforms.runner.Resources", io.vertigo.easyforms.impl.runner.Resources.values());
		localeManager.add("io.vertigo.easyforms.runner.pack.EfPackResources", EfPackResources.values());
		localeManager.add("io.vertigo.easyforms.runner.pack.constraint.EfConstraintResources", EfConstraintResources.values());
		localeManager.add("io.vertigo.easyforms.runner.pack.formatter.EfFormatterResources", EfFormatterResources.values());

		localeManager.add("io.vertigo.easyforms.domain.DtResources", io.vertigo.easyforms.domain.DtResources.values());
	}

	@Override
	public void stop() {
		// Nothing
	}

	@Override
	public List<String> getSupportedLang() {
		return languages;
	}

	@Override
	public String resolveTextForUserlang(final Map<String, String> labels) {
		final var userLang = securityManager.getCurrentUserSession().map(UserSession::getLocale).map(Locale::getLanguage).orElse("fr");
		if (labels == null) {
			return null;
		}
		var value = labels.get(userLang);
		if (value == null) {
			value = labels.get(getSupportedLang().get(0)); // first lang by default
		}
		if (value == null) {
			value = labels.get("i18n");
		}
		return value;
	}

	@Override
	public FileInfo createStdFileInfo(final VFile vFile) {
		return new EasyFormStdFileInfo(vFile);
	}

	@Override
	public FileInfo createTmpFileInfo(final VFile vFile) {
		return new EasyFormTmpFileInfo(vFile);
	}

	@Override
	public boolean isTmpFileInfo(final FileInfoDefinition fileInfoDefinition) {
		return fileInfoDefinition.equals(tmpFileInfoDefinition);
	}

	@Override
	public boolean isStdFileInfo(final FileInfoDefinition fileInfoDefinition) {
		return fileInfoDefinition.equals(stdFileInfoDefinition);
	}

	public final class EasyFormStdFileInfo extends AbstractFileInfo {
		/** SerialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Default constructor.
		 *
		 * @param vFile Data of the file
		 */
		public EasyFormStdFileInfo(final VFile vFile) {
			super(stdFileInfoDefinition, vFile);
		}
	}

	public final class EasyFormTmpFileInfo extends AbstractFileInfo {
		/** SerialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Default constructor.
		 *
		 * @param vFile Data of the file
		 */
		public EasyFormTmpFileInfo(final VFile vFile) {
			super(tmpFileInfoDefinition, vFile);
		}
	}

	// *****
	// *** Methods below could be part of a global (non easy form specific) validation manager (or dataManager ?)
	// *****

	@Override
	public Object formatField(final EasyFormsDataDescriptor fieldDescriptor, final Object inputValue) throws FormatterException {
		if (inputValue == null) {
			if (Cardinality.MANY.equals(fieldDescriptor.cardinality())) {
				return List.of();
			}
			return null;
		}

		try {
			return doFormatField(fieldDescriptor, inputValue);
		} catch (final FormatterException e) {
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(FORMAT_ERROR_MEASURE, 1));

			throw e;
		} catch (final Exception e) {
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(FORMAT_ERROR_MEASURE, 1));

			throw new FormatterException(Resources.EfUnknownFormatterError);
		}
	}

	private Object doFormatField(final EasyFormsDataDescriptor fieldDescriptor, final Object inputValue) throws FormatterException {
		switch (fieldDescriptor.cardinality()) {
			case MANY:
				if (!(inputValue instanceof List)) {
					throw new ClassCastException("Value " + inputValue + " must be a list");
				}
				final var inputCollection = (List) inputValue;
				final List<Object> resolvedList = new ArrayList<>(inputCollection.size());
				for (final var elem : inputCollection) {
					resolvedList.add(doFormatValue(fieldDescriptor, elem));
				}
				return resolvedList;
			case ONE:
			case OPTIONAL_OR_NULLABLE:
				return doFormatValue(fieldDescriptor, inputValue);
			default:
				throw new UnsupportedOperationException();
		}
	}

	private Object doFormatValue(final EasyFormsDataDescriptor fieldDescriptor, final Object inputValue) throws FormatterException {
		final var targetJavaClass = fieldDescriptor.smartTypeDefinition().getJavaClass();
		var adapter = smartTypeManager.getTypeAdapters("easyForm").get(targetJavaClass);
		if (adapter == null) {
			adapter = smartTypeManager.getTypeAdapters("ui").get(targetJavaClass);
		}
		if (adapter != null) {
			return adapter.toJava(inputValue, targetJavaClass);
		} else {
			return smartTypeManager.stringToValue(fieldDescriptor.smartTypeDefinition(), inputValue.toString());
		}
	}

	/**
	 * Validates the value (the value type is also automatically checked before) with business constraints.
	 *
	 * @param fieldDescriptor field description
	 * @param value the value
	 */
	@Override
	public final List<String> validateField(final EasyFormsDataDescriptor fieldDescriptor, final Object value, final Map<String, Object> context) {
		// validate data nature (structure)
		try {
			fieldDescriptor.validate(value);
		} catch (final ConstraintException e) {
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(VALIDATION_ERROR_MEASURE, 1));

			return List.of(e.getMessageText().getDisplay());
		}

		// If ok, validate data intention (business rules)
		final List<String> errors = doValidateField(fieldDescriptor, value, context);

		for (final var fieldConstraint : fieldDescriptor.getFieldConstraints()) {
			if (fieldConstraint != null && !fieldConstraint.checkConstraint(value)) {
				errors.add(fieldConstraint.getErrorMessage().getDisplay());
			}
		}

		if (!errors.isEmpty()) {
			analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
					.incMeasure(VALIDATION_ERROR_MEASURE, 1));
		}
		return errors;
	}

	private List<String> doValidateField(final EasyFormsDataDescriptor fieldDescriptor, final Object value, final Map<String, Object> context) {
		switch (fieldDescriptor.cardinality()) {
			case MANY:
				if (value instanceof final List valueList) {
					final var errorList = new ArrayList<String>();
					if (fieldDescriptor.getMinListSize() != null && fieldDescriptor.getMinListSize().intValue() > valueList.size()) {
						errorList.add(LocaleMessageText.of(Resources.EfMinListSize, fieldDescriptor.getMinListSize(), fieldDescriptor.getMinListSize().intValue() > 1 ? "s" : "").getDisplay());
					} else if (fieldDescriptor.getMaxListSize() != null && fieldDescriptor.getMaxListSize().intValue() < valueList.size()) {
						errorList.add(LocaleMessageText.of(Resources.EfMaxListSize, fieldDescriptor.getMaxListSize(), fieldDescriptor.getMaxListSize().intValue() > 1 ? "s" : "").getDisplay());
					}
					for (final Object element : valueList) {
						errorList.addAll(doCheckConstraints(fieldDescriptor, element, context));
					}
					return errorList;
				} else {
					throw new ClassCastException("Value " + value + " must be a list");
				}
			case ONE:
			case OPTIONAL_OR_NULLABLE:
				return doCheckConstraints(fieldDescriptor, value, context);
			default:
				throw new UnsupportedOperationException();
		}
	}

	private List<String> doCheckConstraints(final EasyFormsDataDescriptor fieldDescriptor, final Object value, final Map<String, Object> context) {
		final var errorList = new ArrayList<String>();
		for (final Constraint constraint : fieldDescriptor.getBusinessConstraints()) {
			if (!constraint.checkConstraint(value)) {
				errorList.add(constraint.getErrorMessage().getDisplay());
			}
		}
		return errorList;
	}

}
