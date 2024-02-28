package io.vertigo.easyforms.domain;

import java.util.Arrays;
import java.util.Iterator;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.data.definitions.DataFieldName;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class DtDefinitions implements Iterable<Class<?>> {

	/**
	 * Enumération des DtDefinitions.
	 */
	public enum Definitions {
		/** Objet de données EasyForm. */
		EasyForm(io.vertigo.easyforms.domain.EasyForm.class),
		/** Objet de données EasyFormsFieldTypeUi. */
		EasyFormsFieldTypeUi(io.vertigo.easyforms.domain.EasyFormsFieldTypeUi.class),
		/** Objet de données EasyFormsFieldUi. */
		EasyFormsFieldUi(io.vertigo.easyforms.domain.EasyFormsFieldUi.class),
		/** Objet de données EasyFormsFieldValidatorTypeUi. */
		EasyFormsFieldValidatorTypeUi(io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi.class),
		/** Objet de données EasyFormsTemplateFieldValidatorUi. */
		EasyFormsTemplateFieldValidatorUi(io.vertigo.easyforms.domain.EasyFormsTemplateFieldValidatorUi.class)		;

		private final Class<?> clazz;

		private Definitions(final Class<?> clazz) {
			this.clazz = clazz;
		}

		/** 
		 * Classe associée.
		 * @return Class d'implémentation de l'objet 
		 */
		public Class<?> getDtClass() {
			return clazz;
		}
	}

	/**
	 * Enumération des champs de EasyForm.
	 */
	public enum EasyFormFields implements DataFieldName<io.vertigo.easyforms.domain.EasyForm> {
		/** Propriété 'Id'. */
		efoId,
		/** Propriété 'Template'. */
		template	}

	/**
	 * Enumération des champs de EasyFormsFieldTypeUi.
	 */
	public enum EasyFormsFieldTypeUiFields implements DataFieldName<io.vertigo.easyforms.domain.EasyFormsFieldTypeUi> {
		/** Propriété 'Field type'. */
		name,
		/** Propriété 'Field type category'. */
		category,
		/** Propriété 'Label'. */
		label,
		/** Propriété 'UI component name'. */
		uiComponentName,
		/** Propriété 'UI parameters'. */
		uiParameters,
		/** Propriété 'UI configuration template'. */
		paramTemplate,
		/** Propriété 'Have configuration'. */
		hasTemplate	}

	/**
	 * Enumération des champs de EasyFormsFieldUi.
	 */
	public enum EasyFormsFieldUiFields implements DataFieldName<io.vertigo.easyforms.domain.EasyFormsFieldUi> {
		/** Propriété 'Field code'. */
		fieldCode,
		/** Propriété 'Field type'. */
		fieldType,
		/** Propriété 'Field type'. */
		fieldTypeLabel,
		/** Propriété 'Label'. */
		label,
		/** Propriété 'Tooltip'. */
		tooltip,
		/** Propriété 'System field'. */
		isDefault,
		/** Propriété 'Mandatory'. */
		isMandatory,
		/** Propriété 'Parameters'. */
		parameters,
		/** Propriété 'Validators'. */
		fieldValidators,
		/** Propriété 'Validators'. */
		fieldValidatorSelection	}

	/**
	 * Enumération des champs de EasyFormsFieldValidatorTypeUi.
	 */
	public enum EasyFormsFieldValidatorTypeUiFields implements DataFieldName<io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi> {
		/** Propriété 'Validator type name'. */
		name,
		/** Propriété 'Label'. */
		label,
		/** Propriété 'Description'. */
		description,
		/** Propriété 'UI configuration template'. */
		paramTemplate,
		/** Propriété 'Attached to fields'. */
		fieldTypes	}

	/**
	 * Enumération des champs de EasyFormsTemplateFieldValidatorUi.
	 */
	public enum EasyFormsTemplateFieldValidatorUiFields implements DataFieldName<io.vertigo.easyforms.domain.EasyFormsTemplateFieldValidatorUi> {
		/** Propriété 'Validator type name'. */
		validatorTypeName,
		/** Propriété 'Label'. */
		label,
		/** Propriété 'Label'. */
		parameterizedLabel,
		/** Propriété 'Description'. */
		description,
		/** Propriété 'Parameters JSON'. */
		parameters	}

	/** {@inheritDoc} */
	@Override
	public Iterator<Class<?>> iterator() {
		return new Iterator<>() {
			private Iterator<Definitions> it = Arrays.asList(Definitions.values()).iterator();

			/** {@inheritDoc} */
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			/** {@inheritDoc} */
			@Override
			public Class<?> next() {
				return it.next().getDtClass();
			}
		};
	}
}
