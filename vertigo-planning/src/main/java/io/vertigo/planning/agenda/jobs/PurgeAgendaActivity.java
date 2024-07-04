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
package io.vertigo.planning.agenda.jobs;

import java.text.MessageFormat;
import java.time.LocalDate;

import javax.inject.Inject;

import io.vertigo.core.lang.Assertion;
import io.vertigo.orchestra.impl.services.execution.AbstractActivityEngine;
import io.vertigo.orchestra.services.execution.ActivityExecutionWorkspace;
import io.vertigo.planning.agenda.services.PlanningServices;

public class PurgeAgendaActivity extends AbstractActivityEngine {

	public static final String PARAM_OLDER_THAN_DAYS = "olderThanDays";

	@Inject
	PlanningServices planningServices;

	@Override
	public ActivityExecutionWorkspace execute(ActivityExecutionWorkspace workspace) throws Exception {
		Assertion.check().isTrue(workspace.containsKey(PARAM_OLDER_THAN_DAYS),
				"Purge Agenda : parameter {0} is not defined", PARAM_OLDER_THAN_DAYS);
		LocalDate olderThan = LocalDate.now()
				.minusDays(Integer.parseInt(workspace.getValue(PARAM_OLDER_THAN_DAYS), 10));
		int result = planningServices.purgeAgendaOlderThan(olderThan);
		getLogger().info(MessageFormat.format("{0} [{1}] - PurgeAgendaActivity [{2}] - Purged rows : {3}",
				workspace.getProcessName(), workspace.getProcessExecutionId(), workspace.getActivityExecutionId(),
				result));
		workspace.setSuccess();
		return workspace;
	}

}
