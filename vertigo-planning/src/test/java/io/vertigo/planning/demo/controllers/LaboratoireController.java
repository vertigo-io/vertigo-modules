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
package io.vertigo.planning.demo.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.vertigo.planning.demo.PlanningDemoData;
import io.vertigo.ui.core.ViewContext;

/**
 * Agenda of the laboratoire (blood tests) : one agenda, plages with several counters (mode guichet) ; a public holiday two weeks after
 * the demo week (wednesday, a week without plage, reached by a creation).
 */
@Controller
@RequestMapping("/laboratoire")
public class LaboratoireController extends AbstractDemoAgendaController {

	@GetMapping("/")
	public void initContext(final ViewContext viewContext) {
		initDemoContext(viewContext, List.of(PlanningDemoData.laboratoireAgendaUid()), true, false, 4, 10);
	}

	/** {@inheritDoc} */
	@Override
	protected List<LocalDate> holidays() {
		return List.of(PlanningDemoData.demoWeekMonday().plusDays(16));
	}
}
