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
package io.vertigo.quarto.impl.publisher.merger.script;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.VSystemException;

/**
 * Stocke le contenu d'un tag de la grammaire ODT
 * en distiguant son type et un attribut.
 * @author oboitel
 */
public final class ScriptTagContent {
	private final ScriptTagDefinition definition;
	private final String attribute;
	private String variableName;

	ScriptTagContent(final ScriptTagDefinition definition, final String attribute) {
		Assertion.check()
				.isNotNull(definition)
				.isTrue(attribute == null || attribute.length() > 0, "Les attributs doivent faire plus de 1 caractère");
		//-----
		this.definition = definition;
		this.attribute = attribute;
	}

	/**
	 * Si aucun attribut une exception est retournée.
	 * @return Atribut du tag
	 */
	public String getAttribute() {
		checkAttribute();
		return attribute;
	}

	/**
	 * @return Variable java courante
	 */
	public String getCurrentVariable() {
		return variableName;
	}

	/**
	 * @param variable Variable java courante
	 */
	void setCurrentVariable(final String variable) {
		variableName = variable;
	}

	/**
	 * @return Definition du tag
	 */
	ScriptTagDefinition getScriptTagDefinition() {
		return definition;
	}

	/**
	 * @return Si le tag a un attribut
	 */
	private boolean hasAttribute() {
		return attribute != null;
	}

	/**
	 * Vérifie que le Tag possède un attribut sinon lance une exception.
	 */
	private void checkAttribute() {
		if (!hasAttribute()) {
			throw new VSystemException("tag malforme : le tag {0} doit avoir un attribut", getScriptTagDefinition().getName());
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "tag::" + definition.getName() + '[' + attribute + ']';
	}
}
