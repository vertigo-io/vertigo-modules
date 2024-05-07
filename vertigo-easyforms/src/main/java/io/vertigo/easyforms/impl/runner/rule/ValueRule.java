package io.vertigo.easyforms.impl.runner.rule;

import java.util.List;
import java.util.Optional;

import io.vertigo.commons.peg.AbstractRule;
import io.vertigo.commons.peg.PegChoice;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;
import io.vertigo.commons.peg.PegWordRule;

class ValueRule extends AbstractRule<String, PegChoice> {

	private static final PegRule<List<Object>> STRING_RULE = PegRules.sequence(
			PegRules.term("\""),
			PegRules.named(PegRules.word(true, "\"", PegWordRule.Mode.REJECT, "^\"*"), "string"),
			PegRules.term("\""));

	private static final PegRule<List<Object>> NUMBER_RULE = PegRules.named(
			PegRules.sequence(
					PegRules.word(false, "0123456789", PegWordRule.Mode.ACCEPT, "0-9"),
					PegRules.optional(PegRules.sequence(
							PegRules.term("."),
							PegRules.word(false, "0123456789", PegWordRule.Mode.ACCEPT, "0-9")))),
			"number");

	private static final PegRule<PegChoice> BOOLEAN_RULE = PegRules.choice(PegRules.term("true"), PegRules.term("false"));

	private static final PegRule<List<Object>> VARIABLE_RULE = PegRules.named(
			PegRules.sequence(
					PegRules.term("#"),
					PegRules.word(false, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.", PegWordRule.Mode.ACCEPT, "a-zA-Z."),
					PegRules.term("#")),
			"variable");

	private static final PegRule<PegChoice> VALUE_RULE = PegRules.named(
			PegRules.choice(
					STRING_RULE,
					NUMBER_RULE,
					VARIABLE_RULE,
					BOOLEAN_RULE),
			"value or variable");

	public ValueRule() {
		super(VALUE_RULE);
	}

	@Override
	protected String handle(final PegChoice choice) {
		if (choice.value() instanceof final PegChoice choice2) {
			return (String) choice2.value();
		} else if (choice.value() instanceof final List list) {
			final StringBuilder sb = new StringBuilder();
			for (final var object : list) {
				if (object instanceof final Optional o) {
					if (o.isPresent()) {
						sb.append(String.join("", (List) o.get()));
					}
				} else {
					sb.append(object);
				}
			}

			return sb.toString();
		} else {
			throw new IllegalArgumentException("Invalid value type");
		}

	}

}
