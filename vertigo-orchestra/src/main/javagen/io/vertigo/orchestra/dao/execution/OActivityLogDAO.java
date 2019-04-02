package io.vertigo.orchestra.dao.execution;

import javax.inject.Inject;

import java.util.Optional;
import io.vertigo.app.Home;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskBuilder;
import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.orchestra.domain.execution.OActivityLog;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OActivityLogDAO extends DAO<OActivityLog, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public OActivityLogDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(OActivityLog.class, storeManager, taskManager);
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
	 * Execute la tache TkGetActivityLogByAceId.
	 * @param aceId Long 
	 * @return Option de io.vertigo.orchestra.domain.execution.OActivityLog dtcOActivityLog
	*/
	public Optional<io.vertigo.orchestra.domain.execution.OActivityLog> getActivityLogByAceId(final Long aceId) {
		final Task task = createTaskBuilder("TkGetActivityLogByAceId")
				.addValue("aceId", aceId)
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.execution.OActivityLog) getTaskManager()
				.execute(task)
				.getResult());
	}

	/**
	 * Execute la tache TkGetLogByPreId.
	 * @param preId Long 
	 * @return Option de io.vertigo.orchestra.domain.execution.OActivityLog dtActivityLog
	*/
	public Optional<io.vertigo.orchestra.domain.execution.OActivityLog> getLogByPreId(final Long preId) {
		final Task task = createTaskBuilder("TkGetLogByPreId")
				.addValue("preId", preId)
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.execution.OActivityLog) getTaskManager()
				.execute(task)
				.getResult());
	}

}
