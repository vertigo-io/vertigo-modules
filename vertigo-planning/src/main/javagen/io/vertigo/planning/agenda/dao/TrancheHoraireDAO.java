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
import io.vertigo.planning.agenda.domain.TrancheHoraire;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class TrancheHoraireDAO extends DAO<TrancheHoraire, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 * @param smartTypeManager SmartTypeManager
	 */
	@Inject
	public TrancheHoraireDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final SmartTypeManager smartTypeManager) {
		super(TrancheHoraire.class, entityStoreManager, taskManager, smartTypeManager);
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
	 * Execute la tache TkBatchInsertTrancheHoraire.
	 * @param dto DtList de TrancheHoraire
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkBatchInsertTrancheHoraire",
			request = """
			insert into TRANCHE_HORAIRE (TRH_ID, AGE_ID, DATE_LOCALE, INSTANT_PUBLICATION, MINUTES_DEBUT, MINUTES_FIN, NB_GUICHET, PLH_ID) 
            values (nextval('SEQ_TRANCHE_HORAIRE'),  
                #dto.ageId#,  
                #dto.dateLocale#,  
                #dto.instantPublication#,  
                #dto.minutesDebut#,  
                #dto.minutesFin#, 
                #dto.nbGuichet#,  
                #dto.plhId#);""",
			taskEngineClass = io.vertigo.basics.task.TaskEngineProcBatch.class)
	public void batchInsertTrancheHoraire(@io.vertigo.datamodel.task.proxy.TaskInput(name = "dto", smartType = "STyDtTrancheHoraire") final io.vertigo.datamodel.data.model.DtList<io.vertigo.planning.agenda.domain.TrancheHoraire> dto) {
		final Task task = createTaskBuilder("TkBatchInsertTrancheHoraire")
				.addValue("dto", dto)
				.build();
		getTaskManager().execute(task);
	}

	/**
	 * Execute la tache TkGetTrancheHorairesDisponibleByAgeIds.
	 * @param ageIds List de Long
	 * @param startDate LocalDate
	 * @param endDate LocalDate
	 * @param now Instant
	 * @param displayDayMin LocalDate
	 * @param displayMinutesMin Integer
	 * @return DtList de TrancheHoraire trancheHoraires
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkGetTrancheHorairesDisponibleByAgeIds",
			request = """
			SELECT trh.*
           FROM tranche_horaire trh
           WHERE trh.age_id in ( #ageIds.rownum# )
           AND trh.date_locale BETWEEN #startDate# AND #endDate#
           AND trh.date_locale >= #displayDayMin#
           AND (trh.date_locale > #displayDayMin# OR trh.minutes_Debut > #displayMinutesMin#)
           AND trh.instant_Publication <= #now#
           AND EXISTS (
               SELECT 1 FROM creneau cre 
               WHERE cre.trh_id = trh.trh_id
               AND cre.rec_id is null)
           order by trh.date_locale, trh.minutes_Debut;""",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtTrancheHoraire", name = "trancheHoraires")
	public io.vertigo.datamodel.data.model.DtList<io.vertigo.planning.agenda.domain.TrancheHoraire> getTrancheHorairesDisponibleByAgeIds(@io.vertigo.datamodel.task.proxy.TaskInput(name = "ageIds", smartType = "STyPId") final java.util.List<Long> ageIds, @io.vertigo.datamodel.task.proxy.TaskInput(name = "startDate", smartType = "STyPLocalDate") final java.time.LocalDate startDate, @io.vertigo.datamodel.task.proxy.TaskInput(name = "endDate", smartType = "STyPLocalDate") final java.time.LocalDate endDate, @io.vertigo.datamodel.task.proxy.TaskInput(name = "now", smartType = "STyPInstant") final java.time.Instant now, @io.vertigo.datamodel.task.proxy.TaskInput(name = "displayDayMin", smartType = "STyPLocalDate") final java.time.LocalDate displayDayMin, @io.vertigo.datamodel.task.proxy.TaskInput(name = "displayMinutesMin", smartType = "STyPHeureMinute") final Integer displayMinutesMin) {
		final Task task = createTaskBuilder("TkGetTrancheHorairesDisponibleByAgeIds")
				.addValue("ageIds", ageIds)
				.addValue("startDate", startDate)
				.addValue("endDate", endDate)
				.addValue("now", now)
				.addValue("displayDayMin", displayDayMin)
				.addValue("displayMinutesMin", displayMinutesMin)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetTrancheHorairesFermeesByAgeIds.
	 * @param ageIds List de Long
	 * @param startDate LocalDate
	 * @param endDate LocalDate
	 * @return DtList de TrancheHoraire trancheHoraires
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkGetTrancheHorairesFermeesByAgeIds",
			request = """
			SELECT trh.*
           FROM tranche_horaire trh
           WHERE trh.age_id in ( #ageIds.rownum# )
           AND trh.date_locale BETWEEN #startDate# AND #endDate#
           AND trh.nb_guichet = 0
           order by trh.date_locale, trh.minutes_Debut;""",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtTrancheHoraire", name = "trancheHoraires")
	public io.vertigo.datamodel.data.model.DtList<io.vertigo.planning.agenda.domain.TrancheHoraire> getTrancheHorairesFermeesByAgeIds(@io.vertigo.datamodel.task.proxy.TaskInput(name = "ageIds", smartType = "STyPId") final java.util.List<Long> ageIds, @io.vertigo.datamodel.task.proxy.TaskInput(name = "startDate", smartType = "STyPLocalDate") final java.time.LocalDate startDate, @io.vertigo.datamodel.task.proxy.TaskInput(name = "endDate", smartType = "STyPLocalDate") final java.time.LocalDate endDate) {
		final Task task = createTaskBuilder("TkGetTrancheHorairesFermeesByAgeIds")
				.addValue("ageIds", ageIds)
				.addValue("startDate", startDate)
				.addValue("endDate", endDate)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkSelectFreeTrancheHorairesByAgeId.
	 * @param ageId Long
	 * @param startDate LocalDate
	 * @param endDate LocalDate
	 * @param now Instant
	 * @return DtList de TrancheHoraire trancheHoraires
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkSelectFreeTrancheHorairesByAgeId",
			request = """
			SELECT trh.*
           FROM tranche_horaire trh
           WHERE trh.age_id = #ageId#
           AND trh.date_locale BETWEEN #startDate# AND #endDate#
           AND trh.instant_Publication <= #now#
           AND EXISTS (
               SELECT 1 FROM creneau cre 
               WHERE cre.trh_id = trh.trh_id
               AND cre.rec_id is null)""",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtTrancheHoraire", name = "trancheHoraires")
	public io.vertigo.datamodel.data.model.DtList<io.vertigo.planning.agenda.domain.TrancheHoraire> selectFreeTrancheHorairesByAgeId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "ageId", smartType = "STyPId") final Long ageId, @io.vertigo.datamodel.task.proxy.TaskInput(name = "startDate", smartType = "STyPLocalDate") final java.time.LocalDate startDate, @io.vertigo.datamodel.task.proxy.TaskInput(name = "endDate", smartType = "STyPLocalDate") final java.time.LocalDate endDate, @io.vertigo.datamodel.task.proxy.TaskInput(name = "now", smartType = "STyPInstant") final java.time.Instant now) {
		final Task task = createTaskBuilder("TkSelectFreeTrancheHorairesByAgeId")
				.addValue("ageId", ageId)
				.addValue("startDate", startDate)
				.addValue("endDate", endDate)
				.addValue("now", now)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkSynchroGetTrancheHorairesByAgeIds.
	 * @param ageIds List de Long
	 * @param now Instant
	 * @return DtList de TrancheHoraire trancheHoraires
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkSynchroGetTrancheHorairesByAgeIds",
			request = """
			select * from (
            select 
                trh.trh_id,
                trh.age_id,
                trh.date_locale,
                trh.minutes_debut,
                trh.minutes_fin,
                trh.instant_publication,
                sum( case when cre.rec_id is null then 1 else 0 end) as nb_guichet
                from creneau cre
                join tranche_horaire trh on cre.trh_id = trh.trh_id
                where trh.age_id in ( #ageIds.rownum# )     
                AND trh.date_locale >= #now#::Date
                AND trh.instant_publication <= #now#
                group by trh.trh_id, trh.date_locale, trh.minutes_debut
                order by trh.date_locale, trh.minutes_debut
         ) as dispo
         where dispo.nb_guichet > 0""",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtTrancheHoraire", name = "trancheHoraires")
	public io.vertigo.datamodel.data.model.DtList<io.vertigo.planning.agenda.domain.TrancheHoraire> synchroGetTrancheHorairesByAgeIds(@io.vertigo.datamodel.task.proxy.TaskInput(name = "ageIds", smartType = "STyPId") final java.util.List<Long> ageIds, @io.vertigo.datamodel.task.proxy.TaskInput(name = "now", smartType = "STyPInstant") final java.time.Instant now) {
		final Task task = createTaskBuilder("TkSynchroGetTrancheHorairesByAgeIds")
				.addValue("ageIds", ageIds)
				.addValue("now", now)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkSynchroGetTrancheHorairesByAgeIdsAndDates.
	 * @param ageIds List de Long
	 * @param localDates List de LocalDate
	 * @param now Instant
	 * @return DtList de TrancheHoraire trancheHoraires
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkSynchroGetTrancheHorairesByAgeIdsAndDates",
			request = """
			select * from (
            select 
                trh.trh_id,
                trh.age_id,
                trh.date_locale,
                trh.minutes_debut,
                trh.instant_publication,
                sum( case when cre.rec_id is null then 1 else 0 end) as nb_guichet
                from creneau cre
                join tranche_horaire trh on cre.trh_id = trh.trh_id
                where trh.age_id in ( #ageIds.rownum# ) 
                AND trh.date_locale in (#localDates.rownum#)
                AND trh.instant_publication <= #now#
                group by trh.trh_id, trh.date_locale, trh.minutes_debut
                order by trh.date_locale, trh.minutes_debut
         ) as dispo
         where dispo.nb_guichet > 0""",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtTrancheHoraire", name = "trancheHoraires")
	public io.vertigo.datamodel.data.model.DtList<io.vertigo.planning.agenda.domain.TrancheHoraire> synchroGetTrancheHorairesByAgeIdsAndDates(@io.vertigo.datamodel.task.proxy.TaskInput(name = "ageIds", smartType = "STyPId") final java.util.List<Long> ageIds, @io.vertigo.datamodel.task.proxy.TaskInput(name = "localDates", smartType = "STyPLocalDate") final java.util.List<java.time.LocalDate> localDates, @io.vertigo.datamodel.task.proxy.TaskInput(name = "now", smartType = "STyPInstant") final java.time.Instant now) {
		final Task task = createTaskBuilder("TkSynchroGetTrancheHorairesByAgeIdsAndDates")
				.addValue("ageIds", ageIds)
				.addValue("localDates", localDates)
				.addValue("now", now)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

}
