package io.vertigo.orchestra.dao.execution;

import javax.inject.Inject;

import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.Home;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskBuilder;
import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.orchestra.domain.execution.OActivityExecution;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OActivityExecutionDAO extends DAO<OActivityExecution, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public OActivityExecutionDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(OActivityExecution.class, storeManager, taskManager);
	}


	/**
	 * Creates a taskBuilder.
	 * @param name  the name of the task
	 * @return the builder 
	 */
	private static TaskBuilder createTaskBuilder(final String name) {
		final TaskDefinition taskDefinition = Home.getApp().getDefinitionSpace().resolve(name, TaskDefinition.class);
		return Task.builder(taskDefinition);
	}

	/**
	 * Execute la tache TkGetActivitiesToLaunch.
	 * @param nodId Long
	 * @return DtList de OActivityExecution dtcActivityExecution
	*/
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.execution.OActivityExecution> getActivitiesToLaunch(final Long nodId) {
		final Task task = createTaskBuilder("TkGetActivitiesToLaunch")
				.addValue("nodId", nodId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetActivityExecutionByToken.
	 * @param aceId Long
	 * @param token String
	 * @return OActivityExecution dtActivityExecution
	*/
	public io.vertigo.orchestra.domain.execution.OActivityExecution getActivityExecutionByToken(final Long aceId, final String token) {
		final Task task = createTaskBuilder("TkGetActivityExecutionByToken")
				.addValue("aceId", aceId)
				.addValue("token", token)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetActivityExecutionsByPreId.
	 * @param preId Long
	 * @return DtList de OActivityExecution dtcOActivityExecution
	*/
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.execution.OActivityExecution> getActivityExecutionsByPreId(final Long preId) {
		final Task task = createTaskBuilder("TkGetActivityExecutionsByPreId")
				.addValue("preId", preId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

}
