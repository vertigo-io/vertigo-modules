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

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring6.SpringTemplateEngine;

import io.vertigo.ui.impl.springmvc.config.VSpringWebConfig;

/**
 * Spring config of the demo : the controllers and the messages, plugged as most projects do (setTemplateEngineMessageSource, see
 * the README) : the messages of the planning components in a parent MessageSource, the demo redefines one of them in its own
 * messages (a key of the project wins) ; the components of vertigo-ui read their own bundles.
 */
@ComponentScan("io.vertigo.planning.demo.controllers")
public class PlanningDemoWebConfig extends VSpringWebConfig {

	@Override
	public SpringTemplateEngine templateEngine() {
		final SpringTemplateEngine templateEngine = super.templateEngine();
		templateEngine.setTemplateEngineMessageSource(getMessageSource());
		return templateEngine;
	}

	private static MessageSource getMessageSource() {
		final ResourceBundleMessageSource vertigoMessageSource = new ResourceBundleMessageSource();
		vertigoMessageSource.addBasenames("io/vertigo/ui/components/planning/planning");
		vertigoMessageSource.setDefaultEncoding("UTF-8");
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setParentMessageSource(vertigoMessageSource); // the messages of the demo are looked up first
		messageSource.addBasenames("io/vertigo/planning/demo/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
