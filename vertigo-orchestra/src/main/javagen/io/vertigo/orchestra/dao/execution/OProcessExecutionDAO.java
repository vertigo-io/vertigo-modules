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
package io.vertigo.orchestra.dao.execution;

import javax.inject.Inject;

import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.datamodel.task.definitions.TaskDefinition;
import io.vertigo.datamodel.task.model.Task;
import io.vertigo.datamodel.task.model.TaskBuilder;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.datastore.impl.dao.DAO;
import io.vertigo.datastore.impl.dao.StoreServices;
import io.vertigo.orchestra.domain.execution.OProcessExecution;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OProcessExecutionDAO extends DAO<OProcessExecution, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 * @param smartTypeManager SmartTypeManager
	 */
	@Inject
	public OProcessExecutionDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final SmartTypeManager smartTypeManager) {
		super(OProcessExecution.class, entityStoreManager, taskManager, smartTypeManager);
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
	 * Execute la tache TkGetActiveProcessExecutionByProId.
	 * @param proId Long
	 * @return DtList de OProcessExecution dtcProcessExecution
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetActiveProcessExecutionByProId",
			request = "select \n" + 
 "         		pre.*\n" + 
 "         	from o_process_execution pre\n" + 
 "         	where pre.PRO_ID = #proId#\n" + 
 "         	and (pre.EST_CD = 'WAITING' or pre.EST_CD = 'RESERVED' or pre.EST_CD = 'SUBMITTED' or pre.EST_CD = 'RUNNING' or pre.EST_CD = 'PENDING')",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessExecution", name = "dtcProcessExecution")
	public io.vertigo.datamodel.data.model.DtList<io.vertigo.orchestra.domain.execution.OProcessExecution> getActiveProcessExecutionByProId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "proId", smartType = "STyOIdentifiant") final Long proId) {
		final Task task = createTaskBuilder("TkGetActiveProcessExecutionByProId")
				.addValue("proId", proId)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetExecutionsByProId.
	 * @param proId Long
	 * @return DtList de OProcessExecution dtcOProcessExecution
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetExecutionsByProId",
			request = "select pre.*\n" + 
 "         	from o_process_execution pre\n" + 
 "         	where pre.PRO_ID = #proId#\n" + 
 "         	order by pre.begin_time desc",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessExecution", name = "dtcOProcessExecution")
	public io.vertigo.datamodel.data.model.DtList<io.vertigo.orchestra.domain.execution.OProcessExecution> getExecutionsByProId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "proId", smartType = "STyOIdentifiant") final Long proId) {
		final Task task = createTaskBuilder("TkGetExecutionsByProId")
				.addValue("proId", proId)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

}
