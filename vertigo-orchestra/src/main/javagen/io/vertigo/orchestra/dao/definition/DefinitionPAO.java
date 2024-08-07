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
package io.vertigo.orchestra.dao.definition;

import javax.inject.Inject;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.datamodel.task.definitions.TaskDefinition;
import io.vertigo.datamodel.task.model.Task;
import io.vertigo.datamodel.task.model.TaskBuilder;
import io.vertigo.datastore.impl.dao.StoreServices;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class DefinitionPAO implements StoreServices {
	private final TaskManager taskManager;

	/**
	 * Constructeur.
	 * @param taskManager Manager des Task
	 */
	@Inject
	public DefinitionPAO(final TaskManager taskManager) {
		Assertion.check().isNotNull(taskManager);
		//-----
		this.taskManager = taskManager;
	}

	/**
	 * Creates a taskBuilder.
	 * @param name  the name of the task
	 * @return the builder
	 */
	private static TaskBuilder createTaskBuilder(final String name) {
		final TaskDefinition taskDefinition = Node.getNode().getDefinitionSpace().resolve(name, TaskDefinition.class);
		return Task.builder(taskDefinition);
	}

	/**
	 * Execute la tache TkDisableOldProcessDefinitions.
	 * @param name String
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkDisableOldProcessDefinitions",
			request = "update o_process \n" +
					"         	set ACTIVE_VERSION = false,\n" +
					"         		NEED_UPDATE = false\n" +
					"         	where NAME = #name#",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProc.class)
	public void disableOldProcessDefinitions(@io.vertigo.datamodel.task.proxy.TaskInput(name = "name", smartType = "STyOLibelle") final String name) {
		final Task task = createTaskBuilder("TkDisableOldProcessDefinitions")
				.addValue("name", name)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		getTaskManager().execute(task);
	}

	/**
	 * Execute la tache TkLockProcessesForInsert.
	 * @param name String
	 * @return Integer nombre
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkLockProcessesForInsert",
			request = "SELECT 1 FROM o_process_type WHERE prt_cd = 'DUMB' FOR UPDATE",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyONombre", name = "nombre")
	public Integer lockProcessesForInsert() {
		final Task task = createTaskBuilder("TkLockProcessesForInsert")
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkCountProcessesByName.
	 * @param name String
	 * @return Integer nombre
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkCountProcessesByName",
			request = "select count(*) from o_process pro where pro.NAME = #name#;",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyONombre", name = "nombre")
	public Integer countProcessesByName(@io.vertigo.datamodel.task.proxy.TaskInput(name = "name", smartType = "STyOLibelle") final String name) {
		final Task task = createTaskBuilder("TkCountProcessesByName")
				.addValue("name", name)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	private TaskManager getTaskManager() {
		return taskManager;
	}
}
