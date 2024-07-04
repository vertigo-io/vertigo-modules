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
package io.vertigo.audit;

import io.vertigo.audit.impl.ledger.LedgerManagerImpl;
import io.vertigo.audit.impl.trace.AuditTraceDtDefinitions;
import io.vertigo.audit.impl.trace.AuditTraceSmartTypes;
import io.vertigo.audit.impl.trace.TraceManagerImpl;
import io.vertigo.audit.ledger.LedgerManager;
import io.vertigo.audit.plugins.ledger.ethereum.EthereumLedgerPlugin;
import io.vertigo.audit.plugins.ledger.fake.FakeLedgerPlugin;
import io.vertigo.audit.plugins.trace.datastore.StoreTraceStorePlugin;
import io.vertigo.audit.plugins.trace.log4j.LogTraceStorePlugin;
import io.vertigo.audit.plugins.trace.memory.MemoryTraceStorePlugin;
import io.vertigo.audit.trace.TraceManager;
import io.vertigo.core.node.config.DefinitionProviderConfig;
import io.vertigo.core.node.config.Feature;
import io.vertigo.core.node.config.Features;
import io.vertigo.core.param.Param;
import io.vertigo.datamodel.impl.smarttype.ModelDefinitionProvider;

public class AuditFeatures extends Features<AuditFeatures> {

	public AuditFeatures() {
		super("vertigo-audit");
	}

	@Feature("trace")
	public AuditFeatures withTrace() {
		getModuleConfigBuilder()
				.addComponent(TraceManager.class, TraceManagerImpl.class)
				.addDefinitionProvider(DefinitionProviderConfig.builder(ModelDefinitionProvider.class)
						.addDefinitionResource("smarttypes", AuditTraceSmartTypes.class.getName())
						.addDefinitionResource("dtobjects", AuditTraceDtDefinitions.class.getName()).build());
		return this;
	}

	@Feature("trace.memory")
	public AuditFeatures withMemoryTrace() {
		getModuleConfigBuilder().addPlugin(MemoryTraceStorePlugin.class);
		return this;
	}

	/**
	 * Activate Trace with Store.
	 * @return these features
	 */
	@Feature("trace.store")
	public AuditFeatures withStoreTrace() {
		getModuleConfigBuilder()
				.addPlugin(StoreTraceStorePlugin.class);
		return this;
	}

	@Feature("trace.log")
	public AuditFeatures withLogTrace() {
		getModuleConfigBuilder().addPlugin(LogTraceStorePlugin.class);
		return this;
	}

	@Feature("ledger")
	public AuditFeatures withLedger() {
		getModuleConfigBuilder()
				.addComponent(LedgerManager.class, LedgerManagerImpl.class);
		return this;
	}

	/**
	 * Add Ethereum BlockChain Ledger
	 * @return  the feature
	 */
	@Feature("ledger.ethereum")
	public AuditFeatures withEthereumBlockChain(final Param... params) {
		getModuleConfigBuilder()
				.addPlugin(EthereumLedgerPlugin.class, params);
		return this;
	}

	/**
	 * Add Ethereum BlockChain Ledger
	 * @return  the feature
	 */
	@Feature("ledger.fake")
	public AuditFeatures withFakeBlockChain() {
		getModuleConfigBuilder()
				.addPlugin(FakeLedgerPlugin.class);
		return this;
	}

	@Override
	protected void buildFeatures() {
		//
	}

}
