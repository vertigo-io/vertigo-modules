/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
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
package io.vertigo.workflow.domain.model;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.ListVAccessor;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.model.VAccessor;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class WfWorkflowDefinition implements Entity {
	private static final long serialVersionUID = 1L;

	private Long wfwdId;
	private String name;
	private java.util.Date date;

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_WFWD_WFAD",
			fkFieldName = "WFAD_ID",
			primaryDtDefinitionName = "DT_WF_ACTIVITY_DEFINITION",
			primaryIsNavigable = true,
			primaryRole = "StartActivity",
			primaryLabel = "startActivity",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DT_WF_WORKFLOW_DEFINITION",
			foreignIsNavigable = false,
			foreignRole = "WfWorkflowDefinition",
			foreignLabel = "WfWorkflowDefinition",
			foreignMultiplicity = "0..*")
	private final VAccessor<io.vertigo.workflow.domain.model.WfActivityDefinition> wfadIdAccessor = new VAccessor<>(io.vertigo.workflow.domain.model.WfActivityDefinition.class, "StartActivity");

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_WFWD_WFTD",
			fkFieldName = "WFWD_ID",
			primaryDtDefinitionName = "DT_WF_WORKFLOW_DEFINITION",
			primaryIsNavigable = false,
			primaryRole = "WfWorkflowDefinition",
			primaryLabel = "WfWorkflowDefinition",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DT_WF_TRANSITION_DEFINITION",
			foreignIsNavigable = true,
			foreignRole = "WfTransitionDefinition",
			foreignLabel = "WfTransitionDefinition",
			foreignMultiplicity = "0..*")
	private final ListVAccessor<io.vertigo.workflow.domain.model.WfTransitionDefinition> wfTransitionDefinitionAccessor = new ListVAccessor<>(this, "A_WFWD_WFTD", "WfTransitionDefinition");

	/** {@inheritDoc} */
	@Override
	public UID<WfWorkflowDefinition> getUID() {
		return UID.of(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id Workflow definition'.
	 * @return Long wfwdId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_WF_ID", type = "ID", required = true, label = "Id Workflow definition")
	public Long getWfwdId() {
		return wfwdId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id Workflow definition'.
	 * @param wfwdId Long <b>Obligatoire</b>
	 */
	public void setWfwdId(final Long wfwdId) {
		this.wfwdId = wfwdId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'name'.
	 * @return String name
	 */
	@Field(domain = "DO_WF_LABEL", label = "name")
	public String getName() {
		return name;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'name'.
	 * @param name String
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'date'.
	 * @return Date date
	 */
	@Field(domain = "DO_WF_DATE", label = "date")
	public java.util.Date getDate() {
		return date;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'date'.
	 * @param date Date
	 */
	public void setDate(final java.util.Date date) {
		this.date = date;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'startActivity'.
	 * @return Long wfadId
	 */
	@Field(domain = "DO_WF_ID", type = "FOREIGN_KEY", label = "startActivity")
	public Long getWfadId() {
		return (Long) wfadIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'startActivity'.
	 * @param wfadId Long
	 */
	public void setWfadId(final Long wfadId) {
		wfadIdAccessor.setId(wfadId);
	}

	/**
	 * Association : startActivity.
	 * @return l'accesseur vers la propriété 'startActivity'
	 */
	public VAccessor<io.vertigo.workflow.domain.model.WfActivityDefinition> startActivity() {
		return wfadIdAccessor;
	}

	@Deprecated
	public io.vertigo.workflow.domain.model.WfActivityDefinition getStartActivity() {
		// we keep the lazyness
		if (!wfadIdAccessor.isLoaded()) {
			wfadIdAccessor.load();
		}
		return wfadIdAccessor.get();
	}

	/**
	 * Retourne l'URI: startActivity.
	 * @return URI de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.UID<io.vertigo.workflow.domain.model.WfActivityDefinition> getStartActivityURI() {
		return wfadIdAccessor.getUID();
	}

	/**
	 * Association : WfTransitionDefinition.
	 * @return l'accesseur vers la propriété 'WfTransitionDefinition'
	 */
	public ListVAccessor<io.vertigo.workflow.domain.model.WfTransitionDefinition> wfTransitionDefinition() {
		return wfTransitionDefinitionAccessor;
	}

	/**
	 * Association : WfTransitionDefinition.
	 * @return DtList de io.vertigo.workflow.domain.model.WfTransitionDefinition
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.workflow.domain.model.WfTransitionDefinition> getWfTransitionDefinitionList() {
		// we keep the lazyness
		if (!wfTransitionDefinitionAccessor.isLoaded()) {
			wfTransitionDefinitionAccessor.load();
		}
		return wfTransitionDefinitionAccessor.get();
	}

	/**
	 * Association URI: WfTransitionDefinition.
	 * @return URI de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.metamodel.association.DtListURIForSimpleAssociation getWfTransitionDefinitionDtListURI() {
		return (io.vertigo.dynamo.domain.metamodel.association.DtListURIForSimpleAssociation) wfTransitionDefinitionAccessor.getDtListURI();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
