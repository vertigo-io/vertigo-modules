package io.vertigo.orchestra.dao.planification;

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
import io.vertigo.orchestra.domain.planification.OProcessPlanification;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OProcessPlanificationDAO extends DAO<OProcessPlanification, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public OProcessPlanificationDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(OProcessPlanification.class, storeManager, taskManager);
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
	 * Execute la tache TkGetAllLastPastPlanifications.
	 * @param currentDate java.time.Instant 
	 * @return io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.planification.OProcessPlanification> processPlanifications
	*/
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.planification.OProcessPlanification> getAllLastPastPlanifications(final java.time.Instant currentDate) {
		final Task task = createTaskBuilder("TkGetAllLastPastPlanifications")
				.addValue("currentDate", currentDate)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetLastPlanificationByProId.
	 * @param proId Long 
	 * @return Option de io.vertigo.orchestra.domain.planification.OProcessPlanification dtOProcessPlanification
	*/
	public Optional<io.vertigo.orchestra.domain.planification.OProcessPlanification> getLastPlanificationByProId(final Long proId) {
		final Task task = createTaskBuilder("TkGetLastPlanificationByProId")
				.addValue("proId", proId)
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.planification.OProcessPlanification) getTaskManager()
				.execute(task)
				.getResult());
	}

	/**
	 * Execute la tache TkGetPlanificationsByProId.
	 * @param proId Long 
	 * @return io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.planification.OProcessPlanification> dtcOProcessPlanification
	*/
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.planification.OProcessPlanification> getPlanificationsByProId(final Long proId) {
		final Task task = createTaskBuilder("TkGetPlanificationsByProId")
				.addValue("proId", proId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetProcessToExecute.
	 * @param nodId Long 
	 * @return io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.planification.OProcessPlanification> dtcOProcessPlanification
	*/
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.planification.OProcessPlanification> getProcessToExecute(final Long nodId) {
		final Task task = createTaskBuilder("TkGetProcessToExecute")
				.addValue("nodId", nodId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

}
