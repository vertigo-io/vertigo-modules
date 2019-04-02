package io.vertigo.orchestra.dao.execution;

import javax.inject.Inject;

import io.vertigo.app.Home;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskBuilder;
import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.orchestra.domain.execution.OProcessExecution;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OProcessExecutionDAO extends DAO<OProcessExecution, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public OProcessExecutionDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(OProcessExecution.class, storeManager, taskManager);
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
	 * Execute la tache TkGetActiveProcessExecutionByProId.
	 * @param proId Long 
	 * @return io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.execution.OProcessExecution> dtcProcessExecution
	*/
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.execution.OProcessExecution> getActiveProcessExecutionByProId(final Long proId) {
		final Task task = createTaskBuilder("TkGetActiveProcessExecutionByProId")
				.addValue("proId", proId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetExecutionsByProId.
	 * @param proId Long 
	 * @return io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.execution.OProcessExecution> dtcOProcessExecution
	*/
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.execution.OProcessExecution> getExecutionsByProId(final Long proId) {
		final Task task = createTaskBuilder("TkGetExecutionsByProId")
				.addValue("proId", proId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

}
