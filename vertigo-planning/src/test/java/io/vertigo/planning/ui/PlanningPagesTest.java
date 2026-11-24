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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Rendering and accessibility of vu:agenda-week, without user action : the 3 pages, the plages assembling several agendas
 * (2, 3 and 4 agendas), the counters, the accessible names.
 */
@EnabledIf("io.vertigo.planning.ui.AbstractPlanningUiTest#hasDatabase") // run on demand, see AbstractPlanningUiTest
public final class PlanningPagesTest extends AbstractPlanningUiTest {
	/** A focusable or clickable element : none may contain another one (RGAA, WCAG 4.1.2). */
	private static final String INTERACTIVE = "button, a[href], input, select, textarea, [role=button], [role=link], [tabindex]:not([tabindex='-1'])";
	private static final List<String> ALL_PAGES = List.of(MEDECIN, CABINET, SECRETARIAT, LABORATOIRE, ENTRETIENS);

	@Test
	void pagesShowTheWeekOfTheDemo() {
		for (final String[] pathAndTitle : List.of(
				new String[] { MEDECIN, "Agenda du Dr Anne Martin" },
				new String[] { CABINET, "Planning du cabinet (édition)" },
				new String[] { SECRETARIAT, "Planning du cabinet (lecture seule)" },
				new String[] { LABORATOIRE, "Laboratoire, prises de sang" },
				new String[] { ENTRETIENS, "Entretiens de recrutement" })) {
			open(pathAndTitle[0]);
			assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(pathAndTitle[1]))).isVisible();
			assertThat(page.getByText("Semaine du " + demoDay(0).format(DATE_FORMAT) + " au " + demoDay(4).format(DATE_FORMAT))).isVisible();
			assertThat(page.locator(".q-calendar-day__day")).hasCount(5);
		}
	}

	@Test
	void cabinetAssemblesTheAgendasOfAPlage() {
		open(CABINET);
		// monday : 2 agendas, both shown
		assertThat(plage(0, "09:00 - 12:00").locator(button("Cette plage assemble 2 agendas"))).isVisible();
		assertThat(plage(0, "09:00 - 12:00").locator(button("Dr Anne Martin (DM)"))).isVisible();
		assertThat(plage(0, "09:00 - 12:00").locator(button("Dr Paul Bernard (DB)"))).isVisible();
		assertThat(plage(0, "09:00 - 12:00")).containsText("0/24");
		// tuesday : 3 agendas from 09:00 to 12:30, the 3 shown
		assertThat(plage(1, "09:00 - 12:30").locator(button("Cette plage assemble 3 agendas"))).isVisible();
		assertThat(plage(1, "09:00 - 12:30").locator(button("Dr Claire Petit (DP)"))).isVisible();
		// wednesday : 4 agendas, more than maxMultiEventShow (3) : 2 shown and the button to see the others
		assertThat(plage(2, "08:30 - 12:30").locator(button("Cette plage assemble 4 agendas"))).isVisible();
		assertThat(plage(2, "08:30 - 12:30").locator(button("Voir plus d'agenda"))).isVisible();
		assertThat(plage(2, "08:30 - 12:30").getByRole(AriaRole.BUTTON)).hasCount(4);
		// monday afternoon : one agenda, no assembly
		assertThat(plage(0, "14:00 - 17:00").locator(button("Cette plage assemble 2 agendas"))).hasCount(0);
	}

	@Test
	void aMessageRedefinedByTheProjectWins() {
		// the demo redefines planning.agendaWeek.duplicateWeek in its own messages, planning.properties in a parent MessageSource (PlanningDemoWebConfig)
		open(CABINET);
		assertThat(button("Dupliquer la semaine")).isVisible();
		assertThat(page.getByText("Dupliquer cette semaine")).hasCount(0);
		// the other messages come from the module
		assertThat(button("Créer une plage horaire")).isVisible();
	}

	@Test
	void plagesShowTheirStateAndTheirReservations() {
		open(CABINET);
		assertThat(plage(0, "09:00 - 12:00")).hasClass(Pattern.compile("bg-trh-publie"));
		assertThat(plage(3, "09:00 - 12:00")).hasClass(Pattern.compile("bg-trh-nonPublie")); // Dr Bernard : no test publishes his agenda
		// thursday : Dr Durand published with 3 reservations, Dr Bernard not published with 1 reservation (on a not published slot)
		assertThat(plage(3, "14:00 - 18:00")).containsText("3/16");
		assertThat(plage(3, "09:00 - 12:00")).containsText("1/12");
		assertThat(plage(3, "09:00 - 12:00").locator("[title='1 rendez-vous réservé(s) sur des créneaux non publiés']")).isVisible();

		open(LABORATOIRE);
		// 07:30 - 10:30, slots of 10 minutes, 4 counters : 18 x 4 = 72
		assertThat(plage(0, "07:30 - 10:30")).containsText("5/72");
		assertThat(plage(0, "07:30 - 10:30")).hasClass(Pattern.compile("bg-trh-publie"));
		assertThat(plage(2, "07:30 - 10:30")).hasClass(Pattern.compile("bg-trh-nonPublie"));
	}

	@Test
	void tranchesAreShownWithTheirHours() {
		open(ENTRETIENS);
		// the calendar shows the tranches, not the plages : monday 09:00 - 12:00 by 45 minutes and 14:00 - 17:00 by 60 minutes, 4 + 3 tranches
		final Locator mondayTranches = day(0).locator(".my-event");
		assertThat(mondayTranches).hasCount(7);
		// the reservations of the tranche (2 interviews on monday morning)
		assertThat(mondayTranches.first()).containsText("1 / 1");
		assertThat(mondayTranches.nth(2)).containsText("0 / 1");
		// the state of a tranche : a full disc when full, an empty circle when free.
		// It is a Material icon, its name is its text : an unknown name would be written in the icon font, out of the cell
		assertThat(mondayTranches.first().locator(".agendas-event .q-icon")).hasText(Pattern.compile("^circle$"));
		assertThat(mondayTranches.nth(2).locator(".agendas-event .q-icon")).hasText(Pattern.compile("^radio_button_unchecked$"));
	}

	@Test
	void trancheHasNoAction() {
		// a tranche doesn't know its plage (no plhId) : a click opens nothing, neither the creation of a plage, and asks nothing to the server
		open(ENTRETIENS);
		final List<String> requests = new ArrayList<>();
		page.onRequest(request -> {
			if ("POST".equals(request.method())) { // the actions, not a resource loaded late
				requests.add(request.url());
			}
		});
		day(0).locator(".my-event").first().click();
		button("Semaine suivante").click();
		assertThat(page.getByText("Semaine du " + demoDay(7).format(DATE_FORMAT) + " au " + demoDay(11).format(DATE_FORMAT))).isVisible();
		assertThat(dialog()).hasCount(0);
		Assertions.assertEquals(List.of(), requests.stream().filter(url -> !url.contains("_semaineSuivante")).toList());
	}

	@Test
	void noInteractiveElementInsideAnother() {
		for (final String path : ALL_PAGES) {
			assertNoInteractiveElementInsideAnother(path);
		}
	}

	private void assertNoInteractiveElementInsideAnother(final String path) {
		open(path);
		assertNoInteractiveElementInsideAnother(path, ".q-calendar-day");
		// the menus of the multi-agendas plages are rendered when opened : open the one of monday
		if (CABINET.equals(path)) {
			plage(0, "09:00 - 12:00").locator(button("Cette plage assemble 2 agendas")).click();
			assertThat(page.locator(".q-menu")).isVisible();
			assertNoInteractiveElementInsideAnother(path + " (menu)", ".q-menu");
		}
	}

	private void assertNoInteractiveElementInsideAnother(final String path, final String rootSelector) {
		final List<?> nested = (List<?>) page.evaluate("([root, selector]) => [...document.querySelectorAll(root)]"
				+ ".flatMap(rootElement => [...rootElement.querySelectorAll(selector)])"
				+ ".filter(element => element.parentElement.closest(selector) !== null)"
				+ ".map(element => element.outerHTML.substring(0, 200))", List.of(rootSelector, INTERACTIVE));
		Assertions.assertTrue(nested.isEmpty(), () -> path + " : interactive elements inside another one : " + nested);
	}

	@Test
	void menuButtonsHoldTextOnly() {
		// a button holds phrasing content only (valid html, RGAA 8.2) : the layout of a line of the menu is out of its buttons
		open(CABINET);
		plage(2, "08:30 - 12:30").locator(button("Cette plage assemble 4 agendas")).click();
		assertThat(page.locator(".q-menu")).isVisible();
		final List<?> buttonsWithBlocks = (List<?>) page.evaluate("() => [...document.querySelectorAll('.q-menu button')]"
				+ ".filter(button => button.querySelector('div') !== null).map(button => button.outerHTML.substring(0, 200))");
		Assertions.assertEquals(List.of(), buttonsWithBlocks);
	}

	@Test
	void everyButtonOfTheCalendarHasAName() {
		for (final String path : ALL_PAGES) {
			open(path);
			Assertions.assertEquals(List.of(), unnamedButtons(page.locator(".q-calendar-day")), path);
		}
	}

	@Test
	void noDuplicatedId() {
		for (final String path : ALL_PAGES) {
			open(path);
			final List<?> duplicatedIds = (List<?>) page.evaluate("() => [...document.querySelectorAll('[id]')].map(element => element.id)"
					+ ".filter((id, index, ids) => ids.indexOf(id) !== index)");
			Assertions.assertEquals(List.of(), duplicatedIds, path);
		}
	}

	@Test
	void agendaButtonsAreNamedWithTheAgendaName() {
		// one agenda, icon mode : the button is named with the agenda name
		open(MEDECIN);
		assertThat(plage(0, "09:00 - 12:00").locator(button("Dr Anne Martin"))).isVisible();
		// several agendas, two letters mode : the initials shown (they only save room), the name of the agenda then its initials for the button
		open(CABINET);
		assertThat(plage(1, "09:00 - 12:30").locator(button("Dr Paul Bernard (DB)"))).hasText("DB");
	}

	@Test
	void singleAgendaButtonIsNamedWithTheAgendaNameInTwoLettersMode() {
		// a plage of one agenda : named as in a plage of several agendas
		open(CABINET);
		assertThat(plage(0, "14:00 - 17:00").locator(button("Dr Anne Martin (DM)"))).hasText("DM");
	}
}
