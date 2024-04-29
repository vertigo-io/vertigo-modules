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
