/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2026, Vertigo.io, team@vertigo.io
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
package io.vertigo.planning.demo.boot;

import java.util.Optional;

import io.vertigo.core.impl.param.ParamPlugin;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.param.Param;

/**
 * Gives the jdbc url of the demo : a PostgreSQL given by PLANNING_UI_TEST_JDBC_URL (env, else system property ; its own name : the
 * database tests read PG_JDBC_URL), with the dedicated schema of the
 * demo, recreated at each start. No default : the schema is dropped, on a PostgreSQL chosen on purpose.
 * The planning tasks use PostgreSQL syntax (interval, generate_series...) : no in memory database.
 */
public final class PlanningDemoJdbcParamPlugin implements ParamPlugin {
	public static final String SCHEMA = "planning_ui_test";
	private static final String JDBC_URL_PARAM_NAME = "PLANNING_DEMO_JDBC_URL";
	private static final String JDBC_URL_VARIABLE = "PLANNING_UI_TEST_JDBC_URL";

	/** {@inheritDoc} */
	@Override
	public Optional<Param> getParam(final String paramName) {
		Assertion.check().isNotBlank(paramName);
		//-----
		if (!JDBC_URL_PARAM_NAME.equals(paramName)) {
			return Optional.empty();
		}
		return Optional.of(Param.of(paramName, jdbcUrl()));
	}

	/**
	 * @return true if the PostgreSQL of the demo is given
	 */
	public static boolean hasJdbcUrl() {
		return pgJdbcUrl().isPresent();
	}

	/**
	 * @return the jdbc url of the demo database
	 */
	public static String jdbcUrl() {
		final String pgJdbcUrl = pgJdbcUrl().orElseThrow(() -> new IllegalStateException("No PostgreSQL for the planning demo : set " + JDBC_URL_VARIABLE + " (env or system property)"));
		// quoteReturningIdentifiers=false : the generated id column is given in upper case (AGE_ID), as the projects on PostgreSQL do
		return pgJdbcUrl + (pgJdbcUrl.contains("?") ? '&' : '?') + "currentSchema=" + SCHEMA + "&quoteReturningIdentifiers=false";
	}

	// the env, else the system property (set by the main of the UI tests)
	private static Optional<String> pgJdbcUrl() {
		return Optional.ofNullable(System.getenv(JDBC_URL_VARIABLE))
				.or(() -> Optional.ofNullable(System.getProperty(JDBC_URL_VARIABLE)))
				.filter(url -> !url.isBlank());
	}
}
