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
package io.vertigo.database.timeseries;

import java.util.Map;

import io.vertigo.lang.Assertion;

/*
 * Couple(date, metriques)
 * @author pchretien, npiedeloup, mlaroche
 */
public final class TimedDataSerie {
	private final Long time;
	private final Map<String, Object> values;

	public TimedDataSerie(final Long time, final Map<String, Object> values) {
		Assertion.checkNotNull(values);
		//---
		this.time = time;
		this.values = values;
	}

	public Long getTime() {
		return time;
	}

	public Map<String, Object> getValues() {
		return values;
	}
}
