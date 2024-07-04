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

/**
 * Definition d'un Tag KScript.
 * @author pchretien, npiedeloup
 */
final class ScriptTagDefinition {
	private final String name;
	private final Class<? extends ScriptTag> classTag;
	private final Boolean openTag;

	ScriptTagDefinition(final String name, final Class<? extends ScriptTag> classTag, final Boolean openTag) {
		this.name = name;
		this.classTag = classTag;
		this.openTag = openTag;
	}

	/**
	 * @return Classe du tag.
	 */
	Class<? extends ScriptTag> getClassTag() {
		return this.classTag;
	}

	/**
	 * @return Si balise ouvrante, false si balise fermante, null si pas de body
	 */
	Boolean isOpenTag() {
		return this.openTag;
	}

	/**
	 * @return Nom du tag
	 */
	String getName() {
		return this.name;
	}
}
