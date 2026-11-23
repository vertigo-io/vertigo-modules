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
 * Planning of the cabinet for the secretariat : the agendas of the 4 doctors, read only (the page sets hasAuthzAdmin to false).
 * Read only for the display only : the routes of the agenda (deletion, creation...) stay open in the demo, a project secures them.
 */
@Controller
@RequestMapping("/cabinet/secretariat")
public class SecretariatController extends AbstractDemoAgendaController {
	@GetMapping("/")
	public void initContext(final ViewContext viewContext) {
		initDemoContext(viewContext, PlanningDemoData.cabinetAgendaUids(), false, false, 1, 15);
	}
}
