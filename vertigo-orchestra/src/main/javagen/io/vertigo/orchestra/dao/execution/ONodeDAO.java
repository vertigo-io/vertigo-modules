package io.vertigo.orchestra.dao.execution;

import java.util.Optional;

import javax.inject.Inject;

import io.vertigo.app.Home;
import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskBuilder;
import io.vertigo.lang.Generated;
import io.vertigo.orchestra.domain.execution.ONode;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class ONodeDAO extends DAO<ONode, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public ONodeDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(ONode.class, storeManager, taskManager);
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
	 * Execute la tache TkGetNodeByName.
	 * @param nodeName String 
	 * @return Option de ONode dtoONode
	*/
	public Optional<io.vertigo.orchestra.domain.execution.ONode> getNodeByName(final String nodeName) {
		final Task task = createTaskBuilder("TkGetNodeByName")
				.addValue("nodeName", nodeName)
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.execution.ONode) getTaskManager()
				.execute(task)
				.getResult());
	}

}
