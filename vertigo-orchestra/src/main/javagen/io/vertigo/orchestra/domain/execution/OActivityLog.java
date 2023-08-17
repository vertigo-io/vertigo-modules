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
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.datamodel.structure.stereotype.DataSpace("orchestra")
public final class OActivityLog implements Entity {
	private static final long serialVersionUID = 1L;

	private Long aclId;
	private String log;
	private String attachment;

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "AAclAce",
			fkFieldName = "aceId",
			primaryDtDefinitionName = "DtOActivityExecution",
			primaryIsNavigable = true,
			primaryRole = "ActivityExecution",
			primaryLabel = "ActivityExecution",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtOActivityLog",
			foreignIsNavigable = false,
			foreignRole = "ActivityLog",
			foreignLabel = "ActivityLog",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.execution.OActivityExecution> aceIdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.execution.OActivityExecution.class, "ActivityExecution");

	/** {@inheritDoc} */
	@Override
	public UID<OActivityLog> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id du log'.
	 * @return Long aclId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOIdentifiant", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id du log")
	public Long getAclId() {
		return aclId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id du log'.
	 * @param aclId Long <b>Obligatoire</b>
	 */
	public void setAclId(final Long aclId) {
		this.aclId = aclId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Contenu du log'.
	 * @return String log
	 */
	@Field(smartType = "STyOText", label = "Contenu du log")
	public String getLog() {
		return log;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Contenu du log'.
	 * @param log String
	 */
	public void setLog(final String log) {
		this.log = log;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Fichier joint'.
	 * @return String attachment
	 */
	@Field(smartType = "STyOText", label = "Fichier joint")
	public String getAttachment() {
		return attachment;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Fichier joint'.
	 * @param attachment String
	 */
	public void setAttachment(final String attachment) {
		this.attachment = attachment;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'ActivityExecution'.
	 * @return Long aceId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyOIdentifiant", label = "ActivityExecution", fkDefinition = "DtOActivityExecution", cardinality = io.vertigo.core.lang.Cardinality.ONE )
	public Long getAceId() {
		return (Long) aceIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'ActivityExecution'.
	 * @param aceId Long <b>Obligatoire</b>
	 */
	public void setAceId(final Long aceId) {
		aceIdAccessor.setId(aceId);
	}

 	/**
	 * Association : ActivityExecution.
	 * @return l'accesseur vers la propriété 'ActivityExecution'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.execution.OActivityExecution> activityExecution() {
		return aceIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
