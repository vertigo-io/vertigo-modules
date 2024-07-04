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

import io.vertigo.quarto.impl.publisher.merger.script.ScriptContext;
import io.vertigo.quarto.impl.publisher.merger.script.ScriptTag;
import io.vertigo.quarto.impl.publisher.merger.script.ScriptTagContent;

/**
 * @author pchretien, npiedeloup
 */
abstract class AbstractTagIf extends AbstractScriptTag implements ScriptTag {
	private static final String CALL_IF = "if({0}) \\{ ";
	private static final String CALL_IF_NOT = "if (!({0})) \\{ ";
	private final String call;
	private final boolean equalsCondition;

	AbstractTagIf(final boolean caseIf, final boolean equalsCondition) {
		call = caseIf ? CALL_IF : CALL_IF_NOT;
		this.equalsCondition = equalsCondition;
	}

	/** {@inheritDoc} */
	@Override
	public final String renderOpen(final ScriptTagContent tag, final ScriptContext context) {
		final String[] parsing = parseAttribute(tag.getAttribute(), equalsCondition ? FIELD_PATH_CALL_EQUALS_CONDITION : FIELD_PATH_CALL);
		// le tag est dans le bon format
		if (equalsCondition) {
			parsing[0] = getCallForEqualsBooleanFieldPath(parsing[1], parsing[3], tag.getCurrentVariable());
		} else {
			parsing[0] = getCallForBooleanFieldPath(parsing[0], tag.getCurrentVariable());
		}
		return getTagRepresentation(call, parsing);
	}

	/** {@inheritDoc} */
	@Override
	public final String renderClose(final ScriptTagContent content, final ScriptContext context) {
		return START_BLOC_JSP + '}' + END_BLOC_JSP;
	}
}
