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
package io.vertigo.orchestra.monitoring.dao.uiexecutions;

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
public final class UiexecutionsPAO implements StoreServices {
	private final TaskManager taskManager;

	/**
	 * Constructeur.
	 * @param taskManager Manager des Task
	 */
	@Inject
	public UiexecutionsPAO(final TaskManager taskManager) {
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
	 * Execute la tache TkGetActivitiesByPreId.
	 * @param preId Long
	 * @return DtList de OActivityExecutionUi dtcOActivityExecutionUi
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetActivitiesByPreId",
			request = "select  ace.ACE_ID as ACE_ID,\n" + 
 "         			act.LABEL as LABEL,\n" + 
 "         			ace.BEGIN_TIME as BEGIN_TIME,\n" + 
 "         			ace.END_TIME as END_TIME,\n" + 
 "         			round(extract('epoch' from (ace.END_TIME-ace.BEGIN_TIME))) as EXECUTION_TIME,\n" + 
 "         			ace.EST_CD as STATUS,\n" + 
 "         			max((case when acw.IS_IN is true then acw.WORKSPACE else null end)) as WORKSPACE_IN,\n" + 
 "         			max((case when acw.IS_IN is false then acw.WORKSPACE else null end)) as WORKSPACE_OUT,\n" + 
 "         			acl.ATTACHMENT is not null as HAS_ATTACHMENT,\n" + 
 "         			acl.LOG is not null as HAS_TECHNICAL_LOG\n" + 
 "         	from o_activity_execution ace\n" + 
 "         	join o_activity act on act.ACT_ID = ace.ACT_ID\n" + 
 "         	join o_activity_workspace acw on acw.ACE_ID = ace.ACE_ID\n" + 
 "         	left join o_activity_log acl on acl.ACE_ID = ace.ACE_ID\n" + 
 "         	where ace.PRE_ID = #preId#\n" + 
 "         	group by ace.ACE_ID, act.LABEL, ace.BEGIN_TIME, ace.END_TIME, acl.ATTACHMENT, acl.LOG, ace.EST_CD\n" +
 "          order by ace.BEGIN_TIME asc",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOActivityExecutionUi", name = "dtcOActivityExecutionUi")
	public io.vertigo.datamodel.data.model.DtList<io.vertigo.orchestra.monitoring.domain.uiexecutions.OActivityExecutionUi> getActivitiesByPreId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "preId", smartType = "STyOIdentifiant") final Long preId) {
		final Task task = createTaskBuilder("TkGetActivitiesByPreId")
				.addValue("preId", preId)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetActivitiyByAceId.
	 * @param aceId Long
	 * @return OActivityExecutionUi dtOActivityExecutionUi
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetActivitiyByAceId",
			request = "select  ace.ACE_ID as ACE_ID,\n" + 
 "         			act.LABEL as LABEL,\n" + 
 "         			ace.BEGIN_TIME as BEGIN_TIME,\n" + 
 "         			ace.END_TIME as END_TIME,\n" + 
 "         			round(extract('epoch' from (ace.END_TIME-ace.BEGIN_TIME))) as EXECUTION_TIME,\n" + 
 "         			ace.EST_CD as STATUS,\n" + 
 "         			max((case when acw.IS_IN is true then acw.WORKSPACE else null end)) as WORKSPACE_IN,\n" + 
 "         			max((case when acw.IS_IN is false then acw.WORKSPACE else null end)) as WORKSPACE_OUT,\n" + 
 "         			acl.ATTACHMENT is not null as HAS_ATTACHMENT,\n" + 
 "         			acl.LOG is not null as HAS_TECHNICAL_LOG\n" + 
 "         	from o_activity_execution ace\n" + 
 "         	join o_activity act on act.ACT_ID = ace.ACT_ID\n" + 
 "         	join o_activity_workspace acw on acw.ACE_ID = ace.ACE_ID\n" + 
 "         	left join o_activity_log acl on acl.ACE_ID = ace.ACE_ID\n" + 
 "         	where ace.ACE_ID = #aceId#\n" + 
 "         	group by ace.ACE_ID, act.LABEL, ace.BEGIN_TIME, ace.END_TIME, acl.ATTACHMENT, acl.LOG, ace.EST_CD\n" +
 "          order by ace.BEGIN_TIME asc",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOActivityExecutionUi", name = "dtOActivityExecutionUi")
	public io.vertigo.orchestra.monitoring.domain.uiexecutions.OActivityExecutionUi getActivitiyByAceId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "aceId", smartType = "STyOIdentifiant") final Long aceId) {
		final Task task = createTaskBuilder("TkGetActivitiyByAceId")
				.addValue("aceId", aceId)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetExecutionByPreId.
	 * @param preId Long
	 * @return OProcessExecutionUi dtOProcessExecutionUi
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetExecutionByPreId",
			request = "select  pre.PRE_ID as PRE_ID,\n" + 
 "         			pre.BEGIN_TIME as BEGIN_TIME,\n" + 
 "         			pre.END_TIME as END_TIME,\n" + 
 "         			round(extract('epoch' from (pre.END_TIME-pre.BEGIN_TIME))) as EXECUTION_TIME,\n" + 
 "         			pre.EST_CD as STATUS,\n" + 
 "         			pre.CHECKED as CHECKED,\n" + 
 "         			pre.CHECKING_DATE as CHECKING_DATE,\n" + 
 "         			pre.CHECKING_COMMENT as CHECKING_COMMENT,\n" + 
 "         			(select \n" + 
 " 			        	acl.attachment is not null\n" + 
 " 						from o_activity_execution ace\n" + 
 " 						left join o_activity_log acl on acl.ACE_ID = ace.ACE_ID\n" + 
 " 						where ace.PRE_ID = #preId#\n" + 
 " 						order by ace.end_time desc limit 1) as HAS_ATTACHMENT\n" + 
 "         	from o_process_execution pre   \n" + 
 "         	where pre.PRE_ID = #preId# \n" +
 "          order by pre.begin_time desc",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessExecutionUi", name = "dtOProcessExecutionUi")
	public io.vertigo.orchestra.monitoring.domain.uiexecutions.OProcessExecutionUi getExecutionByPreId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "preId", smartType = "STyOIdentifiant") final Long preId) {
		final Task task = createTaskBuilder("TkGetExecutionByPreId")
				.addValue("preId", preId)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetExecutionsByProcessName.
	 * @param name String
	 * @param status String
	 * @param limit Integer
	 * @param offset Integer
	 * @return DtList de OProcessExecutionUi dtcOProcessExecutionUi
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetExecutionsByProcessName",
			request = "select  pre.PRE_ID as PRE_ID,\n" + 
 "         			pre.BEGIN_TIME as BEGIN_TIME,\n" + 
 "         			pre.END_TIME as END_TIME,\n" + 
 "         			round(extract('epoch' from (pre.END_TIME-pre.BEGIN_TIME))) as EXECUTION_TIME,\n" + 
 "         			pre.EST_CD as STATUS\n" + 
 "         	from o_process pro\n" + 
 "         	join o_process_execution pre on pro.PRO_ID = pre.PRO_ID\n" + 
 "         	where pro.NAME = #name#\n" + 
 "         	<%if (!\"\".equals(status)) {%>\n" + 
 "         		and pre.EST_CD = #status#\n" + 
 "         	<%}%>\n" + 
 "         	order by pre.begin_time desc\n" + 
 "         	limit #limit#\n" + 
 "         	offset #offset#",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessExecutionUi", name = "dtcOProcessExecutionUi")
	public io.vertigo.datamodel.data.model.DtList<io.vertigo.orchestra.monitoring.domain.uiexecutions.OProcessExecutionUi> getExecutionsByProcessName(@io.vertigo.datamodel.task.proxy.TaskInput(name = "name", smartType = "STyOLibelle") final String name, @io.vertigo.datamodel.task.proxy.TaskInput(name = "status", smartType = "STyOCodeIdentifiant") final String status, @io.vertigo.datamodel.task.proxy.TaskInput(name = "limit", smartType = "STyONombre") final Integer limit, @io.vertigo.datamodel.task.proxy.TaskInput(name = "offset", smartType = "STyONombre") final Integer offset) {
		final Task task = createTaskBuilder("TkGetExecutionsByProcessName")
				.addValue("name", name)
				.addValue("status", status)
				.addValue("limit", limit)
				.addValue("offset", offset)
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
