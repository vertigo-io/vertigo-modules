/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.Node;
import io.vertigo.datafactory.task.TaskManager;
import io.vertigo.datafactory.task.definitions.TaskDefinition;
import io.vertigo.datafactory.task.model.Task;
import io.vertigo.datafactory.task.model.TaskBuilder;
import io.vertigo.datastore.impl.dao.StoreServices;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
 @Generated
public final class ExecutionPAO implements StoreServices {
	private final TaskManager taskManager;

	/**
	 * Constructeur.
	 * @param taskManager Manager des Task
	 */
	@Inject
	public ExecutionPAO(final TaskManager taskManager) {
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
	 * Execute la tache TkHandleDeadProcessesOfNode.
	 * @param nodId Long
	*/
	@io.vertigo.datafactory.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkHandleDeadProcessesOfNode",
			request = "update o_activity_execution \n" + 
 "         	set EST_CD = 'ABORTED'\n" + 
 "         	where ACE_ID in (\n" + 
 " 				select ace.ACE_ID\n" + 
 " 				from o_activity_execution ace\n" + 
 " 				where ace.EST_CD in ('RUNNING','WAITING','SUBMITTED') and ace.NOD_ID = #nodId#);\n" + 
 " 			\n" + 
 "  			update o_process_execution\n" + 
 "         	set EST_CD = 'ABORTED'\n" + 
 "         	where PRE_ID in (\n" + 
 " 				select pre.PRE_ID\n" + 
 " 				from o_process_execution pre\n" + 
 " 				join o_activity_execution ace on ace.PRE_ID = pre.PRE_ID\n" + 
 " 				where ace.EST_CD = 'ABORTED');",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProc.class)
	public void handleDeadProcessesOfNode(@io.vertigo.datafactory.task.proxy.TaskInput(name = "nodId", smartType = "STyOIdentifiant") final Long nodId) {
		final Task task = createTaskBuilder("TkHandleDeadProcessesOfNode")
				.addValue("nodId", nodId)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		getTaskManager().execute(task);
	}

	/**
	 * Execute la tache TkHandleProcessesOfDeadNodes.
	 * @param maxDate Instant
	*/
	@io.vertigo.datafactory.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkHandleProcessesOfDeadNodes",
			request = "update o_activity_execution \n" + 
 "         	set EST_CD = 'ABORTED'\n" + 
 "         	where ACE_ID in (\n" + 
 " 				select ace.ACE_ID\n" + 
 " 				from o_activity_execution ace\n" + 
 " 				join o_node nod on nod.NOD_ID = ace.NOD_ID\n" + 
 " 				where ace.EST_CD in ('RUNNING','WAITING','SUBMITTED') and nod.HEARTBEAT < #maxDate#);\n" + 
 " 			\n" + 
 "  			update o_process_execution\n" + 
 "         	set EST_CD = 'ABORTED'\n" + 
 "         	where PRE_ID in (\n" + 
 " 				select pre.PRE_ID\n" + 
 " 				from o_process_execution pre\n" + 
 " 				join o_activity_execution ace on ace.PRE_ID = pre.PRE_ID\n" + 
 " 				where ace.EST_CD = 'ABORTED');",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProc.class)
	public void handleProcessesOfDeadNodes(@io.vertigo.datafactory.task.proxy.TaskInput(name = "maxDate", smartType = "STyOTimestamp") final java.time.Instant maxDate) {
		final Task task = createTaskBuilder("TkHandleProcessesOfDeadNodes")
				.addValue("maxDate", maxDate)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		getTaskManager().execute(task);
	}

	/**
	 * Execute la tache TkReserveActivitiesToLaunch.
	 * @param nodId Long
	 * @param maxNumber Integer
	*/
	@io.vertigo.datafactory.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkReserveActivitiesToLaunch",
			request = "update  o_activity_execution \n" + 
 "         	set EST_CD = 'RESERVED',\n" + 
 "         		NOD_ID = #nodId#\n" + 
 "         		\n" + 
 "         	where ace_id in (\n" + 
 "         			select ace_id \n" + 
 "         			from o_activity_execution\n" + 
 "         			where EST_CD = 'WAITING' \n" + 
 "         			order by creation_time asc\n" + 
 "         			limit #maxNumber#\n" + 
 "         	)",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProc.class)
	public void reserveActivitiesToLaunch(@io.vertigo.datafactory.task.proxy.TaskInput(name = "nodId", smartType = "STyOIdentifiant") final Long nodId, @io.vertigo.datafactory.task.proxy.TaskInput(name = "maxNumber", smartType = "STyONombre") final Integer maxNumber) {
		final Task task = createTaskBuilder("TkReserveActivitiesToLaunch")
				.addValue("nodId", nodId)
				.addValue("maxNumber", maxNumber)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		getTaskManager().execute(task);
	}

	/**
	 * Execute la tache TkUpdateProcessExecutionTreatment.
	 * @param preId Long
	 * @param checked Boolean
	 * @param checkingDate Instant
	 * @param checkingComment String
	*/
	@io.vertigo.datafactory.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkUpdateProcessExecutionTreatment",
			request = "update o_process_execution\n" + 
 "         	set CHECKED = #checked# ,\n" + 
 "         		CHECKING_DATE = #checkingDate#,\n" + 
 "         		CHECKING_COMMENT = #checkingComment#\n" + 
 "         		where PRE_ID = #preId#",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProc.class)
	public void updateProcessExecutionTreatment(@io.vertigo.datafactory.task.proxy.TaskInput(name = "preId", smartType = "STyOIdentifiant") final Long preId, @io.vertigo.datafactory.task.proxy.TaskInput(name = "checked", smartType = "STyOBooleen") final Boolean checked, @io.vertigo.datafactory.task.proxy.TaskInput(name = "checkingDate", smartType = "STyOTimestamp") final java.time.Instant checkingDate, @io.vertigo.datafactory.task.proxy.TaskInput(name = "checkingComment", smartType = "STyOText") final String checkingComment) {
		final Task task = createTaskBuilder("TkUpdateProcessExecutionTreatment")
				.addValue("preId", preId)
				.addValue("checked", checked)
				.addValue("checkingDate", checkingDate)
				.addValue("checkingComment", checkingComment)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		getTaskManager().execute(task);
	}

	private TaskManager getTaskManager() {
		return taskManager;
	}
}
