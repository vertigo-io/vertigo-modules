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
package io.vertigo.easyforms.impl.runner.pack.constraint;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import io.vertigo.basics.constraint.ConstraintUtil;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.datamodel.smarttype.definitions.Property;

/**
 * Check if the mail domain is in whitelist. Domain check is of type "ends with".
 *
 * @author npiedeloup, skerdudou
 */
public final class ConstraintEmailWhiteList implements Constraint<Integer, String> {
	private final Set<String> whitelist;
	private final LocaleMessageText errorMessage;

	/**
	 * Constructor.
	 *
	 * @param args the minimum value
	 */
	public ConstraintEmailWhiteList(final String args, final Optional<String> overrideMessageOpt, final Optional<String> overrideResourceMessageOpt) {
		final var domains = args.split(",");
		whitelist = new HashSet<>(args.length());
		for (final var domain : domains) {
			whitelist.add(domain);
		}

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
		return whitelist.contains(emailDomain) ||
				whitelist.stream().anyMatch(d -> email.endsWith(d));
	}

	/** {@inheritDoc} */
	@Override
	public LocaleMessageText getErrorMessage() {
		return errorMessage;
	}

	/** {@inheritDoc} */
	@Override
	public Property<Integer> getProperty() {
		return new Property<>("whitelistCount", Integer.class);
	}

	/** {@inheritDoc} */
	@Override
	public Integer getPropertyValue() {
		return whitelist.size();
	}
}
