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
package io.vertigo.planning.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.vertigo.planning.demo.boot.PlanningDemoJdbcParamPlugin;
import io.vertigo.ui.boot.JettyBoot;
import io.vertigo.ui.boot.JettyBootParams;

/**
 * The planning demo on an embedded Jetty, used by the UI tests ; run main() to try the pages by hand, then http://localhost:18090/demo/cabinet/
 * It needs a PostgreSQL : PLANNING_UI_TEST_JDBC_URL (env or system property, set by the main of the UI tests), the demo works in its own schema,
 * dropped and created again at each start : two runs on the same PostgreSQL at the same time would disturb each other.
 */
public final class PlanningDemoServer {
	private static final int MAIN_PORT = 18090;
	private static final String CREATE_SCRIPT = "io/vertigo/planning/sql/planning_create_init_4.5.0.sql";

	private static int port;

	private PlanningDemoServer() {
		//private
	}

	/**
	 * @return the url of the demo started by startOnce
	 */
	public static String baseUrl() {
		return "http://localhost:" + port + "/demo";
	}

	/**
	 * The demo can't start without its database : the main checks it first.
	 * @return why the PostgreSQL of the demo doesn't answer, empty if it answers
	 */
	public static Optional<String> databaseUnavailableReason() {
		try (Connection connection = openConnection()) {
			return connection.isValid(3) ? Optional.empty() : Optional.of("connection not valid");
		} catch (final SQLException e) {
			return Optional.of(e.getMessage());
		}
	}

	/**
	 * Starts the demo once for all the tests of the run, on a free port ; stopped with the JVM.
	 * The schema is created before the webapp : a failed start of JettyBoot stops the JVM, a SQL error here is a test error.
	 */
	public static synchronized void startOnce() throws Exception {
		if (port == 0) {
			createSchema();
			try (ServerSocket serverSocket = new ServerSocket(0)) {
				port = serverSocket.getLocalPort();
			}
			start(false);
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					JettyBoot.stop();
				} catch (final Exception e) {
					// nothing to do : the JVM stops
				}
			}));
		}
	}

	private static Connection openConnection() throws SQLException {
		return DriverManager.getConnection(PlanningDemoJdbcParamPlugin.jdbcUrl() + "&connectTimeout=3&loginTimeout=3");
	}

	/**
	 * Creates the schema of the demo with the real creation script of the module (the data is created by the services at startup).
	 * The script is split on ';' : enough for this script, which has no function body.
	 */
	private static void createSchema() throws SQLException, IOException {
		final String script;
		try (InputStream in = PlanningDemoServer.class.getClassLoader().getResourceAsStream(CREATE_SCRIPT)) {
			script = new String(in.readAllBytes(), StandardCharsets.UTF_8).lines()
					.filter(line -> !line.trim().startsWith("--"))
					.collect(Collectors.joining("\n"));
		}
		try (Connection connection = openConnection(); Statement statement = connection.createStatement()) {
			statement.execute("drop schema if exists " + PlanningDemoJdbcParamPlugin.SCHEMA + " cascade");
			statement.execute("create schema " + PlanningDemoJdbcParamPlugin.SCHEMA);
			// the script drops its tables without schema : only in the schema of the demo (a search_path given in the jdbc url could change it)
			try (var resultSet = statement.executeQuery("select current_schema()")) {
				resultSet.next();
				if (!PlanningDemoJdbcParamPlugin.SCHEMA.equals(resultSet.getString(1))) {
					throw new IllegalStateException("The planning demo works in the schema " + PlanningDemoJdbcParamPlugin.SCHEMA + ", not in " + resultSet.getString(1));
				}
			}
			for (final String sql : script.split(";")) {
				if (!sql.isBlank()) {
					statement.execute(sql);
				}
			}
		}
	}

	private static void start(final boolean join) throws Exception {
		final var jettyBootParamsBuilder = JettyBootParams.builder("testWebApp/", PlanningDemoWebApplicationInitializer.class)
				.withContextPath("/demo")
				.noSsl()
				.withPort(port);
		if (!join) {
			jettyBootParamsBuilder.noJoin();
		}
		JettyBoot.startServer(jettyBootParamsBuilder.build(), List::of); // the webapp context is the only handler
	}

	/**
	 * Starts the demo to try the pages by hand (the main of the UI tests gives PLANNING_UI_TEST_JDBC_URL),
	 * until the process is stopped. The data is created again at each start, on the next week.
	 * @param args none
	 */
	public static void main(final String[] args) throws Exception {
		final var databaseUnavailableReason = databaseUnavailableReason();
		if (databaseUnavailableReason.isPresent()) {
			System.err.println("No PostgreSQL for the planning demo, set PLANNING_UI_TEST_JDBC_URL : " + databaseUnavailableReason.get());
			return;
		}
		createSchema();
		port = MAIN_PORT;
		System.out.println("Planning demo, the pages :");
		for (final String page : List.of("cabinet/medecin/", "cabinet/", "cabinet/secretariat/", "laboratoire/", "entretiens/")) {
			System.out.println("  " + baseUrl() + "/" + page);
		}
		start(true);
	}
}
