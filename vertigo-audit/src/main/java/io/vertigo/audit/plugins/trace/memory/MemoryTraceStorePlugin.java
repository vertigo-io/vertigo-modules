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
package io.vertigo.audit.plugins.trace.memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import io.vertigo.audit.impl.trace.TraceStorePlugin;
import io.vertigo.audit.trace.Trace;
import io.vertigo.audit.trace.TraceCriteria;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.data.model.DtList;

/**
 *
 * @author xdurand *
 */
public final class MemoryTraceStorePlugin implements TraceStorePlugin {
	private final Map<Long, Trace> inMemoryStore = new ConcurrentHashMap<>();
	private final AtomicLong memorySequenceGenerator = new AtomicLong(0);

	@Override
	public Trace read(final Long idAuditTrace) {
		return inMemoryStore.get(idAuditTrace);
	}

	@Override
	public void create(final Trace auditTrace) {
		Assertion.check()
				.isNotNull(auditTrace)
				.isNull(auditTrace.getTraId(), "A new audit trail must not have an id");
		//---
		final long generatedId = memorySequenceGenerator.addAndGet(1);
		auditTrace.setTraId(generatedId);
		inMemoryStore.put(generatedId, auditTrace);
	}

	@Override
	public DtList<Trace> findByCriteria(final TraceCriteria auditTraceCriteria) {
		final DtList<Trace> auditTracesBuilder = new DtList<>(Trace.class);

		for (final Trace auditTrace : inMemoryStore.values()) {
			final boolean categoryMatched = matchCategory(auditTraceCriteria, auditTrace);
			final boolean userMatched = matchUser(auditTraceCriteria, auditTrace);
			final boolean businessDateMatched = matchBusinessDate(auditTraceCriteria, auditTrace);
			final boolean executionDateMatched = matchExecutionDate(auditTraceCriteria, auditTrace);
			final boolean itemMatched = matchItem(auditTraceCriteria, auditTrace);
			if (categoryMatched && userMatched && businessDateMatched && executionDateMatched && itemMatched) {
				auditTracesBuilder.add(auditTrace);
			}
		}

		return auditTracesBuilder;
	}

	private static boolean matchItem(final TraceCriteria auditTraceCriteria, final Trace auditTrace) {
		return auditTraceCriteria.getItemUrn() == null
				|| auditTraceCriteria.getItemUrn().equals(auditTrace.getItemUrn());
	}

	private static boolean matchExecutionDate(final TraceCriteria auditTraceCriteria, final Trace auditTrace) {
		return auditTraceCriteria.getStartExecutionDate() == null && auditTraceCriteria.getEndExecutionDate() == null
				|| auditTrace.getExecutionDate() != null
						&& (auditTraceCriteria.getStartExecutionDate() == null || auditTraceCriteria.getStartExecutionDate().isBefore(auditTrace.getExecutionDate()))
						&& (auditTraceCriteria.getEndExecutionDate() == null || auditTraceCriteria.getEndExecutionDate().isAfter(auditTrace.getExecutionDate()));
	}

	private static boolean matchBusinessDate(final TraceCriteria auditTraceCriteria, final Trace auditTrace) {
		return auditTraceCriteria.getStartBusinessDate() == null && auditTraceCriteria.getEndBusinessDate() == null
				|| auditTrace.getBusinessDate() != null
						&& (auditTraceCriteria.getStartBusinessDate() == null || auditTraceCriteria.getStartBusinessDate().isBefore(auditTrace.getBusinessDate()))
						&& (auditTraceCriteria.getEndBusinessDate() == null || auditTraceCriteria.getEndBusinessDate().isAfter(auditTrace.getBusinessDate()));
	}

	private static boolean matchUser(final TraceCriteria auditTraceCriteria, final Trace auditTrace) {
		return StringUtil.isBlank(auditTraceCriteria.getUsername())
				|| auditTraceCriteria.getUsername().equals(auditTrace.getUsername());
	}

	private static boolean matchCategory(final TraceCriteria auditTraceCriteria, final Trace auditTrace) {
		return StringUtil.isBlank(auditTraceCriteria.getCategory())
				|| auditTraceCriteria.getCategory().equals(auditTrace.getCategory());
	}
}
