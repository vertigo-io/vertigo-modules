/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.core.boot;

import io.vertigo.core.config.LogConfig;
import io.vertigo.core.engines.AopEngine;
import io.vertigo.core.engines.ElasticaEngine;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.JsonExclude;
import io.vertigo.lang.Option;

/*
 * This Class defines the properties of ComponentSpace and DefinitionSpace. 
 * That's to say : how to boot the modules of Vertigo.
 * @author pchretien
 */
public final class BootConfig {
	private final Option<LogConfig> logConfigOption;

	private final boolean silence;
	@JsonExclude
	private final AopEngine aopEngine;
	@JsonExclude
	private final Option<ElasticaEngine> elasticaEngine;

	BootConfig(
			final Option<LogConfig> logConfigOption,
			final AopEngine aopEngine,
			final Option<ElasticaEngine> elasticaEngine,
			final boolean silence) {
		Assertion.checkNotNull(logConfigOption);
		//---
		Assertion.checkNotNull(aopEngine);
		Assertion.checkNotNull(elasticaEngine);
		//-----
		this.logConfigOption = logConfigOption;
		//-----
		this.silence = silence;
		this.aopEngine = aopEngine;
		this.elasticaEngine = elasticaEngine;
	}

	public Option<LogConfig> getLogConfig() {
		return logConfigOption;
	}

	public boolean isSilence() {
		return silence;
	}

	public AopEngine getAopEngine() {
		return aopEngine;
	}

	public Option<ElasticaEngine> getElasticaEngine() {
		return elasticaEngine;
	}
}
