package io.vertigo.easyforms.impl.runner.pack.constraint;

import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.Property;

public interface EasyFormsConstraint<J, D> extends Constraint<J, D> {

	@Override
	default Property getProperty() {
		return null;
	}

	@Override
	default J getPropertyValue() {
		return null;
	}

}