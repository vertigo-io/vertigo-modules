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

import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;

/**
 * What the user does with the agenda : the menu of a plage assembling several agendas (mouse and keyboard), the detail of a
 * plage, the creation, modification and deletion of a plage, with one agenda and with counters, the publication, the
 * duplication, the week navigation.
 */
@EnabledIf("io.vertigo.planning.ui.AbstractPlanningUiTest#hasDatabase") // run on demand, see AbstractPlanningUiTest
public final class PlanningAgendaActionsTest extends AbstractPlanningUiTest {

	@Test
	void multiAgendasMenuOpensWithTheMouse() {
		open(CABINET);
		// a click in the cell of the plage, on the multi-agendas icon : the menu lists the plage of each agenda
		plage(0, "09:00 - 12:00").locator(button("Cette plage assemble 2 agendas")).click();
		final Locator menu = page.locator(".q-menu");
		assertThat(menu).isVisible();
		assertThat(menu.getByRole(AriaRole.LISTITEM)).hasCount(2);
		// a line : the open button (the agenda name), the hours, the state and the reservations of the plage, the delete button
		// (named with the hours : an agenda may have two plages in a group)
		assertThat(menu.locator(button("Ouvrir la plage de Dr Anne Martin 09:00 - 12:00"))).isVisible();
		assertThat(menu.locator(button("Ouvrir la plage de Dr Paul Bernard 09:00 - 12:00"))).isVisible();
		assertThat(menu.getByRole(AriaRole.LISTITEM).first()).containsText("09:00 - 12:00");
		assertThat(menu.getByRole(AriaRole.LISTITEM).first().locator(".q-badge")).hasAttribute("title", "Publié");
		assertThat(menu.getByRole(AriaRole.LISTITEM).first()).containsText("0/12");
		assertThat(menu.locator(button("Supprimer"))).hasCount(2);
		// the menu is a list, not a menu of actions : no menuitem
		assertThat(menu.getByRole(AriaRole.MENUITEM)).hasCount(0);
	}

	@Test
	void multiAgendasMenuOpensWithTheKeyboard() {
		open(CABINET);
		final Locator multiAgendasButton = plage(2, "08:30 - 12:30").locator(button("Cette plage assemble 4 agendas"));
		assertThat(multiAgendasButton).not().hasAttribute("tabindex", "-1"); // reachable with Tab
		assertThat(multiAgendasButton).hasAttribute("aria-haspopup", "dialog"); // it opens the list of the plages, a dialog
		multiAgendasButton.focus();
		page.keyboard().press("Enter");
		final Locator menu = page.locator(".q-menu");
		assertThat(menu).isVisible();
		assertThat(menu).hasAttribute("role", "dialog");
		assertThat(menu).hasAttribute("aria-label", "Cette plage assemble 4 agendas");
		// the 4 agendas, also the 2 hidden behind 'Voir plus d'agenda'
		assertThat(menu.getByRole(AriaRole.LISTITEM)).hasCount(4);
		// the keyboard reaches the plages of the menu and opens one
		final Locator selectPetit = menu.locator(button("Ouvrir la plage de Dr Claire Petit 08:30 - 12:30"));
		assertThat(selectPetit).not().hasAttribute("tabindex", "-1");
		selectPetit.focus();
		page.keyboard().press("Enter");
		assertThat(dialog()).isVisible();
		assertThat(dialog()).containsText("Dr Claire Petit");
		page.keyboard().press("Escape");
		assertThat(dialog()).isHidden();
	}

	@Test
	void plageOfTheMenuOpensItsDetail() {
		open(CABINET);
		plage(1, "09:00 - 12:30").locator(button("Cette plage assemble 3 agendas")).click();
		page.locator(".q-menu").locator(button("Ouvrir la plage de Dr Paul Bernard 09:30 - 12:00")).click();
		assertThat(dialog()).isVisible();
		assertThat(dialog()).containsText("Dr Paul Bernard");
		assertThat(dialog()).containsText("09:30");
	}

	@Test
	void lineOfTheMenuOpensItsPlageWithTheMouse() {
		// the mouse : anywhere in the line, here on its reservations
		open(CABINET);
		plage(1, "09:00 - 12:30").locator(button("Cette plage assemble 3 agendas")).click();
		page.locator(".q-menu").getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText("Dr Claire Petit")).locator(".q-chip").click();
		assertThat(dialog()).isVisible();
		assertThat(dialog()).containsText("Dr Claire Petit");
	}

	@Test
	void agendaOfAMultiAgendasPlageOpensItsPlageDirectly() {
		open(CABINET);
		plage(0, "09:00 - 12:00").locator(button("Dr Anne Martin (DM)")).click();
		assertThat(dialog()).isVisible();
		assertThat(dialog()).containsText("Dr Anne Martin");
		assertThat(page.locator(".q-menu")).hasCount(0);
	}

	@Test
	void plageOfOneAgendaOpensItsDetail() {
		open(CABINET);
		plage(3, "14:00 - 18:00").locator(button("Dr Louis Durand (DD)")).click();
		assertThat(dialog()).isVisible();
		assertThat(dialog()).containsText("Dr Louis Durand");
		assertThat(dialog()).containsText("3 rendez-vous réservé(s) sur 16 proposés");
	}

	@Test
	void plageIsCreatedModifiedAndDeleted() {
		open(MEDECIN);
		// creation : friday 17:30 - 18:30, slots of 15 minutes ; after the plages of the cabinet that day (until 17:00), not touching them
		// (left by a failure, it would only change the count of weekIsDuplicated)
		button("Créer une plage horaire").click();
		fillPlage(demoDay(4).format(DATE_FORMAT), "17:30", "18:30");
		dialog().locator(button("Valider")).click();
		assertNotified("Plage horaire créée");
		assertThat(plage(4, "17:30 - 18:30")).containsText("0/4");
		assertThat(plage(4, "17:30 - 18:30")).hasClass(Pattern.compile("bg-trh-nonPublie"));

		// modification : until 19:30, confirmed
		plage(4, "17:30 - 18:30").locator(button("Modifier")).click();
		final Locator heureFin = dialog().getByLabel("Heure de fin");
		heureFin.fill("19:30");
		dialog().locator(button("Modifier")).click();
		dialog().last().locator(button("Valider")).click();
		assertNotified("Plage horaire mise à jour");
		assertThat(plage(4, "17:30 - 19:30")).containsText("0/8");

		// deletion, confirmed
		plage(4, "17:30 - 19:30").locator(button("Supprimer")).click();
		dialog().locator(button("Supprimer")).click();
		assertNotified("Plage horaire supprimée");
		assertThat(plage(4, "17:30 - 19:30")).hasCount(0);
	}

	@Test
	void plageWithCountersIsCreated() {
		open(LABORATOIRE);
		// thursday 14:00 - 15:00, 4 counters, slots of 10 minutes : 6 x 4 = 24
		button("Créer une plage horaire").click();
		fillPlage(demoDay(3).format(DATE_FORMAT), "14:00", "15:00");
		final Locator nbGuichet = dialog().getByLabel("Nombre de guichets");
		nbGuichet.fill("4");
		dialog().locator(button("Valider")).click();
		assertNotified("Plage horaire créée");
		assertThat(plage(3, "14:00 - 15:00")).containsText("0/24");
	}

	@Test
	void conflictingPlageIsRefused() {
		open(MEDECIN);
		button("Créer une plage horaire").click();
		fillPlage(demoDay(0).format(DATE_FORMAT), "10:00", "11:00");
		dialog().locator(button("Valider")).click();
		assertThat(dialog()).containsText("La plage horaire est en conflit avec une autre");
		assertThat(plage(0, "10:00 - 11:00")).hasCount(0);
	}

	@Test
	void plagesArePublished() {
		open(MEDECIN);
		// wednesday 08:30 - 12:30 of Dr Martin is published (in the data), monday afternoon is not
		assertThat(plage(0, "14:00 - 17:00")).hasClass(Pattern.compile("bg-trh-nonPublie"));
		button("Gérer la publication").click();
		dialog().getByRole(AriaRole.TAB, new Locator.GetByRoleOptions().setName("Publier immédiatement")).click();
		dialog().locator(button("Valider")).click();
		assertNotified("Publication effectuée");
		assertThat(plage(0, "14:00 - 17:00")).hasClass(Pattern.compile("bg-trh-publie"));
	}

	@Test
	void weekIsDuplicated() {
		open(MEDECIN);
		button("Dupliquer la semaine").click(); // the label redefined by the demo (PlanningDemoWebConfig)
		assertThat(dialog()).isVisible();
		dialog().locator(button("Valider")).click();
		assertNotified("Duplication effectuée");
		// the agenda shows the first free week (the next one) : the 4 plages of Dr Martin but the one of wednesday, a public holiday
		assertThat(page.getByText("Semaine du " + demoDay(7).format(DATE_FORMAT) + " au " + demoDay(11).format(DATE_FORMAT))).isVisible();
		assertThat(page.locator(".q-calendar-day .my-event")).hasCount(3);
		assertThat(day(2).locator(".my-event")).hasCount(0);
	}

	@Test
	void holidayIsShownAfterAMoveToItsWeek() {
		// a creation two weeks after shows the week of the new plage : its public holiday (wednesday) too
		open(LABORATOIRE);
		button("Créer une plage horaire").click();
		fillPlage(demoDay(15).format(DATE_FORMAT), "14:00", "15:00");
		dialog().getByLabel("Nombre de guichets").fill("1");
		dialog().locator(button("Valider")).click();
		assertNotified("Plage horaire créée");
		assertThat(page.getByText("Semaine du " + demoDay(14).format(DATE_FORMAT) + " au " + demoDay(18).format(DATE_FORMAT))).isVisible();
		assertThat(page.locator(".q-badge").filter(new Locator.FilterOptions().setHasText("Jour férié"))).hasCount(1);
	}

	@Test
	void holidayIsShownAndClosed() {
		// the infos of the calendar given by the project : thursday a public holiday, closed
		open(MEDECIN);
		final Locator holiday = page.locator(".q-badge").filter(new Locator.FilterOptions().setHasText("Jour férié"));
		assertThat(holiday).hasCount(1);
		assertThat(holiday).hasAttribute("title", "Fermé");
		// a closed day is not a target of the duplication of a day
		button("Dupliquer une journée").click();
		assertThat(dialog()).isVisible();
		assertThat(dialog().getByRole(AriaRole.CHECKBOX, new Locator.GetByRoleOptions().setName("Dupliquer vers vendredi"))).isVisible();
		assertThat(dialog().getByRole(AriaRole.CHECKBOX, new Locator.GetByRoleOptions().setName("Dupliquer vers jeudi"))).hasCount(0);
	}

	@Test
	void weeksAreBrowsed() {
		open(LABORATOIRE);
		button("Semaine suivante").click();
		assertThat(page.getByText("Semaine du " + demoDay(7).format(DATE_FORMAT) + " au " + demoDay(11).format(DATE_FORMAT))).isVisible();
		button("Semaine précédente").click();
		button("Semaine précédente").click();
		assertThat(page.getByText("Semaine du " + demoDay(-7).format(DATE_FORMAT) + " au " + demoDay(-3).format(DATE_FORMAT))).isVisible();
	}

	@Test
	void slotOfAPlageIsDeleted() {
		open(LABORATOIRE);
		// friday 07:30 - 10:30 : 18 slots of 10 minutes, a click in the plage opens its detail
		plage(4, "07:30 - 10:30").locator(".plage-label").click();
		assertThat(dialog()).isVisible();
		final Locator deleteSlot = dialog().locator(button("Supprimer ce créneau"));
		assertThat(deleteSlot).hasCount(18);
		// the icons of the detail : Material icons by default (planning.properties), the deletion of a slot, its state
		assertThat(deleteSlot.first().locator(".q-icon")).hasText("delete_outline");
		assertThat(dialog().locator(".q-icon").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^ri-")))).hasCount(0);
		deleteSlot.first().click();
		assertNotified("Tranche horaire supprimée");
		assertThat(deleteSlot).hasCount(17);
	}

	@Test
	void detailHasNoUnnamedButton() {
		open(CABINET);
		plage(3, "14:00 - 18:00").locator(button("Dr Louis Durand (DD)")).click();
		assertThat(dialog()).isVisible();
		// the dialog named with its title (WCAG 4.1.2)
		assertThat(dialog()).hasAccessibleName(Pattern.compile("^Plage horaire du \\d\\d/\\d\\d/\\d{4} de 14:00 à 18:00$"));
		// the toolbar of the detail : the back button, an icon (RGAA 11.9, WCAG 4.1.2)
		final Locator toolbar = dialog().locator(".q-toolbar");
		assertThat(toolbar.locator(button("Fermer"))).isVisible();
		Assertions.assertEquals(List.of(), unnamedButtons(toolbar));
		toolbar.locator(button("Fermer")).click();
		assertThat(dialog()).isHidden();
	}

	@Test
	void plageOpensWithTheKeyboardInModeGuichet() {
		open(LABORATOIRE);
		// no agenda button in mode guichet : the hours are a button, named as they are read (RGAA 7.1, WCAG 2.1.1)
		final Locator openPlage = plage(0, "07:30 - 10:30").locator(button("Ouvrir la plage de 07:30 - 10:30"));
		assertThat(openPlage).not().hasAttribute("tabindex", "-1"); // reachable with Tab
		// the layout unchanged : the button has the style of the label (.plage-label), not the one of a button
		assertThat(openPlage).hasCSS("font-weight", "700");
		assertThat(openPlage).hasCSS("font-size", "16px");
		assertThat(openPlage).hasCSS("background-color", "rgba(0, 0, 0, 0)");
		// the focus of the keyboard as Quasar's : the color of the text, lightly (its focus helper), no outline
		openPlage.focus();
		page.keyboard().press("Shift+Tab");
		page.keyboard().press("Tab");
		assertThat(openPlage.locator(".q-focus-helper")).hasCSS("opacity", "0.22");
		page.keyboard().press("Enter");
		assertThat(dialog()).isVisible();
		assertThat(dialog()).containsText("5 rendez-vous réservé(s) sur 72 proposés");
	}

	@Test
	void plageContainedInAnotherOneIsAssembledWithIt() {
		open(CABINET);
		// friday morning : Dr Petit 09:00 - 12:00 and Dr Durand 10:00 - 11:00 ; friday afternoon : Dr Durand 15:00 - 16:00 and Dr Bernard 14:00 - 17:00
		// the order of the plages comes from the database (no order by) : getEvents on the 2 plages of each half day, in both orders,
		// gives 1 plage assembling 2 agendas
		final Object assembled = page.evaluate("day => {"
				+ " const methods = VUiExtensions.methods;"
				+ " const friday = VertigoUi.vueData.plagesHoraire.filter(p => methods.toCalendarDate(p.dateLocale, p.minutesDebut).date === day);"
				+ " return [friday.filter(p => p.minutesDebut < 13 * 60), friday.filter(p => p.minutesDebut >= 13 * 60 && p.minutesDebut < 17 * 60)]"
				+ "  .flatMap(pair => [pair, [...pair].reverse()])"
				+ "  .map(pair => methods.getEvents.call(methods, day, pair).map(e => (e.eventList || [e]).length).join(','));"
				+ "}", demoDay(4).toString());
		Assertions.assertEquals(List.of("2", "2", "2", "2"), assembled);
		// and shown so
		assertThat(plage(4, "09:00 - 12:00").locator(button("Cette plage assemble 2 agendas"))).isVisible();
		assertThat(plage(4, "10:00 - 11:00")).hasCount(0);
		assertThat(plage(4, "14:00 - 17:00").locator(button("Cette plage assemble 2 agendas"))).isVisible();
		assertThat(plage(4, "15:00 - 16:00")).hasCount(0);
	}

	@Test
	void readOnlyPlanningHasNoActionOnThePlages() {
		// the control : on the cabinet, a click on a free time of the calendar opens the creation of a plage
		open(CABINET);
		final Locator.ClickOptions fridayFirstHalfHour = new Locator.ClickOptions().setPosition(20, 5); // friday : no plage before 09:00
		day(4).click(fridayFirstHalfHour);
		assertThat(dialog()).isVisible();
		page.keyboard().press("Escape");
		assertThat(dialog()).isHidden();

		open(SECRETARIAT);
		assertThat(button("Créer une plage horaire")).hasCount(0); // the action buttons of the page : none
		// no modification nor deletion, on a plage nor in the menu of a plage assembling several agendas
		assertThat(day(3).locator(button("Modifier"))).hasCount(0);
		assertThat(day(3).locator(button("Supprimer"))).hasCount(0);
		plage(0, "09:00 - 12:00").locator(button("Cette plage assemble 2 agendas")).click();
		assertThat(page.locator(".q-menu").locator(button("Ouvrir la plage de Dr Anne Martin 09:00 - 12:00"))).isVisible();
		assertThat(page.locator(".q-menu").locator(button("Supprimer"))).hasCount(0);
		page.keyboard().press("Escape");
		assertThat(page.locator(".q-menu")).hasCount(0);
		// no creation on a free time : the detail of a plage is the only dialog opened after
		day(4).click(fridayFirstHalfHour);
		plage(3, "14:00 - 18:00").locator(button("Dr Louis Durand (DD)")).click();
		assertThat(dialog()).hasCount(1);
		assertThat(dialog()).containsText("Dr Louis Durand");
		// the detail read only too : no deletion of a slot
		assertThat(dialog()).containsText("14:00 - 14:15");
		assertThat(dialog().locator(button("Supprimer ce créneau"))).hasCount(0);
	}

	private void fillPlage(final String date, final String heureDebut, final String heureFin) {
		assertThat(dialog()).isVisible();
		dialog().getByLabel("Date de la plage horaire").fill(date);
		dialog().getByLabel("Heure de début").fill(heureDebut);
		dialog().getByLabel("Heure de fin").fill(heureFin);
	}
}
