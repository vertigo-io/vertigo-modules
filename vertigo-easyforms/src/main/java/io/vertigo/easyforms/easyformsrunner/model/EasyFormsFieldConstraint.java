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

@DefinitionPrefix(EasyFormsFieldConstraint.PREFIX)
public final class EasyFormsFieldConstraint extends AbstractDefinition<EasyFormsFieldConstraint> {

	public static final String PREFIX = "EfFco";

	private final String label;
	private final int priority;
	private final Set<EasyFormsFieldType> fieldTypes;

	private EasyFormsFieldConstraint(final String code, final String label, final int priority, final Collection<EasyFormsFieldType> fieldTypes) {
		super(PREFIX + code);
		//---
		this.label = label;
		this.priority = priority;
		this.fieldTypes = Collections.unmodifiableSet(new HashSet<>(fieldTypes));
	}

	public static EasyFormsFieldConstraint of(final String code, final String label, final int priority, final EasyFormsFieldType... fieldTypes) {
		return new EasyFormsFieldConstraint(code, label, priority, Set.of(fieldTypes));
	}

	public static EasyFormsFieldConstraint of(final String code, final String label, final int priority, final Collection<EasyFormsFieldType> fieldTypes) {
		Assertion.check().isNotNull(fieldTypes);

		return new EasyFormsFieldConstraint(code, label, priority, fieldTypes);
	}

	public static EasyFormsFieldConstraint resolve(final String name) {
		if (name.startsWith(PREFIX)) {
			return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsFieldConstraint.class);
		}
		return Node.getNode().getDefinitionSpace().resolve(PREFIX + name, EasyFormsFieldConstraint.class);
	}

	public static List<EasyFormsFieldConstraint> getConstraintForType(final EasyFormsFieldType type) {
		return Node.getNode().getDefinitionSpace()
				.getAll(EasyFormsFieldConstraint.class)
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
