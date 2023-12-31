/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.datamodel.structure.definitions;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.ListBuilder;
import io.vertigo.core.lang.MapBuilder;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionId;
import io.vertigo.core.node.definition.DefinitionPrefix;

/**
 * The DtDefinition class defines the definition of data.
 *
 * @author pchretien
 */
@DefinitionPrefix(DtDefinition.PREFIX)
public final class DtDefinition extends AbstractDefinition<DtDefinition> {
	public static final String PREFIX = "Dt";
	/** the dataSpace must match this pattern. */
	public static final Pattern REGEX_DATA_SPACE = Pattern.compile("[a-z][a-zA-Z0-9]{3,60}");
	public static final String DEFAULT_DATA_SPACE = "main";

	/** if the definition is a fragment. */
	private final Optional<DefinitionId<DtDefinition>> fragmentOpt;

	/** name of the package. */
	private final String packageName;

	/** List of fields.  */
	private final List<DtField> fields;

	/** Map. (fieldName, DtField). */
	private final Map<String, DtField> mappedFields;

	private final DtStereotype stereotype;

	/** id Field */
	private final Optional<DtField> idFieldOpt;

	private final Optional<DtField> sortFieldOpt;
	private final Optional<DtField> displayFieldOpt;
	private final Optional<DtField> handleFieldOpt;
	private final Optional<DtField> keyFieldOpt;

	private final String dataSpace;

	/**
	 * Constructor.
	 */
	DtDefinition(
			final String name,
			final Optional<DefinitionId<DtDefinition>> fragment,
			final String packageName,
			final DtStereotype stereotype,
			final List<DtField> dtFields,
			final String dataSpace,
			final Optional<DtField> sortField,
			final Optional<DtField> displayField,
			final Optional<DtField> handleField,
			final Optional<DtField> keyField) {
		super(name);
		//---
		Assertion.check()
				.isNotNull(fragment)
				.isNotNull(stereotype)
				.isNotNull(dtFields)
				.isNotBlank(dataSpace)
				.isTrue(REGEX_DATA_SPACE.matcher(dataSpace).matches(), "dataSpace {0} must match pattern {1}", dataSpace, REGEX_DATA_SPACE)
				.isNotNull(sortField)
				.isNotNull(displayField)
				.isNotNull(handleField)
				.isNotNull(keyField);
		//-----
		final var fieldsBuilder = new ListBuilder<DtField>();
		final var mappedFieldsBuilder = new MapBuilder<String, DtField>();
		//-----
		fragmentOpt = fragment;
		//
		this.stereotype = stereotype;
		this.packageName = packageName;
		DtField id = null;

		sortFieldOpt = sortField;
		displayFieldOpt = displayField;
		handleFieldOpt = handleField;
		keyFieldOpt = keyField;

		for (final DtField dtField : dtFields) {
			Assertion.check()
					.when(stereotype.isPersistent() && dtField.isPersistent(), () -> Assertion.check()
							.isTrue(!dtField.cardinality().hasMany(),
									"Only non multiple smarttype are allowed in entity '{0}'", name));
			if (dtField.getType().isId()) {
				Assertion.check().isNull(id, "Only one ID Field is allowed : {0}", name);
				id = dtField;
			}
			registerDtField(mappedFieldsBuilder, fieldsBuilder, dtField);
		}
		fields = fieldsBuilder.unmodifiable().build();
		mappedFields = mappedFieldsBuilder.unmodifiable().build();
		idFieldOpt = Optional.ofNullable(id);
		this.dataSpace = dataSpace;
		//-----
		Assertion.check()
				.when(fragment.isPresent(), () -> Assertion.check()
						.isTrue(DtStereotype.Fragment == stereotype, "Error on {0} with sterotype {1}, If an object is a fragment then it must have this stereotype", name, stereotype))
				//Persistent => ID
				.when(stereotype.isPersistent(), () -> Assertion.check()
						.isTrue(idFieldOpt.isPresent(), "Error on {0}, If an object is persistent then it must have an ID", name))
				.when(stereotype.isPersistent(), () -> Assertion.check()
						.isTrue(keyFieldOpt.isEmpty(), "Error on {0}, If an object is persistent then it must not have a keyField", name))
				.when(!stereotype.isPersistent(), () -> Assertion.check()
						.isTrue(idFieldOpt.isEmpty(), "Error on {0}, If an object is not persistent then it must have no ID", name));
	}

	/**
	 * Static method factory for DtDefinitionBuilder
	 * @param name the name of the dtDefinition
	 * @return DtDefinitionBuilder
	 */
	public static DtDefinitionBuilder builder(final String name) {
		return new DtDefinitionBuilder(name);
	}

	private static void registerDtField(final MapBuilder<String, DtField> mappedFieldsBuilder, final ListBuilder<DtField> fieldsBuilder, final DtField dtField) {
		Assertion.check()
				.isNotNull(dtField);
		//-----
		fieldsBuilder.add(dtField);
		mappedFieldsBuilder.putCheckKeyNotExists(dtField.name(), dtField);
	}

	public Optional<DtDefinition> getFragment() {
		return fragmentOpt.map(DefinitionId::get);
	}

	/**
	 * @return Stereotype du Dt
	 */
	public DtStereotype getStereotype() {
		return stereotype;
	}

	/**
	 * @return Nom canonique (i.e. avec le package) de la classe d'implémentation du DtObject
	 */
	public String getClassCanonicalName() {
		return getPackageName() + '.' + getClassSimpleName();
	}

	/**
	 * @return Simple Nom (i.e. sans le package) de la classe d'implémentation du DtObject
	 */
	public String getClassSimpleName() {
		return id().shortName();
	}

	/**
	 * @return the name of the package
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Retourne le champ correspondant SOUS CONDITION qu'il existe sinon assertion.
	 *
	 * @param fieldName Nom du champ
	 * @return Champ correspondant
	 */
	public DtField getField(final String fieldName) {
		Assertion.check().isNotBlank(fieldName);
		//-----
		final DtField dtField = mappedFields.get(fieldName);
		//-----
		Assertion.check().isNotNull(dtField, "field '{0}' not found on '{1}'. Available fields are :{2}", fieldName, getName(), mappedFields.keySet());
		return dtField;
	}

	/**
	 * Retourne le champ correspondant SOUS CONDITION qu'il existe sinon assertion.
	 *
	 * @param fieldName Nom du champ
	 * @return Champ correspondant
	 */
	public DtField getField(final DtFieldName fieldName) {
		return getField(fieldName.name());
	}

	/**
	 * @param fieldName FieldName
	 * @return if this field exists in this DtDefinition
	 */
	public boolean contains(final String fieldName) {
		Assertion.check().isNotNull(fieldName);
		//-----
		return mappedFields.containsKey(fieldName);
	}

	/**
	 * @return Collection des champs.
	 */
	public List<DtField> getFields() {
		return Collections.unmodifiableList(fields);
	}

	/**
	 * @return Champ identifiant l'identifiant
	 */
	public Optional<DtField> getIdField() {
		return idFieldOpt;
	}

	/**
	 * Gestion de la persistance.
	 * @return Si la définition est persistée.
	 */
	public boolean isPersistent() {
		return stereotype.isPersistent();
	}

	/**
	 * @return Champ représentant l'affichage
	 */
	public Optional<DtField> getDisplayField() {
		return displayFieldOpt;
	}

	/**
	 * @return Champ représentant le tri
	 */
	public Optional<DtField> getSortField() {
		return sortFieldOpt;
	}

	/**
	 * @return Champ représentant le handle
	 */
	public Optional<DtField> getHandleField() {
		return handleFieldOpt;
	}

	/**
	 * @return Champ représentant le champ servant de clé pour différencier localement les éléments d'une collection
	 */
	public Optional<DtField> getKeyField() {
		return keyFieldOpt;
	}

	/**
	 * @return the dataSpace
	 */
	public String getDataSpace() {
		return dataSpace;
	}
}
