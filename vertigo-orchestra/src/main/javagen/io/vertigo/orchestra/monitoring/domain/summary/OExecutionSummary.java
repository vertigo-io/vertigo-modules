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
package io.vertigo.orchestra.monitoring.domain.summary;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.stereotype.Field;
import io.vertigo.datamodel.data.util.DataModelUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OExecutionSummary implements DataObject {
	private static final long serialVersionUID = 1L;

	private Long proId;
	private String processName;
	private String processLabel;
	private java.time.Instant lastExecutionTime;
	private java.time.Instant nextExecutionTime;
	private Integer errorsCount;
	private Integer misfiredCount;
	private Integer successfulCount;
	private Integer runningCount;
	private Integer averageExecutionTime;
	private String health;

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Id du processus'.
	 * @return Long proId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOIdentifiant", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id du processus")
	public Long getProId() {
		return proId;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Id du processus'.
	 * @param proId Long <b>Obligatoire</b>
	 */
	public void setProId(final Long proId) {
		this.proId = proId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nom du processus'.
	 * @return String processName <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOLibelle", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Nom du processus")
	public String getProcessName() {
		return processName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nom du processus'.
	 * @param processName String <b>Obligatoire</b>
	 */
	public void setProcessName(final String processName) {
		this.processName = processName;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Libellé du processus'.
	 * @return String processLabel <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOLibelle", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Libellé du processus")
	public String getProcessLabel() {
		return processLabel;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Libellé du processus'.
	 * @param processLabel String <b>Obligatoire</b>
	 */
	public void setProcessLabel(final String processLabel) {
		this.processLabel = processLabel;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Dernière exécution le'.
	 * @return Instant lastExecutionTime
	 */
	@Field(smartType = "STyOTimestamp", label = "Dernière exécution le")
	public java.time.Instant getLastExecutionTime() {
		return lastExecutionTime;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Dernière exécution le'.
	 * @param lastExecutionTime Instant
	 */
	public void setLastExecutionTime(final java.time.Instant lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Prochaine exécution le'.
	 * @return Instant nextExecutionTime
	 */
	@Field(smartType = "STyOTimestamp", label = "Prochaine exécution le")
	public java.time.Instant getNextExecutionTime() {
		return nextExecutionTime;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Prochaine exécution le'.
	 * @param nextExecutionTime Instant
	 */
	public void setNextExecutionTime(final java.time.Instant nextExecutionTime) {
		this.nextExecutionTime = nextExecutionTime;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nombre en erreur'.
	 * @return Integer errorsCount
	 */
	@Field(smartType = "STyONombre", label = "Nombre en erreur")
	public Integer getErrorsCount() {
		return errorsCount;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nombre en erreur'.
	 * @param errorsCount Integer
	 */
	public void setErrorsCount(final Integer errorsCount) {
		this.errorsCount = errorsCount;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nombre non executés'.
	 * @return Integer misfiredCount
	 */
	@Field(smartType = "STyONombre", label = "Nombre non executés")
	public Integer getMisfiredCount() {
		return misfiredCount;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nombre non executés'.
	 * @param misfiredCount Integer
	 */
	public void setMisfiredCount(final Integer misfiredCount) {
		this.misfiredCount = misfiredCount;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nombre en succès'.
	 * @return Integer successfulCount
	 */
	@Field(smartType = "STyONombre", label = "Nombre en succès")
	public Integer getSuccessfulCount() {
		return successfulCount;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nombre en succès'.
	 * @param successfulCount Integer
	 */
	public void setSuccessfulCount(final Integer successfulCount) {
		this.successfulCount = successfulCount;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nombre en cours'.
	 * @return Integer runningCount
	 */
	@Field(smartType = "STyONombre", label = "Nombre en cours")
	public Integer getRunningCount() {
		return runningCount;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nombre en cours'.
	 * @param runningCount Integer
	 */
	public void setRunningCount(final Integer runningCount) {
		this.runningCount = runningCount;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Durée moyenne d'exécution'.
	 * @return Integer averageExecutionTime
	 */
	@Field(smartType = "STyONombre", label = "Durée moyenne d'exécution")
	public Integer getAverageExecutionTime() {
		return averageExecutionTime;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Durée moyenne d'exécution'.
	 * @param averageExecutionTime Integer
	 */
	public void setAverageExecutionTime(final Integer averageExecutionTime) {
		this.averageExecutionTime = averageExecutionTime;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Santé du processus'.
	 * @return String health <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOCodeIdentifiant", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Santé du processus")
	public String getHealth() {
		return health;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Santé du processus'.
	 * @param health String <b>Obligatoire</b>
	 */
	public void setHealth(final String health) {
		this.health = health;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
