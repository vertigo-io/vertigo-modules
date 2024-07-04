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
package io.vertigo.quarto.impl.publisher.merger.grammar;

import io.vertigo.quarto.impl.publisher.merger.script.ScriptGrammar;
import io.vertigo.quarto.impl.publisher.merger.script.ScriptTag;

/**
 * Grammaire des éditions.
 * Offre un langage simple et de haut niveau permettant d'utiliser une syntaxe non java
 * afin par exemple de constituer des éditions.
 *
 * Une grammaire est constituée de mots clés (Keyword) en nombre fini.
 *
 * @author oboitel, pchretien
 */
public final class ScriptGrammarUtil {

	/*
	 * Mots clés de la grammaire de base.
	 */
	private enum Keyword {
		/**
		 * FIELD.
		 */
		FIELD("=", false, TagEncodedField.class),
		//FIELD("=", false, TagField.class),
		/**
		 * IF.
		 */
		IF("if ", true, TagIf.class),
		/**
		 * IFNOT.
		 */
		IFNOT("ifnot ", true, TagIfNot.class),
		/**
		 * IFEQUALS.
		 */
		IFEQUALS("ifequals ", true, TagIfEquals.class),
		/**
		 * IFNOT.
		 */
		IFNOTEQUALS("ifnotequals ", true, TagIfNotEquals.class),
		/**
		 * FOR.
		 */
		FOR("loop ", true, TagFor.class),
		/**
		 * BLOCK.
		 */
		BLOCK("block ", true, TagBlock.class),
		/**
		 * OBJECT.
		 */
		OBJECT("var ", true, TagObject.class);

		private final String syntax;
		private final boolean hasBody;
		private final Class<?> tagClass;

		private <S extends ScriptTag> Keyword(final String syntax, final boolean hasBody, final Class<S> tagClass) {
			this.syntax = syntax;
			this.hasBody = hasBody;
			this.tagClass = tagClass;
		}

		String getSyntax() {
			return syntax;
		}

		boolean hasBody() {
			return hasBody;
		}

		<S extends ScriptTag> Class<S> getTagClass() {
			return (Class<S>) tagClass;
		}
	}

	private ScriptGrammarUtil() {
		// Class utilitaire sans état
	}

	/**
	 * @return ScriptGrammar initialisé.
	 */
	public static ScriptGrammar createScriptGrammar() {
		final ScriptGrammar scriptGrammar = new ScriptGrammar();
		//On enregistre tous les mots clés.
		for (final Keyword keyword : Keyword.values()) {
			scriptGrammar.registerScriptTag(keyword.getSyntax(), keyword.getTagClass(), keyword.hasBody());
		}
		return scriptGrammar;
	}
}
