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
package io.vertigo.audit.impl.trace;

import io.vertigo.audit.trace.Trace;
import io.vertigo.audit.trace.TraceCriteria;
import io.vertigo.core.node.component.Plugin;
import io.vertigo.datamodel.data.model.DtList;

/**
 * This class defines the storage of audit trails.
 * @author xdurand
 */
public interface TraceStorePlugin extends Plugin {
	/**
	 * Gets an audit trail.
	 * @param auditTraceId the audit trail defined by its id.
	 * @return the
	 */
	Trace read(Long auditTraceId);

	/**
	 * Saves a new audit trail.
	 * Attention: The audit MUST NOT have an id.
	 * @param auditTrace the audit trail to save.
	 */
	void create(Trace auditTrace);

	/**
	 * Fetchs all Audit Trace mathing the provided criteria
	 * @param auditTraceCriteria
	 * @return the matching taces for the provided criteria
	 */
	DtList<Trace> findByCriteria(TraceCriteria auditTraceCriteria);

}
