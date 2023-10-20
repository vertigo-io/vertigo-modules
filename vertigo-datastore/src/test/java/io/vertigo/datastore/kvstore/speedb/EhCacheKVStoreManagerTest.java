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
package io.vertigo.datastore.kvstore.speedb;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.vertigo.commons.CommonsFeatures;
import io.vertigo.core.node.config.BootConfig;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.param.Param;
import io.vertigo.core.plugins.analytics.log.SocketLoggerAnalyticsConnectorPlugin;
import io.vertigo.core.plugins.resource.classpath.ClassPathResourceResolverPlugin;
import io.vertigo.datastore.DataStoreFeatures;
import io.vertigo.datastore.kvstore.AbstractKVStoreManagerTest;

/**
 * @author npiedeloup
 */
public final class EhCacheKVStoreManagerTest extends AbstractKVStoreManagerTest {

	@Override
	protected NodeConfig buildNodeConfig() {
		return NodeConfig.builder()
				.withBoot(BootConfig.builder()
						.addPlugin(ClassPathResourceResolverPlugin.class)
						.addAnalyticsConnectorPlugin(SocketLoggerAnalyticsConnectorPlugin.class)
						.build())
				.addModule(new CommonsFeatures()
						.build())
				.addModule(new DataStoreFeatures()
						.withCache()
						.withMemoryCache()
						.withKVStore()
						.withEhCacheKV(
								Param.of("collections", "flowers;TTL=" + TTL + ", trees;inMemory"),
								Param.of("dbFilePath", storagePath))
						.build())
				.build();
	}

	@Override
	@Disabled
	@Test
	public void testRemoveFail() {
		//cant detect not found key
	}

	@Override
	@Disabled
	@Test
	public void testTimeToLive() {
		//need daemon : get can return expired key @see https://github.com/facebook/rocksdb/wiki/Time-to-Live
	}
}
