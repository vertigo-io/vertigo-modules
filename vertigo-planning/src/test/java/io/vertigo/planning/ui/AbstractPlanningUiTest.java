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
package io.vertigo.planning.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;

import io.vertigo.planning.demo.PlanningDemoData;
import io.vertigo.planning.demo.PlanningDemoServer;
import io.vertigo.planning.demo.boot.PlanningDemoJdbcParamPlugin;

/**
 * UI tests of the planning components on the demo (cabinet médical, its read only planning, laboratoire, interviews in tranches horaires), in Chromium.
 * Run on demand, as the other UI tests of Vertigo : enabled by PLANNING_UI_TEST_JDBC_URL (env or system property), the PostgreSQL of the demo.
 * The demo starts once for the run, Playwright downloads its Chromium on the first run.
 * Every test fails on a javascript error, a console error or an http error (except 422, an input refused).
 * The tests changing data use their own day and hours, so that they don't depend on each other.
 */
abstract class AbstractPlanningUiTest {
	protected static final String MEDECIN = "/cabinet/medecin/";
	protected static final String CABINET = "/cabinet/";
	protected static final String LABORATOIRE = "/laboratoire/";
	/** The cabinet, read only. */
	protected static final String SECRETARIAT = "/cabinet/secretariat/";
	/** The interviews, in mode tranches horaires. */
	protected static final String ENTRETIENS = "/entretiens/";
	protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private static Playwright playwright;
	private static Browser browser;

	private BrowserContext context;
	protected Page page;
	private final List<String> errors = new ArrayList<>();

	/**
	 * @return true if the PostgreSQL of the demo is given (env or system property PLANNING_UI_TEST_JDBC_URL) : the tests are run
	 */
	static boolean hasDatabase() {
		return PlanningDemoJdbcParamPlugin.hasJdbcUrl();
	}

	@BeforeAll
	static void startDemo() throws Exception {
		// the browser first : the demo recreates its schema at start
		playwright = Playwright.create();
		browser = playwright.chromium().launch();
		PlaywrightAssertions.setDefaultAssertionTimeout(10_000);
		PlanningDemoServer.startOnce();
	}

	/**
	 * Starts the demo to navigate on it by hand (Run As > Java Application), until stopped : http://localhost:18090/demo/cabinet/
	 * The data is created again at each start, on the next week.
	 * @param args none
	 */
	public static void main(final String[] args) throws Exception {
		// the local PostgreSQL of the UI tests (docker run -d --name vertigo-planning-uitest-pg -p 55432:5432 -e POSTGRES_PASSWORD=postgres postgres:14)
		System.setProperty("PLANNING_UI_TEST_JDBC_URL", "jdbc:postgresql://localhost:55432/postgres?user=postgres&password=postgres");
		PlanningDemoServer.main(args);
	}

	@AfterAll
	static void closeBrowser() {
		if (playwright != null) { // null when the browser didn't start
			playwright.close();
			playwright = null;
		}
	}

	@BeforeEach
	void openPage() {
		context = browser.newContext(new Browser.NewContextOptions().setLocale("fr-FR").setViewportSize(1600, 1000));
		context.setDefaultTimeout(10_000);
		page = context.newPage();
		page.onPageError(error -> errors.add("javascript error : " + error));
		page.onConsoleMessage(message -> {
			// a failed resource is checked on the responses (a refused input answers 422, logged by the browser)
			if ("error".equals(message.type()) && !message.text().startsWith("Failed to load resource")) {
				errors.add("console error : " + message.text());
			}
		});
		page.onResponse(response -> {
			// 422 : an input refused by the server, shown to the user
			if (response.status() >= 400 && response.status() != 422) {
				errors.add("http error " + response.status() + " : " + response.url());
			}
		});
	}

	@AfterEach
	void checkNoError() {
		if (context != null) { // null when openPage failed : its error is the one to read
			context.close();
		}
		Assertions.assertTrue(errors.isEmpty(), () -> String.join("\n", errors));
	}

	/**
	 * Opens a page of the demo and waits for the calendar.
	 * @param path the page path (MEDECIN, CABINET, LABORATOIRE, SECRETARIAT, ENTRETIENS)
	 */
	protected void open(final String path) {
		final var response = page.navigate(PlanningDemoServer.baseUrl() + path);
		Assertions.assertEquals(200, response.status(), path);
		assertThat(page.locator(".q-calendar-day")).isVisible();
		// no raw message key (??key_locale??) in the html rendered by the server (attributes and dialogs not opened yet included) :
		// every message is found, in the bundles of the components or of the demo
		final Matcher rawKey = Pattern.compile("\\?\\?[\\w.-]+\\?\\?").matcher(response.text());
		Assertions.assertFalse(rawKey.find(), () -> path + " holds a raw message key : " + rawKey.group());
	}

	protected Locator button(final String name) {
		return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name).setExact(true));
	}

	protected Locator button(final Pattern name) {
		return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name));
	}

	/**
	 * @param dayIndex 0 for monday of the demo week
	 * @return the column of this day in the calendar
	 */
	protected Locator day(final int dayIndex) {
		return page.locator(".q-calendar-day__day").nth(dayIndex);
	}

	/**
	 * @param dayIndex 0 for monday of the demo week
	 * @param hours the hours shown on the plage, as 09:00 - 12:00
	 * @return the plage (or the plage assembling several agendas) of this day
	 */
	protected Locator plage(final int dayIndex, final String hours) {
		return day(dayIndex).locator(".my-event, .my-events").filter(new Locator.FilterOptions().setHasText(hours));
	}

	protected static LocalDate demoDay(final int dayIndex) {
		return PlanningDemoData.demoWeekMonday().plusDays(dayIndex);
	}

	/**
	 * @return the dialogs opened, but the menu of a plage assembling several agendas (a dialog too)
	 */
	protected Locator dialog() {
		return page.locator("[role=dialog]:not(.q-menu)");
	}

	protected void assertNotified(final String message) {
		assertThat(page.locator(".q-notification").filter(new Locator.FilterOptions().setHasText(message))).isVisible();
	}

	/**
	 * Lines of an accessibility tree for a button without accessible name.
	 * @param locator the part of the page
	 * @return the unnamed buttons, as written in the aria snapshot
	 */
	protected static List<String> unnamedButtons(final Locator locator) {
		return locator.ariaSnapshot().lines()
				.filter(line -> line.trim().matches("- button( \\[[^\\]]+\\])*:?")) // no name, maybe states as [disabled]
				.toList();
	}
}
