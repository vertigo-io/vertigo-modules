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
package io.vertigo.easyforms.runner.model.data;

import java.util.List;

import io.vertigo.core.lang.Cardinality;
import io.vertigo.datamodel.data.definitions.DataDescriptor;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;

public class EasyFormsDataDescriptor extends DataDescriptor {

	private final List<Constraint> fieldConstraints;
	private final List<Constraint> businessConstraints;
	private final Integer minListSize; // When cardinality MANY, provide minimum number of elements
	private final Integer maxListSize; // When cardinality MANY, provide maximum number of elements

	public EasyFormsDataDescriptor(final String name, final SmartTypeDefinition smartTypeDefinition, final Cardinality cardinality, final List<Constraint> fieldConstraints,
			final List<Constraint> businessConstraints, final Integer minListSize, final Integer maxListSize) {
		super(name.replaceAll("[_-]", "").toLowerCase(), smartTypeDefinition, cardinality);
		this.fieldConstraints = fieldConstraints;
		this.businessConstraints = businessConstraints;
		this.minListSize = minListSize;
		this.maxListSize = maxListSize;
	}

	public List<Constraint> getFieldConstraints() {
		return fieldConstraints;
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
