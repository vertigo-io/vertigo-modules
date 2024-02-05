package io.vertigo.easyforms.easyformsrunner.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;

@DefinitionPrefix(EasyFormsFieldValidator.PREFIX)
public final class EasyFormsFieldValidator extends AbstractDefinition<EasyFormsFieldValidator> {

	public static final String PREFIX = "EfFva";

	private final int priority;
	private final EasyFormsTemplate paramTemplate;
	private final Set<EasyFormsFieldType> fieldTypes;

	private EasyFormsFieldValidator(final String name, final int priority, final EasyFormsTemplate paramTemplate, final Set<EasyFormsFieldType> fieldTypes) {
		super(name);
		//---
		this.priority = priority;
		this.paramTemplate = paramTemplate;
		this.fieldTypes = fieldTypes;
	}

	public static EasyFormsFieldValidator of(final String name, final int priority, final EasyFormsFieldType... fieldTypes) {
		return new EasyFormsFieldValidator(name, priority, null, Set.of(fieldTypes));
	}

	public static EasyFormsFieldValidator of(final String name, final int priority, final EasyFormsTemplate paramTemplate, final EasyFormsFieldType... fieldTypes) {
		return new EasyFormsFieldValidator(name, priority, paramTemplate, Set.of(fieldTypes));
	}

	public static EasyFormsFieldValidator of(final String name, final int priority, final Collection<EasyFormsFieldType> fieldTypes) {
		Assertion.check().isNotNull(fieldTypes);
		return new EasyFormsFieldValidator(name, priority, null, Collections.unmodifiableSet(new HashSet<>(fieldTypes)));
	}

	public static EasyFormsFieldValidator of(final String name, final int priority, final EasyFormsTemplate paramTemplate, final Collection<EasyFormsFieldType> fieldTypes) {
		Assertion.check().isNotNull(fieldTypes);
		return new EasyFormsFieldValidator(name, priority, paramTemplate, Collections.unmodifiableSet(new HashSet<>(fieldTypes)));
	}

	public static EasyFormsFieldValidator resolve(final String name) {
		return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsFieldValidator.class);
	}

	public static List<EasyFormsFieldValidator> getConstraintForType(final EasyFormsFieldType type) {
		return Node.getNode().getDefinitionSpace()
				.getAll(EasyFormsFieldValidator.class)
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
		return LocaleMessageText.of(() -> getName() + "Description").getDisplay();
	}
}
