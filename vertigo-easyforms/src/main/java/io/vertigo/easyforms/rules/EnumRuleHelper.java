package io.vertigo.easyforms.rules;

import java.util.Arrays;

import io.vertigo.commons.peg.AbstractRule;
import io.vertigo.commons.peg.PegChoice;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;

/**
 * Helper for enum rules.
 *
 * @author skerdudou
 */
public class EnumRuleHelper {

	private EnumRuleHelper() {
		//private constructor
	}

	public static <B extends Enum<B> & ITermRule> PegRule<B> getGlobalRule(final Class<B> enumClass) {
		final var enumValues = enumClass.getEnumConstants();
		final var operatorRules = Arrays.stream(enumValues)
				.map(EnumRuleHelper::getIndividualRule)
				.toArray(PegRule[]::new);

		final var pegChoice = PegRules.choice(operatorRules);

		return new AbstractRule<>(pegChoice) {
			@Override
			protected B handle(final PegChoice parsing) {
				return (B) parsing.value();
			}
		};
	}

	public static <B extends Enum<B> & ITermRule> PegRule<B> getIndividualRule(final B element) {
		final PegRule<?>[] list = element.getStrValues().stream()
				.map(PegRules::term)
				.toArray(PegRule[]::new);

		final var pegChoice = PegRules.choice(list);

		return new AbstractRule<>(pegChoice) {
			@Override
			protected B handle(final PegChoice parsing) {
				return element;
			}
		};
	}

}
