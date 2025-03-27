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

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import io.vertigo.audit.trace.Trace;
import io.vertigo.audit.trace.TraceCriteria;
import io.vertigo.audit.trace.TraceManager;
import io.vertigo.core.lang.Assertion;
import io.vertigo.datamodel.data.model.DtList;

/**
 * @author xdurand
 */
public final class TraceManagerImpl implements TraceManager {

	private final List<TraceStorePlugin> auditTraceStorePlugins;
	private final Optional<TraceStorePlugin> readAuditTraceStorePluginOpt;

	/**
	 * Constructor.
	 * @param auditTraceStorePlugin
	 */
	@Inject
	public TraceManagerImpl(final List<TraceStorePlugin> auditTraceStorePlugin) {
		Assertion.check().isNotNull(auditTraceStorePlugin);
		//---
		auditTraceStorePlugins = auditTraceStorePlugin;
		readAuditTraceStorePluginOpt = auditTraceStorePlugins.stream()
				.filter(TraceStorePlugin::isReadSupported)
				.findFirst();
	}

	@Override
	public void addTrace(final Trace auditTrace) {
		auditTraceStorePlugins.stream()
				.forEachOrdered(auditTraceStorePlugin -> auditTraceStorePlugin.create(auditTrace));
	}

	@Override
	public DtList<Trace> findTrace(final TraceCriteria auditTraceCriteria) {
		Assertion.check().isNotNull(auditTraceCriteria);
		//---
		return readAuditTraceStorePluginOpt
				.orElseThrow(() -> new UnsupportedOperationException("No TraceStorePlugin supporting trace reading was found"))
				.findByCriteria(auditTraceCriteria);
	}

	@Override
	public Trace getTrace(final Long auditTraceId) {
		Assertion.check().isNotNull(auditTraceId);
		//---
		return readAuditTraceStorePluginOpt
				.orElseThrow(() -> new UnsupportedOperationException("No TraceStorePlugin supporting trace reading was found"))
				.read(auditTraceId);
	}

}
