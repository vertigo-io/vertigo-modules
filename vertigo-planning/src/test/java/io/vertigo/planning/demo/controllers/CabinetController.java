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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.vertigo.planning.demo.PlanningDemoData;
import io.vertigo.ui.core.ViewContext;

/**
 * Planning of the cabinet : the personal agendas of the 4 doctors together, a plage of several doctors at the same time assembles their agendas.
 */
@Controller
@RequestMapping("/cabinet")
public class CabinetController extends AbstractDemoAgendaController {

	@GetMapping("/")
	public void initContext(final ViewContext viewContext) {
		initDemoContext(viewContext, PlanningDemoData.cabinetAgendaUids(), false, false, 1, 15);
	}
}
