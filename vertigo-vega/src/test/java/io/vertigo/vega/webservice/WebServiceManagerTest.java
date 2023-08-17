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
package io.vertigo.vega.webservice;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;
import io.vertigo.core.node.AutoCloseableNode;
import io.vertigo.vega.webservice.data.MyNodeConfig;

public final class WebServiceManagerTest extends AbstractWebServiceManagerTest {

	private static AutoCloseableNode node;

	static {
		//RestAsssured init
		RestAssured.port = MyNodeConfig.WS_PORT;
	}

	@BeforeAll
	public static void setUp() {
		node = new AutoCloseableNode(MyNodeConfig.config(true));
	}

	@AfterAll
	public static void tearDown() {
		if (node != null) {
			node.close();
		}
	}
}
