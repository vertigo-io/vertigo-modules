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
package io.vertigo.orchestra.domain.execution;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datamodel.data.stereotype.Field;
import io.vertigo.datamodel.data.util.DtObjectUtil;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.datamodel.data.stereotype.DataSpace("orchestra")
public final class OProcessExecution implements Entity {
	private static final long serialVersionUID = 1L;

	private Long preId;
	private java.time.Instant beginTime;
	private java.time.Instant endTime;
	private String engine;
	private Boolean checked;
	private java.time.Instant checkingDate;
	private String checkingComment;

	@io.vertigo.datamodel.data.stereotype.Association(
			name = "APrePro",
			fkFieldName = "proId",
			primaryDtDefinitionName = "DtOProcess",
			primaryIsNavigable = true,
			primaryRole = "Process",
			primaryLabel = "Processus",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtOProcessExecution",
			foreignIsNavigable = false,
			foreignRole = "ExecutionProcessus",
			foreignLabel = "ExecutionProcessus",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.definition.OProcess> proIdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.definition.OProcess.class, "Process");

	@io.vertigo.datamodel.data.stereotype.Association(
			name = "APreEst",
			fkFieldName = "estCd",
			primaryDtDefinitionName = "DtOExecutionState",
			primaryIsNavigable = true,
			primaryRole = "ExecutionState",
			primaryLabel = "ExecutionState",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DtOProcessExecution",
			foreignIsNavigable = false,
			foreignRole = "ExecutionProcess",
			foreignLabel = "ExecutionProcessus",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.referential.OExecutionState> estCdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.referential.OExecutionState.class, "ExecutionState");

	@io.vertigo.datamodel.data.stereotype.Association(
			name = "APreUsr",
			fkFieldName = "usrId",
			primaryDtDefinitionName = "DtOUser",
			primaryIsNavigable = true,
			primaryRole = "User",
			primaryLabel = "User",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DtOProcessExecution",
			foreignIsNavigable = false,
			foreignRole = "ExecutionProcess",
			foreignLabel = "ExecutionProcessus",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.referential.OUser> usrIdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.referential.OUser.class, "User");

	/** {@inheritDoc} */
	@Override
	public UID<OProcessExecution> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id de l'execution d'un processus'.
	 * @return Long preId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOIdentifiant", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id de l'execution d'un processus")
	public Long getPreId() {
		return preId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id de l'execution d'un processus'.
	 * @param preId Long <b>Obligatoire</b>
	 */
	public void setPreId(final Long preId) {
		this.preId = preId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de début'.
	 * @return Instant beginTime <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOTimestamp", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Date de début")
	public java.time.Instant getBeginTime() {
		return beginTime;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de début'.
	 * @param beginTime Instant <b>Obligatoire</b>
	 */
	public void setBeginTime(final java.time.Instant beginTime) {
		this.beginTime = beginTime;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de fin'.
	 * @return Instant endTime
	 */
	@Field(smartType = "STyOTimestamp", label = "Date de fin")
	public java.time.Instant getEndTime() {
		return endTime;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de fin'.
	 * @param endTime Instant
	 */
	public void setEndTime(final java.time.Instant endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Implémentation effective de l'execution'.
	 * @return String engine
	 */
	@Field(smartType = "STyOClasse", label = "Implémentation effective de l'execution")
	public String getEngine() {
		return engine;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Implémentation effective de l'execution'.
	 * @param engine String
	 */
	public void setEngine(final String engine) {
		this.engine = engine;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Pris en charge'.
	 * @return Boolean checked
	 */
	@Field(smartType = "STyOBooleen", label = "Pris en charge")
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Pris en charge'.
	 * @param checked Boolean
	 */
	public void setChecked(final Boolean checked) {
		this.checked = checked;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de prise en charge'.
	 * @return Instant checkingDate
	 */
	@Field(smartType = "STyOTimestamp", label = "Date de prise en charge")
	public java.time.Instant getCheckingDate() {
		return checkingDate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de prise en charge'.
	 * @param checkingDate Instant
	 */
	public void setCheckingDate(final java.time.Instant checkingDate) {
		this.checkingDate = checkingDate;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Commentaire'.
	 * @return String checkingComment
	 */
	@Field(smartType = "STyOText", label = "Commentaire")
	public String getCheckingComment() {
		return checkingComment;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Commentaire'.
	 * @param checkingComment String
	 */
	public void setCheckingComment(final String checkingComment) {
		this.checkingComment = checkingComment;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Processus'.
	 * @return Long proId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.data.stereotype.ForeignKey(smartType = "STyOIdentifiant", label = "Processus", fkDefinition = "DtOProcess", cardinality = io.vertigo.core.lang.Cardinality.ONE )
	public Long getProId() {
		return (Long) proIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Processus'.
	 * @param proId Long <b>Obligatoire</b>
	 */
	public void setProId(final Long proId) {
		proIdAccessor.setId(proId);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'ExecutionState'.
	 * @return String estCd
	 */
	@io.vertigo.datamodel.data.stereotype.ForeignKey(smartType = "STyOCodeIdentifiant", label = "ExecutionState", fkDefinition = "DtOExecutionState" )
	public String getEstCd() {
		return (String) estCdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'ExecutionState'.
	 * @param estCd String
	 */
	public void setEstCd(final String estCd) {
		estCdAccessor.setId(estCd);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'User'.
	 * @return Long usrId
	 */
	@io.vertigo.datamodel.data.stereotype.ForeignKey(smartType = "STyOIdentifiant", label = "User", fkDefinition = "DtOUser" )
	public Long getUsrId() {
		return (Long) usrIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'User'.
	 * @param usrId Long
	 */
	public void setUsrId(final Long usrId) {
		usrIdAccessor.setId(usrId);
	}

 	/**
	 * Association : ExecutionState.
	 * @return l'accesseur vers la propriété 'ExecutionState'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.referential.OExecutionState> executionState() {
		return estCdAccessor;
	}

 	/**
	 * Association : Processus.
	 * @return l'accesseur vers la propriété 'Processus'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.definition.OProcess> process() {
		return proIdAccessor;
	}

 	/**
	 * Association : User.
	 * @return l'accesseur vers la propriété 'User'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.referential.OUser> user() {
		return usrIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
