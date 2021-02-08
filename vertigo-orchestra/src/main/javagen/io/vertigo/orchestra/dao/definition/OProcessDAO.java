/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2021, Vertigo.io, team@vertigo.io
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

import java.util.Optional;
import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.task.definitions.TaskDefinition;
import io.vertigo.datamodel.task.model.Task;
import io.vertigo.datamodel.task.model.TaskBuilder;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.datastore.impl.dao.DAO;
import io.vertigo.datastore.impl.dao.StoreServices;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.orchestra.domain.definition.OProcess;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OProcessDAO extends DAO<OProcess, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 * @param smartTypeManager SmartTypeManager
	 */
	@Inject
	public OProcessDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final SmartTypeManager smartTypeManager) {
		super(OProcess.class, entityStoreManager, taskManager, smartTypeManager);
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
	 * Execute la tache TkGetActiveProcessByName.
	 * @param name String
	 * @return Option de OProcess dtProcess
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetActiveProcessByName",
			request = "select " + 
 "        		pro.*" + 
 "        	from o_process pro" + 
 "        	where pro.NAME = #name#" + 
 "	        	and pro.ACTIVE_VERSION is true",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcess")
	public Optional<io.vertigo.orchestra.domain.definition.OProcess> getActiveProcessByName(@io.vertigo.datamodel.task.proxy.TaskInput(name = "name", smartType = "STyOLibelle") final String name) {
		final Task task = createTaskBuilder("TkGetActiveProcessByName")
				.addValue("name", name)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.definition.OProcess) getTaskManager()
				.execute(task)
				.getResult());
	}

	/**
	 * Execute la tache TkGetAllActiveProcesses.
	 * @return DtList de OProcess dtcProcesses
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetAllActiveProcesses",
			request = "select " + 
 "        		pro.*" + 
 "        	from o_process pro" + 
 "        	where pro.ACTIVE_VERSION is true",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcess")
	public io.vertigo.datamodel.structure.model.DtList<io.vertigo.orchestra.domain.definition.OProcess> getAllActiveProcesses() {
		final Task task = createTaskBuilder("TkGetAllActiveProcesses")
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

}
