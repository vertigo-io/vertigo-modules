package io.vertigo.easyforms.runner.model.data;

import java.util.List;

import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.data.definitions.DataDescriptor;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;

public class EasyFormsDataDescriptor extends DataDescriptor {

	private final List<Constraint> businessConstraints;
	private final Integer minListSize; // When cardinality MANY, provide minimum number of elements
	private final Integer maxListSize; // When cardinality MANY, provide maximum number of elements

	public EasyFormsDataDescriptor(final String name, final SmartTypeDefinition smartTypeDefinition, final Cardinality cardinality, final List<Constraint> businessConstraints,
			final Integer minListSize, final Integer maxListSize) {
		super(StringUtil.constToLowerCamelCase(name), smartTypeDefinition, cardinality);
		this.businessConstraints = businessConstraints;
		this.minListSize = minListSize;
		this.maxListSize = maxListSize;
	}

	public List<Constraint> getBusinessConstraints() {
		return businessConstraints;
	}

	public Integer getMinListSize() {
		return minListSize;
	}

	public Integer getMaxListSize() {
		return maxListSize;
	}

}
