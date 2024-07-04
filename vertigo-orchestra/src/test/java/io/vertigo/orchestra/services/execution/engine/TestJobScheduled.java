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
package io.vertigo.orchestra.services.execution.engine;

import io.vertigo.orchestra.services.execution.RunnableActivityEngine;

public final class TestJobScheduled extends RunnableActivityEngine {
	private static int count = 0;

	@Override
	public void run() {
		try {
			//On simule une attente qui correspond à un traitement métier de 100 ms
			Thread.sleep(100);
		} catch (final InterruptedException e) {
			Thread.currentThread().interrupt(); //si interrupt on relance
		}
		incCount();
	}

	private static synchronized void incCount() {
		count++;
	}

	public static synchronized int getCount() {
		return count;
	}

	public static synchronized void reset() {
		count = 0;
	}
}
