package io.vertigo.easyforms.impl.easyformsrunner.library.constraint;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import io.vertigo.basics.constraint.ConstraintUtil;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.Property;

/**
 * Check if the mail domain is not in a blacklist.
 *
 * @author npiedeloup, skerdudou
 */
public final class ConstraintEmailBlackList implements Constraint<Integer, String> {
	private final Set<String> blacklist;
	private final LocaleMessageText errorMessage;

	/**
	 * Constructor.
	 *
	 * @param args the minimum value
	 */
	public ConstraintEmailBlackList(final String args, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		final var domains = args.split(",");
		blacklist = new HashSet<>(args.length());
		for (final var domain : domains) {
			blacklist.add(domain);
		}

		errorMessage = ConstraintUtil.resolveMessage(overrideMessageOpt, overrideResourceMessageOpt,
				() -> LocaleMessageText.of("Le mail n'est pas autorisé"));
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkConstraint(final String email) {
		final int arobaIndex = email.indexOf('@');
		final String emailDomain = email.substring(arobaIndex + 1).toLowerCase(Locale.ROOT);
		return !blacklist.contains(emailDomain);
	}

	/** {@inheritDoc} */
	@Override
	public LocaleMessageText getErrorMessage() {
		return errorMessage;
	}

	/** {@inheritDoc} */
	@Override
	public Property<Integer> getProperty() {
		return new Property<>("blacklistCount", Integer.class);
	}

	/** {@inheritDoc} */
	@Override
	public Integer getPropertyValue() {
		return blacklist.size();
	}
}
