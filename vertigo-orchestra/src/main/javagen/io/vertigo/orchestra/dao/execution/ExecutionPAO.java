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
package io.vertigo.orchestra.dao.execution;

import javax.inject.Inject;

import io.vertigo.core.node.Node;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Generated;
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
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkHandleDeadProcessesOfNode",
			request = "update o_activity_execution " + 
 "        	set EST_CD = 'ABORTED'" + 
 "        	where ACE_ID in (" + 
 "				select ace.ACE_ID" + 
 "				from o_activity_execution ace" + 
 "				where ace.EST_CD in ('RUNNING','WAITING','SUBMITTED') and ace.NOD_ID = #nodId#);" + 
 "			" + 
 " 			update o_process_execution" + 
 "        	set EST_CD = 'ABORTED'" + 
 "        	where PRE_ID in (" + 
 "				select pre.PRE_ID" + 
 "				from o_process_execution pre" + 
 "				join o_activity_execution ace on ace.PRE_ID = pre.PRE_ID" + 
 "				where ace.EST_CD = 'ABORTED');",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProc.class)
	public void handleDeadProcessesOfNode(@io.vertigo.datamodel.task.proxy.TaskInput(name = "nodId", smartType = "STyOIdentifiant") final Long nodId) {
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
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkHandleProcessesOfDeadNodes",
			request = "update o_activity_execution " + 
 "        	set EST_CD = 'ABORTED'" + 
 "        	where ACE_ID in (" + 
 "				select ace.ACE_ID" + 
 "				from o_activity_execution ace" + 
 "				join o_node nod on nod.NOD_ID = ace.NOD_ID" + 
 "				where ace.EST_CD in ('RUNNING','WAITING','SUBMITTED') and nod.HEARTBEAT < #maxDate#);" + 
 "			" + 
 " 			update o_process_execution" + 
 "        	set EST_CD = 'ABORTED'" + 
 "        	where PRE_ID in (" + 
 "				select pre.PRE_ID" + 
 "				from o_process_execution pre" + 
 "				join o_activity_execution ace on ace.PRE_ID = pre.PRE_ID" + 
 "				where ace.EST_CD = 'ABORTED');",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProc.class)
	public void handleProcessesOfDeadNodes(@io.vertigo.datamodel.task.proxy.TaskInput(name = "maxDate", smartType = "STyOTimestamp") final java.time.Instant maxDate) {
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
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkReserveActivitiesToLaunch",
			request = "update  o_activity_execution " + 
 "        	set EST_CD = 'RESERVED'," + 
 "        		NOD_ID = #nodId#" + 
 "        		" + 
 "        	where ace_id in (" + 
 "        			select ace_id " + 
 "        			from o_activity_execution" + 
 "        			where EST_CD = 'WAITING' " + 
 "        			order by creation_time asc" + 
 "        			limit #maxNumber#" + 
 "        	)",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProc.class)
	public void reserveActivitiesToLaunch(@io.vertigo.datamodel.task.proxy.TaskInput(name = "nodId", smartType = "STyOIdentifiant") final Long nodId, @io.vertigo.datamodel.task.proxy.TaskInput(name = "maxNumber", smartType = "STyONombre") final Integer maxNumber) {
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
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkUpdateProcessExecutionTreatment",
			request = "update o_process_execution" + 
 "        	set CHECKED = #checked# ," + 
 "        		CHECKING_DATE = #checkingDate#," + 
 "        		CHECKING_COMMENT = #checkingComment#" + 
 "        		where PRE_ID = #preId#",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProc.class)
	public void updateProcessExecutionTreatment(@io.vertigo.datamodel.task.proxy.TaskInput(name = "preId", smartType = "STyOIdentifiant") final Long preId, @io.vertigo.datamodel.task.proxy.TaskInput(name = "checked", smartType = "STyOBooleen") final Boolean checked, @io.vertigo.datamodel.task.proxy.TaskInput(name = "checkingDate", smartType = "STyOTimestamp") final java.time.Instant checkingDate, @io.vertigo.datamodel.task.proxy.TaskInput(name = "checkingComment", smartType = "STyOText") final String checkingComment) {
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
