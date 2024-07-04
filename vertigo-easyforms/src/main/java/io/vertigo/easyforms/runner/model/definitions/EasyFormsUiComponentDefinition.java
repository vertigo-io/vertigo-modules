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

import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.AbstractDefinition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;

@DefinitionPrefix(EasyFormsUiComponentDefinition.PREFIX)
public final class EasyFormsUiComponentDefinition extends AbstractDefinition<EasyFormsUiComponentDefinition> {

	public static final String PREFIX = "EfUic";

	/**
	 * Theses parameters are not exposed by default in the UI.
	 * FieldType MUST copy from this template what he wants to expose.
	 */
	private final EasyFormsTemplate parameters;

	private EasyFormsUiComponentDefinition(final String name, final EasyFormsTemplate parameters) {
		super(name);
		//---
		this.parameters = parameters;
	}

	public static EasyFormsUiComponentDefinition of(final String name, final EasyFormsTemplate parameters) {
		return new EasyFormsUiComponentDefinition(name, parameters);
	}

	public static EasyFormsUiComponentDefinition resolve(final String name) {
		return Node.getNode().getDefinitionSpace().resolve(name, EasyFormsUiComponentDefinition.class);
	}

	public EasyFormsTemplate getParameters() {
		return parameters;
	}

}
