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
package io.vertigo.planning.agenda.dao;

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
import io.vertigo.planning.agenda.domain.PlageHoraire;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class PlageHoraireDAO extends DAO<PlageHoraire, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 * @param smartTypeManager SmartTypeManager
	 */
	@Inject
	public PlageHoraireDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final SmartTypeManager smartTypeManager) {
		super(PlageHoraire.class, entityStoreManager, taskManager, smartTypeManager);
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
	 * Execute la tache TkGetExistsConflictingPlageHoraire.
	 * @param ageIds List de Long
	 * @param dateLocale LocalDate
	 * @param heureDebut Integer
	 * @param heureFin Integer
	 * @return Option de PlageHoraire plageHoraires
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkGetExistsConflictingPlageHoraire",
			request = """
			select plh.*
           from plage_horaire plh
           WHERE plh.age_id in ( #ageIds.rownum# ) 
           AND plh.date_locale = #dateLocale#
           AND plh.minutes_Debut < #heureFin# AND plh.minutes_Fin > #heureDebut#
           LIMIT 1;""",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtPlageHoraire", name = "plageHoraires")
	public Optional<io.vertigo.planning.agenda.domain.PlageHoraire> getExistsConflictingPlageHoraire(@io.vertigo.datamodel.task.proxy.TaskInput(name = "ageIds", smartType = "STyPId") final java.util.List<Long> ageIds, @io.vertigo.datamodel.task.proxy.TaskInput(name = "dateLocale", smartType = "STyPLocalDate") final java.time.LocalDate dateLocale, @io.vertigo.datamodel.task.proxy.TaskInput(name = "heureDebut", smartType = "STyPHeureMinute") final Integer heureDebut, @io.vertigo.datamodel.task.proxy.TaskInput(name = "heureFin", smartType = "STyPHeureMinute") final Integer heureFin) {
		final Task task = createTaskBuilder("TkGetExistsConflictingPlageHoraire")
				.addValue("ageIds", ageIds)
				.addValue("dateLocale", dateLocale)
				.addValue("heureDebut", heureDebut)
				.addValue("heureFin", heureFin)
				.build();
		return Optional.ofNullable((io.vertigo.planning.agenda.domain.PlageHoraire) getTaskManager()
				.execute(task)
				.getResult());
	}

}
