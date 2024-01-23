package io.vertigo.easyforms.easyformsrunner.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;

@DefinitionPrefix(EasyFormsFieldValidator.PREFIX)
public final class EasyFormsFieldValidator extends AbstractDefinition<EasyFormsFieldValidator> {

	public static final String PREFIX = "EfFco";

	private final String label;
	private final int priority;
	private final Set<EasyFormsFieldType> fieldTypes;

	private EasyFormsFieldValidator(final String code, final String label, final int priority, final Collection<EasyFormsFieldType> fieldTypes) {
		super(PREFIX + code);
		//---
		this.label = label;
		this.priority = priority;
		this.fieldTypes = Collections.unmodifiableSet(new HashSet<>(fieldTypes));
	}

	public static EasyFormsFieldValidator of(final String code, final String label, final int priority, final EasyFormsFieldType... fieldTypes) {
		return new EasyFormsFieldValidator(code, label, priority, Set.of(fieldTypes));
	}

	public static EasyFormsFieldValidator of(final String code, final String label, final int priority, final Collection<EasyFormsFieldType> fieldTypes) {
		Assertion.check().isNotNull(fieldTypes);

		return new EasyFormsFieldValidator(code, label, priority, fieldTypes);
	}

	public static EasyFormsFieldValidator resolve(final String name) {
		if (name.startsWith(PREFIX)) {
			return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsFieldValidator.class);
		}
		return Node.getNode().getDefinitionSpace().resolve(PREFIX + name, EasyFormsFieldValidator.class);
	}

	public static List<EasyFormsFieldValidator> getConstraintForType(final EasyFormsFieldType type) {
		return Node.getNode().getDefinitionSpace()
				.getAll(EasyFormsFieldValidator.class)
				.stream()
				.filter(c -> c.getFieldTypes().contains(type))
				.toList();
	}

	public String getLabel() {
		return label;
	}

	public int getPriority() {
		return priority;
	}

	public Set<EasyFormsFieldType> getFieldTypes() {
		return fieldTypes;
	}

}
