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
package io.vertigo.easyforms.runner.model.definitions;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;

@DefinitionPrefix(EasyFormsFieldValidatorTypeDefinition.PREFIX)
public final class EasyFormsFieldValidatorTypeDefinition extends AbstractDefinition<EasyFormsFieldValidatorTypeDefinition> {

	public static final String PREFIX = "EfFva";

	private final int priority;
	private final EasyFormsTemplate paramTemplate; // expose parameters to designer UI
	private final Set<EasyFormsFieldTypeDefinition> fieldTypes;
	private final Function<EasyFormsData, Serializable[]> descriptionParameterResolver;
	private final Constraint constraint;

	private EasyFormsFieldValidatorTypeDefinition(final String name, final int priority, final EasyFormsTemplate paramTemplate, final Set<EasyFormsFieldTypeDefinition> fieldTypes,
			final Constraint constraint) {
		this(name, priority, paramTemplate, fieldTypes, p -> new Serializable[0], constraint);
	}

	private EasyFormsFieldValidatorTypeDefinition(final String name, final int priority, final EasyFormsTemplate paramTemplate, final Set<EasyFormsFieldTypeDefinition> fieldTypes,
			final Function<EasyFormsData, Serializable[]> descriptionParameterResolver, final Constraint constraint) {
		super(name);
		//---
		this.priority = priority;
		this.paramTemplate = paramTemplate;
		this.fieldTypes = fieldTypes;
		this.descriptionParameterResolver = descriptionParameterResolver;
		this.constraint = constraint;
	}

	public static EasyFormsFieldValidatorTypeDefinition of(final String name, final int priority, final Collection<EasyFormsFieldTypeDefinition> fieldTypes,
			final Constraint constraint) {
		Assertion.check().isNotNull(fieldTypes);
		return new EasyFormsFieldValidatorTypeDefinition(name, priority, null, Collections.unmodifiableSet(new HashSet<>(fieldTypes)), constraint);
	}

	public static EasyFormsFieldValidatorTypeDefinition of(final String name, final int priority, final Collection<EasyFormsFieldTypeDefinition> fieldTypes, final Constraint constraint,
			final EasyFormsTemplate paramTemplate) {
		Assertion.check().isNotNull(fieldTypes);
		return new EasyFormsFieldValidatorTypeDefinition(name, priority, paramTemplate, Collections.unmodifiableSet(new HashSet<>(fieldTypes)), constraint);
	}

	public static EasyFormsFieldValidatorTypeDefinition resolve(final String name) {
		return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsFieldValidatorTypeDefinition.class);
	}

	public static List<EasyFormsFieldValidatorTypeDefinition> getConstraintForType(final EasyFormsFieldTypeDefinition type) {
		return Node.getNode().getDefinitionSpace()
				.getAll(EasyFormsFieldValidatorTypeDefinition.class)
				.stream()
				.filter(c -> c.getFieldTypes().contains(type))
				.toList();
	}

	public int getPriority() {
		return priority;
	}

	public EasyFormsTemplate getParamTemplate() {
		return paramTemplate;
	}

	public Set<EasyFormsFieldTypeDefinition> getFieldTypes() {
		return fieldTypes;
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public String getLabel() {
		return LocaleMessageText.of(() -> getName() + "Label").getDisplay();
	}

	public String getDescription() {
		return LocaleMessageText.of(() -> getName() + "Description").getDisplayOpt().orElse(null);
	}

	public String getParameterizedLabel(final EasyFormsData paremeters) {
		return LocaleMessageText.ofDefaultMsg(getLabel(), () -> getName() + "ParameterizedLabel", descriptionParameterResolver.apply(paremeters)).getDisplay();
	}
}
