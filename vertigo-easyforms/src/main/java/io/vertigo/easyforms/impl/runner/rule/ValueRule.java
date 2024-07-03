package io.vertigo.easyforms.impl.runner.rule;

import java.util.List;
import java.util.Optional;

import io.vertigo.commons.peg.AbstractRule;
import io.vertigo.commons.peg.PegChoice;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;
import io.vertigo.commons.peg.PegWordRule;

class ValueRule extends AbstractRule<String, PegChoice> {

	private static final PegRule<String> NULL_RULE = PegRules.term("null");

	private static final PegRule<List<Object>> STRING_RULE = PegRules.sequence(
			PegRules.term("\""),
			PegRules.named(PegRules.word(true, "\"", PegWordRule.Mode.REJECT, "^\"*"), "string"),
			PegRules.term("\""));

	private static final PegRule<List<Object>> NUMBER_RULE = PegRules.named(
			PegRules.sequence(
					PegRules.optional(PegRules.term("-")), // Optional negative sign
					PegRules.word(false, "0123456789", PegWordRule.Mode.ACCEPT, "0-9"),
					PegRules.optional(PegRules.sequence( // Optional decimal value
							PegRules.term("."),
							PegRules.word(false, "0123456789", PegWordRule.Mode.ACCEPT, "0-9")))),
			"number");

	private static final PegRule<PegChoice> BOOLEAN_RULE = PegRules.choice(PegRules.term("true"), PegRules.term("false"));

	private static final PegRule<List<Object>> VARIABLE_RULE = PegRules.named(
			PegRules.sequence(
					PegRules.term("#"),
					PegRules.word(false, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ._-", PegWordRule.Mode.ACCEPT, "a-zA-Z._-"),
					PegRules.term("#")),
			"variable");

	private static final PegRule<PegChoice> VALUE_RULE = PegRules.named(
			PegRules.choice(
					NULL_RULE, // 0
					STRING_RULE, // 1
					NUMBER_RULE, // 2
					VARIABLE_RULE, // 3
					BOOLEAN_RULE), // 4
			"value or variable", "Expected {0}");

	public ValueRule() {
		super(VALUE_RULE);
	}

	@Override
	protected String handle(final PegChoice choice) {
		switch (choice.choiceIndex()) {
			case 0: // NULL_RULE
				return (String) choice.value();
			case 1: // STRING_RULE
			case 3: // VARIABLE_RULE
				final StringBuilder sb = new StringBuilder();
				for (final var object : (List) choice.value()) {
					sb.append(object);
				}
				return sb.toString();
			case 2: // NUMBER_RULE
				final StringBuilder sb2 = new StringBuilder();
				final var list = (List) choice.value();

				((Optional) list.get(0)).ifPresent(sb2::append); // Optional negative sign

				sb2.append(list.get(1)); // number

				((Optional) list.get(2)).ifPresent(v -> {
					final var list2 = (List) v;
					sb2.append(list2.get(0)); // .
					sb2.append(list2.get(1)); // decimal value
				});

				return sb2.toString();
			case 4: // BOOLEAN_RULE
				return (String) ((PegChoice) choice.value()).value();

			default:
				throw new IllegalArgumentException("Invalid value type");
		}

	}

}
