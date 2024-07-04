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
package io.vertigo.quarto.impl.publisher.merger.processor;

import io.vertigo.commons.script.ScriptManager;
import io.vertigo.commons.script.SeparatorType;
import io.vertigo.core.lang.Assertion;
import io.vertigo.quarto.impl.publisher.merger.script.ScriptGrammar;
import io.vertigo.quarto.impl.publisher.merger.script.ScriptHandlerImpl;
import io.vertigo.quarto.publisher.model.PublisherData;

/**
 * Processor String2String qui evalue la chaine d'entree en transformant les balises
 * de la grammaire ODT en balise jsp <% %>.
 * Les balises reconnues sont <# #>
 * @author oboitel
 */
public final class GrammarEvaluatorProcessor implements MergerProcessor {
	private final ScriptManager scriptManager;
	private final ScriptGrammar scriptGrammar;

	/**
	 * Constructeur.
	 * @param scriptManager Manager des scripts
	 * @param scriptGrammar ScriptGrammar
	 */
	public GrammarEvaluatorProcessor(final ScriptManager scriptManager, final ScriptGrammar scriptGrammar) {
		Assertion.check()
				.isNotNull(scriptManager)
				.isNotNull(scriptGrammar);
		//-----
		this.scriptManager = scriptManager;
		this.scriptGrammar = scriptGrammar;
	}

	/** {@inheritDoc} */
	@Override
	public String execute(final String input, final PublisherData publisherData) {
		final ScriptHandlerImpl scriptHandler = new ScriptHandlerImpl(MergerScriptEvaluatorProcessor.DATA, scriptGrammar);
		scriptManager.parse(input, scriptHandler, SeparatorType.XML_CODE.getSeparator());
		return scriptHandler.result();
	}
}
