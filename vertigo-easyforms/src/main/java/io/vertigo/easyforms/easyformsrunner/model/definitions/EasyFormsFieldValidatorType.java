package io.vertigo.easyforms.easyformsrunner.model.definitions;

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
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplate;

@DefinitionPrefix(EasyFormsFieldValidatorType.PREFIX)
public final class EasyFormsFieldValidatorType extends AbstractDefinition<EasyFormsFieldValidatorType> {

	public static final String PREFIX = "EfFva";

	private final int priority;
	private final EasyFormsTemplate paramTemplate; // expose parameters to designer UI
	private final Set<EasyFormsFieldType> fieldTypes;
	private final Function<EasyFormsData, Serializable[]> descriptionParameterResolver;

	private EasyFormsFieldValidatorType(final String name, final int priority, final EasyFormsTemplate paramTemplate, final Set<EasyFormsFieldType> fieldTypes) {
		this(name, priority, paramTemplate, fieldTypes, p -> new Serializable[0]);
	}

	private EasyFormsFieldValidatorType(final String name, final int priority, final EasyFormsTemplate paramTemplate, final Set<EasyFormsFieldType> fieldTypes,
			final Function<EasyFormsData, Serializable[]> descriptionParameterResolver) {
		super(name);
		//---
		this.priority = priority;
		this.paramTemplate = paramTemplate;
		this.fieldTypes = fieldTypes;
		this.descriptionParameterResolver = descriptionParameterResolver;
	}

	public static EasyFormsFieldValidatorType of(final String name, final int priority, final EasyFormsFieldType... fieldTypes) {
		return new EasyFormsFieldValidatorType(name, priority, null, Set.of(fieldTypes));
	}

	public static EasyFormsFieldValidatorType of(final String name, final int priority, final EasyFormsTemplate paramTemplate, final EasyFormsFieldType... fieldTypes) {
		return new EasyFormsFieldValidatorType(name, priority, paramTemplate, Set.of(fieldTypes));
	}

	public static EasyFormsFieldValidatorType of(final String name, final int priority, final Collection<EasyFormsFieldType> fieldTypes) {
		Assertion.check().isNotNull(fieldTypes);
		return new EasyFormsFieldValidatorType(name, priority, null, Collections.unmodifiableSet(new HashSet<>(fieldTypes)));
	}

	public static EasyFormsFieldValidatorType of(final String name, final int priority, final EasyFormsTemplate paramTemplate, final Collection<EasyFormsFieldType> fieldTypes) {
		Assertion.check().isNotNull(fieldTypes);
		return new EasyFormsFieldValidatorType(name, priority, paramTemplate, Collections.unmodifiableSet(new HashSet<>(fieldTypes)));
	}

	public static EasyFormsFieldValidatorType resolve(final String name) {
		return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsFieldValidatorType.class);
	}

	public static List<EasyFormsFieldValidatorType> getConstraintForType(final EasyFormsFieldType type) {
		return Node.getNode().getDefinitionSpace()
				.getAll(EasyFormsFieldValidatorType.class)
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

	public Set<EasyFormsFieldType> getFieldTypes() {
		return fieldTypes;
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
