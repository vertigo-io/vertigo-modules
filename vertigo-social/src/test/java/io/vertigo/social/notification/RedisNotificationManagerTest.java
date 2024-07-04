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
package io.vertigo.social.notification;

import org.junit.jupiter.api.BeforeEach;

import io.vertigo.connectors.redis.RedisConnector;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.social.MyNodeConfig;
import redis.clients.jedis.UnifiedJedis;

public final class RedisNotificationManagerTest extends AbstractNotificationManagerTest {

	@Override
	protected NodeConfig buildNodeConfig() {
		return MyNodeConfig.config(true);
	}

	@BeforeEach
	public void cleanUp() {
		final RedisConnector redisConnector = getApp().getComponentSpace().resolve(RedisConnector.class);
		final UnifiedJedis jedis = redisConnector.getClient();
		jedis.flushAll();
	}
}
