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
package io.vertigo.audit.plugins.trace.log4j;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertigo.audit.impl.trace.TraceStorePlugin;
import io.vertigo.audit.trace.Trace;
import io.vertigo.audit.trace.TraceCriteria;
import io.vertigo.core.lang.Assertion;
import io.vertigo.datamodel.data.model.DtList;

/**
 * @author npiedeloup
 */
public final class LogTraceStorePlugin implements TraceStorePlugin {
	//execDate, category, username, itemUrn, message, businessDate, context, id
	private static final String AUDIT_PATTERN = "id:\"{}\", executionDate:\"{}\", category:\"{}\", username:\"{}\", itemUrn:\"{}\", message:\"{}\", businessDate:\"{}\", context:\"{}\"";
	private static final Logger AUDIT_LOGGER = LogManager.getLogger("audit");
	private final AtomicLong logSequenceGenerator = new AtomicLong(0);

	@Override
	public Trace read(final Long idAuditTrace) {
		throw new UnsupportedOperationException("LogTraceStorePlugin don't support trace read");
	}

	@Override
	public void create(final Trace auditTrace) {
		Assertion.check()
				.isNotNull(auditTrace)
				.isNull(auditTrace.getTraId(), "A new audit trail must not have an id");
		//---
		auditTrace.setTraId(logSequenceGenerator.addAndGet(1));
		AUDIT_LOGGER.info(AUDIT_PATTERN, auditTrace.getTraId(), auditTrace.getExecutionDate(), auditTrace.getCategory(), auditTrace.getUsername(),
				auditTrace.getItemUrn(), auditTrace.getMessage(), auditTrace.getBusinessDate(), auditTrace.getContext());
	}

	@Override
	public DtList<Trace> findByCriteria(final TraceCriteria auditTraceCriteria) {
		throw new UnsupportedOperationException("LogTraceStorePlugin don't support trace read");
	}
}
