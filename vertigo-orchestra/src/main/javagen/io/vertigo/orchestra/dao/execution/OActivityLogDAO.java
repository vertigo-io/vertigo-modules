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

import java.util.Optional;

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
import io.vertigo.orchestra.domain.execution.OActivityLog;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OActivityLogDAO extends DAO<OActivityLog, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 * @param smartTypeManager SmartTypeManager
	 */
	@Inject
	public OActivityLogDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final SmartTypeManager smartTypeManager) {
		super(OActivityLog.class, entityStoreManager, taskManager, smartTypeManager);
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
	 * Execute la tache TkGetActivityLogByAceId.
	 * @param aceId Long
	 * @return Option de OActivityLog dtcOActivityLog
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetActivityLogByAceId",
			request = "select acl.*\n" + 
 "         	from o_activity_log acl\n" + 
 "         	where acl.ACE_ID = #aceId#",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOActivityLog", name = "dtcOActivityLog")
	public Optional<io.vertigo.orchestra.domain.execution.OActivityLog> getActivityLogByAceId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "aceId", smartType = "STyOIdentifiant") final Long aceId) {
		final Task task = createTaskBuilder("TkGetActivityLogByAceId")
				.addValue("aceId", aceId)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.execution.OActivityLog) getTaskManager()
				.execute(task)
				.getResult());
	}

	/**
	 * Execute la tache TkGetLogByPreId.
	 * @param preId Long
	 * @return Option de OActivityLog dtActivityLog
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetLogByPreId",
			request = "select \n" + 
 "         	acl.*\n" + 
 " 			from o_activity_execution ace\n" + 
 " 			join o_activity_log acl on acl.ACE_ID = ace.ACE_ID\n" + 
 " 			where ace.PRE_ID = #preId#\n" + 
 " 			order by ace.end_time desc limit 1",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOActivityLog", name = "dtActivityLog")
	public Optional<io.vertigo.orchestra.domain.execution.OActivityLog> getLogByPreId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "preId", smartType = "STyOIdentifiant") final Long preId) {
		final Task task = createTaskBuilder("TkGetLogByPreId")
				.addValue("preId", preId)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.execution.OActivityLog) getTaskManager()
				.execute(task)
				.getResult());
	}

}
