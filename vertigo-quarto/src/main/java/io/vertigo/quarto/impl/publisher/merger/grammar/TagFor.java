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

import java.util.regex.Pattern;

import io.vertigo.core.lang.VSystemException;
import io.vertigo.quarto.impl.publisher.merger.script.ScriptContext;
import io.vertigo.quarto.impl.publisher.merger.script.ScriptTag;
import io.vertigo.quarto.impl.publisher.merger.script.ScriptTagContent;

/**
 * @author pchretien, npiedeloup
 */
//public car instancié dynamiquement
public final class TagFor extends AbstractScriptTag implements ScriptTag {
	private static final String FOR_CALL = "for (java.util.Iterator {2}It = {0}.iterator(); {2}It.hasNext();) \\{ {1} {2} = ({1}) {2}It.next(); ";
	private static final Pattern FOR_ATTRIBUTE = Pattern.compile("^([0-9a-zA-Z_]+) *: *([0-9a-zA-Z_]+(\\.[0-9a-zA-Z_]+)*)");

	/** {@inheritDoc} */
	@Override
	public String renderOpen(final ScriptTagContent tag, final ScriptContext context) {
		// Renvoie un tableau de trois elements d'après l'expression reguliere
		final String[] parsing = parseAttribute(tag.getAttribute(), FOR_ATTRIBUTE);

		// le tag est dans le bon format

		/**
		 * on recupere le parsing de l'expression reguliere
		 * parsing[0] -> attribut entier
		 * parsing[1] -> le nom de la variable d'iteration
		 * parsing[2] -> le field path de la Liste
		 */
		context.push(parsing[1]);

		// rendu du tag
		final String[] rendering = new String[3];
		rendering[0] = getCallForCollectionFieldPath(parsing[2], tag.getCurrentVariable());
		rendering[1] = getDataAccessorClass().getCanonicalName();
		rendering[2] = parsing[1];

		return getTagRepresentation(FOR_CALL, rendering);
	}

	/** {@inheritDoc} */
	@Override
	public String renderClose(final ScriptTagContent content, final ScriptContext context) {
		if (context.empty()) {
			throw new VSystemException("document malforme : le tag loop est mal ferme");
		}
		context.pop();
		return START_BLOC_JSP + '}' + END_BLOC_JSP;
	}
}
