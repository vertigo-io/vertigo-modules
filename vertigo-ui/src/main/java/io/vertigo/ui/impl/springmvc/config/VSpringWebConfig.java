/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.ui.impl.springmvc.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import io.vertigo.connectors.spring.EnableVertigoSpringBridge;
import io.vertigo.ui.controllers.ListAutocompleteController;
import io.vertigo.ui.impl.springmvc.argumentresolvers.DtListStateMethodArgumentResolver;
import io.vertigo.ui.impl.springmvc.argumentresolvers.FileInfoURIConverter;
import io.vertigo.ui.impl.springmvc.argumentresolvers.FileInfoURIConverterValueHandler;
import io.vertigo.ui.impl.springmvc.argumentresolvers.UiFileInfoReturnValueHandler;
import io.vertigo.ui.impl.springmvc.argumentresolvers.UiMessageStackMethodArgumentResolver;
import io.vertigo.ui.impl.springmvc.argumentresolvers.UserSessionMethodArgumentResolver;
import io.vertigo.ui.impl.springmvc.argumentresolvers.VFileMethodArgumentResolver;
import io.vertigo.ui.impl.springmvc.argumentresolvers.VFileReturnValueHandler;
import io.vertigo.ui.impl.springmvc.argumentresolvers.ViewAttributeMethodArgumentResolver;
import io.vertigo.ui.impl.springmvc.argumentresolvers.ViewContextReturnValueAndArgumentResolver;
import io.vertigo.ui.impl.springmvc.authorization.VSpringMvcAuthorizationInterceptor;
import io.vertigo.ui.impl.springmvc.config.interceptors.VAnnotationHandlerInterceptorImpl;
import io.vertigo.ui.impl.springmvc.config.interceptors.VControllerInterceptorEngine;
import io.vertigo.ui.impl.springmvc.config.interceptors.VSpringMvcViewContextInterceptor;
import io.vertigo.ui.impl.springmvc.controller.VSpringMvcControllerAdvice;
import io.vertigo.ui.impl.springmvc.controller.VSpringMvcExceptionHandler;
import io.vertigo.ui.impl.thymeleaf.VUiStandardDialect;
import io.vertigo.ui.impl.thymeleaf.components.NamedComponentDefinition;
import io.vertigo.ui.impl.thymeleaf.components.NamedComponentParser;
import io.vertigo.ui.impl.thymeleaf.components.VuiResourceTemplateResolver;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Configuration
@EnableVertigoSpringBridge
@EnableWebMvc
public class VSpringWebConfig implements WebMvcConfigurer, ApplicationContextAware {

	@Autowired
	private ApplicationContext applicationContext;

	private static final String COMPONENT_PATH_PREFIX = "io/vertigo/ui/";
	private static final String[] STANDARD_UI_COMPONENTS_NAME = {
			"utils/vue-data", "utils/include-data", //technical components
			"layout/page", "layout/head", "layout/form", "layout/modal", "layout/block", //layout components
			"layout/grid", "layout/grid-cell", //grid
			"layout/messages", //messages
			"inputs/label", "inputs/text-field", "inputs/text-area", "inputs/checkbox", "inputs/checkbox-multiple", "inputs/slider", "inputs/knob", "inputs/fileupload", //standard controls components
			"inputs/select", "inputs/select-multiple", "inputs/radio", //select controls components
			"inputs/autocomplete", "inputs/autocomplete-multiple", "inputs/chips-autocomplete", //with client-worflow controls components
			"inputs/date", "inputs/datetime", //date
			"inputs/geolocation", // geoLocation
			"inputs/tree", // tree
			"inputs/text-editor", //text-editor (richtext)
			"table/table", "table/column", //table
			"collections/collection", "collections/list", "collections/cards", "collections/field-read", // collections
			"collections/search", "collections/facets", //search
			"buttons/button-submit", "buttons/button-link", "buttons/buttons-group" //buttons
	};

	/*
	* STEP 1 - Create SpringResourceTemplateResolver
	* */
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding("UTF-8");
		// for dev purpose
		templateResolver.setCacheable(!isDevMode());
		return templateResolver;
	}

	private VuiResourceTemplateResolver componentsResolver(final String componentPath) {
		final VuiResourceTemplateResolver templateResolver = new VuiResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("classpath://" + componentPath);
		templateResolver.setSuffix(".html");
		templateResolver.setResolvablePatterns(Collections.singleton("components/*"));
		// for dev purpose
		templateResolver.setCacheable(!isDevMode());
		return templateResolver;
	}

	/*
	* STEP 2 - Create SpringTemplateEngine
	* */
	@Bean
	public SpringTemplateEngine templateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		final SpringResourceTemplateResolver viewsResolvers = templateResolver();
		viewsResolvers.setOrder(3);
		templateEngine.setTemplateResolver(viewsResolvers);
		templateEngine.setEnableSpringELCompiler(true);
		//---
		// add custom components
		final VuiResourceTemplateResolver customComponentResolvers = componentsResolver(getCustomComponentsPathPrefix());
		customComponentResolvers.setOrder(1); //custom first
		customComponentResolvers.setCheckExistence(true); //some components may missing
		templateEngine.addTemplateResolver(customComponentResolvers);

		// add components
		final VuiResourceTemplateResolver componentResolvers = componentsResolver(COMPONENT_PATH_PREFIX);
		componentResolvers.setOrder(2);
		templateEngine.addTemplateResolver(componentResolvers);

		//---
		final VUiStandardDialect dialect = new VUiStandardDialect(getUiComponents(componentResolvers, customComponentResolvers));
		templateEngine.addDialect("vu", dialect);

		templateEngine.addDialect(new LayoutDialect());

		return templateEngine;
	}

	private Set<NamedComponentDefinition> getUiComponents(final VuiResourceTemplateResolver componentResolvers, final VuiResourceTemplateResolver customComponentResolvers) {
		final NamedComponentParser parser = new NamedComponentParser("vu", componentResolvers);

		final Set<NamedComponentDefinition> uiComponents = new HashSet<>();
		//standard components
		for (final String componentName : STANDARD_UI_COMPONENTS_NAME) {
			uiComponents.addAll(parser.parseComponent(componentName));
		}
		// custom components
		final NamedComponentParser customParser = new NamedComponentParser("vu", customComponentResolvers);
		for (final String componentName : getCustomComponentNames()) {
			uiComponents.addAll(customParser.parseComponent(componentName));
		}
		return uiComponents;
	}

	protected Set<String> getCustomComponentNames() {
		return Collections.emptySet();
	}

	/**
	 * Define prefix for custom components.
	 * Should be the module name : don't starts with /, ends with /
	 * Components must be put in : prefix+"/components/"+componentName
	 * @return path prefix for custom components
	 */
	protected String getCustomComponentsPathPrefix() {
		return COMPONENT_PATH_PREFIX;
	}

	@Bean(DispatcherServlet.REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME)
	public RequestToViewNameTranslator customRequestToViewNameTranslator() {
		return new VRequestToViewNameTranslator();
	}

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	/**
	 * STEP 3 - Register ThymeleafViewResolver
	 */
	@Override
	public void configureViewResolvers(final ViewResolverRegistry registry) {
		final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setCharacterEncoding("UTF-8");
		resolver.setTemplateEngine(templateEngine());
		registry.viewResolver(resolver);
	}

	@Override
	public void addFormatters(final FormatterRegistry registry) {
		registry.addConverter(new FileInfoURIConverter());
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new ViewAttributeMethodArgumentResolver());
		resolvers.add(new ViewContextReturnValueAndArgumentResolver());
		resolvers.add(new UiMessageStackMethodArgumentResolver());
		resolvers.add(new UserSessionMethodArgumentResolver());
		resolvers.add(new DtListStateMethodArgumentResolver());
		resolvers.add(new VFileMethodArgumentResolver());
		resolvers.add(new FileInfoURIConverterValueHandler());
	}

	@Override
	public void addReturnValueHandlers(final List<HandlerMethodReturnValueHandler> handlers) {
		handlers.add(new ViewContextReturnValueAndArgumentResolver());
		handlers.add(new VFileReturnValueHandler());
		handlers.add(new FileInfoURIConverterValueHandler());
		handlers.add(new UiFileInfoReturnValueHandler());
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		if (applicationContext instanceof ConfigurableApplicationContext) {
			final VSpringMvcControllerAdvice controllerAdvice = ((ConfigurableApplicationContext) applicationContext).getBeanFactory().createBean(VSpringMvcControllerAdvice.class);
			((ConfigurableApplicationContext) applicationContext).getBeanFactory().registerSingleton("viewContextControllerAdvice", controllerAdvice);
			final VSpringMvcExceptionHandler vExceptionHandler = ((ConfigurableApplicationContext) applicationContext).getBeanFactory().createBean(VSpringMvcExceptionHandler.class);
			((ConfigurableApplicationContext) applicationContext).getBeanFactory().registerSingleton("vExceptionHandler", vExceptionHandler);
			final ListAutocompleteController listAutocompleteController = ((ConfigurableApplicationContext) applicationContext).getBeanFactory().createBean(ListAutocompleteController.class);
			((ConfigurableApplicationContext) applicationContext).getBeanFactory().registerSingleton("listAutocompleteController", listAutocompleteController);
		}
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(new VSpringMvcAuthorizationInterceptor());
		registry.addInterceptor(new VSpringMvcViewContextInterceptor());
		registry.addInterceptor(new VAnnotationHandlerInterceptorImpl(getVControllerInterceptorEngines()));
	}

	protected List<Class<? extends VControllerInterceptorEngine>> getVControllerInterceptorEngines() {
		return Collections.emptyList();
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/vertigo-ui/static/**")
				.addResourceLocations("classpath:/io/vertigo/ui/static/")
				.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());

	}

	protected boolean isDevMode() {
		return true;
	}

}
