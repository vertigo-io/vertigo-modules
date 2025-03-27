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
package io.vertigo.planning.agenda.services;

import java.util.Optional;

import javax.inject.Inject;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.node.config.discovery.NotDiscoverable;
import io.vertigo.core.param.ParamValue;

@NotDiscoverable
public class PlanningServicesConfig implements Component {

	static final int MAX_AGENDA_TO_LOAD = 20;
	static final int ORPHAN_RESERVATION_AGG_MINUTES = 30;

	private final int createMinDureePlageMinute;
	private final int createMaxDureePlageHeure;
	private final int createPlageHeureMin; //07:30 min by default
	private final int createPlageHeureMax; //21:00 max by default

	private final int createMaxNbGuichet;
	private final int createMaxDaysFromNow; //maximum 1 an par rapport à aujourd'hui

	private final int publishMaxDaysFromNow; //maximum 1 an par rapport à aujourd'hui
	private final int publishMaxDaysPeriode; //maximum 2 mois publié à la fois
	private final int publishNowDelaySecond; //1 minute de sécurité avant publication effective

	private final int duplicateMaxDaysPeriode; //3 mois
	private final int duplicateMaxDaysFromNow; //maximum 1 an par rapport à aujourd'hui

	/**
	 * Constructor.
	 * @param createMinDureePlageMinute the minimum duration of a time slot in minutes
	 * @param createMaxDureePlageHeure the maximum duration of a time slot in hours
	 * @param createPlageHeureMin the minimum time slot start time in minutes from start of day
	 * @param createPlageHeureMax the maximum time slot end time in minutes from start of day
	 * @param createMaxNbGuichet the maximum number of counters (guichets) for a time slot
	 * @param createMaxDaysFromNow the maximum number of days in the future to create a time slot
	 * @param publishMaxDaysFromNow the maximum number of days in the future to publish a time slot
	 * @param publishMaxDaysPeriode the maximum number of days published at once
	 * @param publishNowDelaySecond the delay before effective publication (security to revert)
	 * @param duplicateMaxDaysPeriode the maximum number of days to duplicate
	 * @param duplicateMaxDaysFromNow the maximum number of days from now to duplicate
	 */
	@Inject
	public PlanningServicesConfig(
			@ParamValue("createMinDureePlageMinute") final Optional<Integer> createMinDureePlageMinuteOpt,
			@ParamValue("createMaxDureePlageHeure") final Optional<Integer> createMaxDureePlageHeureOpt,
			@ParamValue("createPlageHeureMin") final Optional<Integer> createPlageHeureMinOpt,
			@ParamValue("createPlageHeureMax") final Optional<Integer> createPlageHeureMaxOpt,
			@ParamValue("createMaxNbGuichet") final Optional<Integer> createMaxNbGuichetOpt,
			@ParamValue("createMaxDaysFromNow") final Optional<Integer> createMaxDaysFromNowOpt,
			@ParamValue("publishMaxDaysFromNow") final Optional<Integer> publishMaxDaysFromNowOpt,
			@ParamValue("publishMaxDaysPeriode") final Optional<Integer> publishMaxDaysPeriodeOpt,
			@ParamValue("publishNowDelaySecond") final Optional<Integer> publishNowDelaySecondOpt,
			@ParamValue("duplicateMaxDaysPeriode") final Optional<Integer> duplicateMaxDaysPeriodeOpt,
			@ParamValue("duplicateMaxDaysFromNow") final Optional<Integer> duplicateMaxDaysFromNowOpt) {
		Assertion.check()
				.isNotNull(createMinDureePlageMinuteOpt)
				.isNotNull(createMaxDureePlageHeureOpt)
				.isNotNull(createPlageHeureMinOpt)
				.isNotNull(createPlageHeureMaxOpt)
				.isNotNull(createMaxNbGuichetOpt)
				.isNotNull(createMaxDaysFromNowOpt)
				.isNotNull(publishMaxDaysFromNowOpt)
				.isNotNull(publishMaxDaysPeriodeOpt)
				.isNotNull(publishNowDelaySecondOpt)
				.isNotNull(duplicateMaxDaysPeriodeOpt)
				.isNotNull(duplicateMaxDaysFromNowOpt);
		//-----
		createMinDureePlageMinute = createMinDureePlageMinuteOpt.orElse(60); //60 minutes min by default
		createMaxDureePlageHeure = createMaxDureePlageHeureOpt.orElse(10); //10 hours max by default
		createPlageHeureMin = createPlageHeureMinOpt.orElse(7 * 60 + 30); //7:30 min by default
		createPlageHeureMax = createPlageHeureMaxOpt.orElse(21 * 60); //21:00 max by default
		createMaxNbGuichet = createMaxNbGuichetOpt.orElse(9); //9 max by default
		createMaxDaysFromNow = createMaxDaysFromNowOpt.orElse(365); //max 1 year in future
		publishMaxDaysFromNow = publishMaxDaysFromNowOpt.orElse(365); //max 1 year in future
		publishMaxDaysPeriode = publishMaxDaysPeriodeOpt.orElse(31 * 2); //max 2 months published at once
		publishNowDelaySecond = publishNowDelaySecondOpt.orElse(60); //1 minute before publishing by security
		duplicateMaxDaysPeriode = duplicateMaxDaysPeriodeOpt.orElse(31 * 3); //3 months max by default
		duplicateMaxDaysFromNow = duplicateMaxDaysFromNowOpt.orElse(365); //max 1 year in future
	}

	/**
	 * @return the createMinDureePlageMinute
	 */
	final int getCreateMinDureePlageMinute() {
		return createMinDureePlageMinute;
	}

	/**
	 * @return the createMaxDureePlageHeure
	 */
	final int getCreateMaxDureePlageHeure() {
		return createMaxDureePlageHeure;
	}

	/**
	 * @return the createPlageHeureMin
	 */
	final int getCreatePlageHeureMin() {
		return createPlageHeureMin;
	}

	/**
	 * @return the createPlageHeureMax
	 */
	final int getCreatePlageHeureMax() {
		return createPlageHeureMax;
	}

	/**
	 * @return the createMaxNbGuichet
	 */
	final int getCreateMaxNbGuichet() {
		return createMaxNbGuichet;
	}

	/**
	 * @return the createMaxDaysFromNow
	 */
	final int getCreateMaxDaysFromNow() {
		return createMaxDaysFromNow;
	}

	/**
	 * @return the publishMaxDaysFromNow
	 */
	final int getPublishMaxDaysFromNow() {
		return publishMaxDaysFromNow;
	}

	/**
	 * @return the publishMaxDaysPeriode
	 */
	final int getPublishMaxDaysPeriode() {
		return publishMaxDaysPeriode;
	}

	/**
	 * @return the publishNowDelaySecond
	 */
	final int getPublishNowDelaySecond() {
		return publishNowDelaySecond;
	}

	/**
	 * @return the duplicateMaxDaysPeriode
	 */
	final int getDuplicateMaxDaysPeriode() {
		return duplicateMaxDaysPeriode;
	}

	/**
	 * @return the duplicateMaxDaysFromNow
	 */
	final int getDuplicateMaxDaysFromNow() {
		return duplicateMaxDaysFromNow;
	}

}
