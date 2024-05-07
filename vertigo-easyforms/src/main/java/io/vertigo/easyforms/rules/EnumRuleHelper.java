package io.vertigo.easyforms.rules;

import java.util.Arrays;
import java.util.List;

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

	/**
	 * Get a matching rule for an enum.
	 *
	 * @param enumClass the enum class
	 * @return the rule
	 * @param <B> the enum type
	 */
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

	/**
	 * Get a matching rule for an enum with spaces around.
	 *
	 * @param enumClass the enum class
	 * @return the rule
	 * @param <B> the enum type
	 */
	public static <B extends Enum<B> & ITermRule> PegRule<B> getSpacedGlobalRule(final Class<B> enumClass) {
		final var rule = PegRules.sequence(
				PegRules.blanks(),
				getGlobalRule(enumClass),
				PegRules.blanks());

		return new AbstractRule<>(rule) {
			@Override
			protected B handle(final List<Object> parsing) {
				return (B) parsing.get(1);
			}
		};
	}

	/**
	 * Get a rule for an individual enum value.
	 *
	 * @param element the enum value
	 * @return the rule
	 */
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

	/**
	 * Get a rule for an individual enum value, skipping spaces before.
	 *
	 * @param element the enum value
	 * @return the rule
	 */
	public static <B extends Enum<B> & ITermRule> PegRule<B> getIndividualRuleSkipSpaces(final B element) {
		final PegRule<B> mainRule = getIndividualRule(element);
		final var rule = PegRules.named(
				PegRules.sequence(
						PegRules.skipBlanks(),
						mainRule),
				mainRule.getExpression());

		return new AbstractRule<>(rule) {
			@Override
			protected B handle(final List<Object> parsing) {
				return (B) parsing.get(1);
			}
		};
	}

}
