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
package io.vertigo.easyforms.runner.rule;

import io.vertigo.commons.peg.PegNoMatchFoundException;
import io.vertigo.commons.peg.PegResult;
import io.vertigo.commons.peg.PegSolver;
import io.vertigo.commons.peg.rule.PegRule;
import io.vertigo.commons.peg.rule.PegRules;
import io.vertigo.commons.peg.term.PegBoolOperatorTerm;

/**
 * Main rule for EasyForm expressions.
 * Supports expressions of the form: "value1 == value2 && value3 != value4 + 5". ie : operation(bool, comparison(operation(math)))
 *
 * @author skerdudou
 */
class EasyFormsRule implements PegRule<PegSolver<String, Object, Boolean>> {

	private static final PegRule<PegSolver<PegSolver<String, Object, Boolean>, Boolean, Boolean>> MAIN_RULE = PegRules.delayedOperation(
			PegRules.delayedOperationAndComparison(new ValueRule()),
			PegBoolOperatorTerm.class,
			true);

	@Override
	public String getExpression() {
		return MAIN_RULE.getExpression();
	}

	@Override
	public PegResult<PegSolver<String, Object, Boolean>> parse(final String text, final int start) throws PegNoMatchFoundException {
		final var result = MAIN_RULE.parse(text, start);
		return new PegResult<>(result.getIndex(), f -> result.getValue().apply(f2 -> f2.apply(f)));
	}

}
