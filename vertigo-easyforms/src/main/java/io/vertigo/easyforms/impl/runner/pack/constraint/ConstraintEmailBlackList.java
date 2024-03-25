package io.vertigo.easyforms.impl.runner.pack.constraint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import io.vertigo.basics.constraint.ConstraintUtil;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.Property;

/**
 * Check if the mail domain is not in a blacklist.
 * Add 'DISPOSABLE' to arg list to add a built-in list of disposable email.
 *
 * @author npiedeloup, skerdudou
 */
public final class ConstraintEmailBlackList implements Constraint<Integer, String> {
	public static final String DISPOSABLE = "DISPOSABLE";

	public static Set<String> DISPOSABLE_MAIL;
	static { // load list of disposable email from file
		try (var is = ConstraintEmailBlackList.class.getResourceAsStream("disposableMailList.txt");
				var reader = new BufferedReader(new InputStreamReader(is))) {
			DISPOSABLE_MAIL = new HashSet<>();
			String line;
			while ((line = reader.readLine()) != null) {
				DISPOSABLE_MAIL.add(line);
			}
		} catch (final IOException e) {
			throw WrappedException.wrap(e);
		}
	}

	private final Set<String> blacklist;
	private final LocaleMessageText errorMessage;
	private final boolean checkDisposable;

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

		checkDisposable = blacklist.contains(DISPOSABLE);
		blacklist.remove(DISPOSABLE);

		errorMessage = ConstraintUtil.resolveMessage(overrideMessageOpt, overrideResourceMessageOpt, EfConstraintResources.EfConEmail);
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkConstraint(final String email) {
		if (email == null) {
			return true;
		}
		final int arobaIndex = email.indexOf('@');
		final String emailDomain = email.substring(arobaIndex + 1).toLowerCase(Locale.ROOT);
		return !blacklist.contains(emailDomain) &&
				(!checkDisposable || !DISPOSABLE_MAIL.contains(emailDomain));
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
