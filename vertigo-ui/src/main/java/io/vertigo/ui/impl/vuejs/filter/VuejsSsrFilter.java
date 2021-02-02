/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2020, Vertigo.io, team@vertigo.io
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
package io.vertigo.ui.impl.vuejs.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.Definition;
import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleDefinitionProvider;
import io.vertigo.datastore.cache.CacheManager;
import io.vertigo.datastore.cache.definitions.CacheDefinition;
import io.vertigo.vega.impl.servlet.filter.AbstractFilter;
import io.vertigo.vega.impl.servlet.filter.ContentSecurityPolicyFilter;

/**
 * Filter to pre-compile vuejs template on the server-side to comply with CSP directives.
 * @author mlaroche
 */
public final class VuejsSsrFilter extends AbstractFilter implements SimpleDefinitionProvider {
	private static final Logger LOGGER = LogManager.getLogger(VuejsSsrFilter.class);
	private static final String VUEJS_SSR_CACHE_URL_SUFFIX = "@SSR-";
	private static final String VUEJS_SSR_CACHE_COLLECTION = "CacheVuejsSSR";
	private static final String VERTIGO_SSR_TAG_PATTERN_STR = "<(vertigo-ssr)(\\s[^>]*)?>";
	private static final Pattern VERTIGO_SSR_TAG_PATTERN = Pattern.compile(VERTIGO_SSR_TAG_PATTERN_STR);

	private String ssrServerUrl;
	private boolean doublePassRender = false;

	/** Object token, by */
	private CacheManager cacheManager;

	/** {@inheritDoc} */
	@Override
	public void doInit() {
		cacheManager = Node.getNode().getComponentSpace().resolve(CacheManager.class);

		final FilterConfig filterConfig = getFilterConfig();
		ssrServerUrl = filterConfig.getInitParameter("ssrServerUrl");
		Assertion.check().isNotNull(ssrServerUrl);

		doublePassRender = Boolean.parseBoolean(filterConfig.getInitParameter("doublePassRender"));
	}

	@Override
	public List<? extends Definition> provideDefinitions(final DefinitionSpace definitionSpace) {
		return Collections.singletonList(new CacheDefinition(
				VUEJS_SSR_CACHE_COLLECTION,
				false,
				1000, //1000 elements max
				5, //5s TTL
				5, //5s TTI (idle)
				false));
	}

	@Override
	public void doMyFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		if (!(req instanceof HttpServletRequest) || !(res instanceof HttpServletResponse)) {
			chain.doFilter(req, res);
			return;
		}

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		if (doublePassRender && request.getRequestURL().toString().contains(VUEJS_SSR_CACHE_URL_SUFFIX)) {
			final String uid = request.getRequestURL().toString();
			final String scriptSSR = (String) cacheManager.get(VUEJS_SSR_CACHE_COLLECTION, uid);
			cacheManager.remove(VUEJS_SSR_CACHE_COLLECTION, uid); //get only once
			response.getWriter().print(scriptSSR);
			return;
		}

		final Optional<String> nonce = Optional.ofNullable((String) request.getAttribute(ContentSecurityPolicyFilter.NONCE_ATTRIBUTE_NAME));
		try (final VuejsSsrServletResponseWrapper wrappedResponse = new VuejsSsrServletResponseWrapper(response)) {
			boolean hasError = true;
			try {
				chain.doFilter(request, wrappedResponse);
				hasError = false;
				try {
					final VueJsPageSplit vueJsPageSplit = vuejsContentSplit(wrappedResponse.getAsString());
					final String compiledTemplate = compileVueJsTemplate(vueJsPageSplit.getTemplateToCompile(), ssrServerUrl);

					final String replacedTemplate;
					if (!doublePassRender) {
						replacedTemplate = vuejsSsrNonce(compiledTemplate, nonce);
					} else {
						replacedTemplate = vuejsSsrDoublePass(compiledTemplate, request.getRequestURL(), cacheManager);
					}
					response.getWriter().print(vueJsPageSplit.getBefore());
					response.getWriter().print(replacedTemplate);
					response.getWriter().print(vueJsPageSplit.getAfter());
				} catch (final Exception e) {
					LOGGER.error("Can't process VueJsSSR replacement", e);
					response.getWriter().print(wrappedResponse.getAsString());
				}
			} finally {
				if (hasError) { //it's error page
					//text is already encoded by thymeleaf (content is already secure)
					response.getWriter().print(wrappedResponse.getAsString());
				}
			}
		}
	}

	private static VueJsPageSplit vuejsContentSplit(final String fullContent) {
		final Matcher matcher = VERTIGO_SSR_TAG_PATTERN.matcher(fullContent);
		final String before;
		final String templateToCompile;
		final String after;
		if (matcher.find()) {
			final int start = matcher.start();
			final int startTagEnd = matcher.end();
			final String tag = matcher.group(1);
			final int end = findEndTag(fullContent, start, tag, 0);
			before = fullContent.substring(0, start);
			templateToCompile = fullContent.substring(startTagEnd, end); //crop container tag
			after = fullContent.substring(end + 3 + tag.length());
		} else {
			throw new IllegalArgumentException("Can't find tag " + "page" + " in result");
		}
		return new VueJsPageSplit(before, templateToCompile, after);
	}

	private static String vuejsSsrNonce(final String compiledTemplate, final Optional<String> nonce) {
		final StringBuilder replacedTemplate = new StringBuilder();
		replacedTemplate.append("<script");
		if (nonce.isPresent()) {
			replacedTemplate.append(" nonce=\"")
					.append(nonce.get())
					.append('\"');
		}
		replacedTemplate.append(">\r\n")
				.append(compiledTemplate)
				.append("</script>\r\n");
		return replacedTemplate.toString();
	}

	private static String vuejsSsrDoublePass(final String compiledTemplate, final StringBuffer currentUrl, final CacheManager cacheManager) {
		final String uid = currentUrl.append(VUEJS_SSR_CACHE_URL_SUFFIX).append(UUID.randomUUID().toString()).append(".js").toString();

		cacheManager.put(VUEJS_SSR_CACHE_COLLECTION, uid, compiledTemplate);

		final StringBuilder replacedTemplate = new StringBuilder();
		replacedTemplate.append("<script src=\"")
				.append(uid)
				.append("\"></script>\r\n");
		return replacedTemplate.toString();
	}

	private static int findEndTag(final String temp, final int from, final String tag, final int deep) {
		int end = temp.indexOf("</" + tag + ">", from);
		Assertion.check().isTrue(end > 0, "Cant find en tag {0} after position {1}", "</" + tag + ">", from);
		int innerStart = temp.indexOf("<" + tag, from + 1 + tag.length());
		int innerEnd = -1;
		while (innerStart != -1 && innerStart < end) {
			innerEnd = findEndTag(temp, innerStart, tag, deep + 1);
			end = temp.indexOf("</" + tag + ">", innerEnd + 3 + tag.length());
			Assertion.check().isTrue(end > 0, "Cant find en tag {0} after position {1} ({2})", "</" + tag + ">", from, deep);
			innerStart = temp.indexOf("<" + tag, innerEnd);
		}
		return end;
	}

	public static String compileVueJsTemplate(final String template, final String serverUrl) {
		final JsonObject requestParameter = new JsonObject();
		requestParameter.add("template", new JsonPrimitive(template));
		final JsonObject compiledTemplate = callRestWS(serverUrl, GSON.toJson(requestParameter), JsonObject.class);
		final String render = compiledTemplate.get("render").getAsString();
		final List<String> staticRenderFns = StreamSupport.stream(compiledTemplate.get("staticRenderFns").getAsJsonArray().spliterator(), false)
				.map(JsonElement::getAsString)
				.collect(Collectors.toList());

		final StringBuilder renderJsFunctions = new StringBuilder("var VertigoSsr = {}\r\n");
		renderJsFunctions.append("VertigoSsr.render = function(h) {\r\n")
				.append(render).append(" \r\n")
				.append("};\r\n")
				.append("  VertigoSsr.staticRenderFns = [\r\n");
		staticRenderFns.forEach(staticFn -> renderJsFunctions
				.append("		  function () {\r\n")
				.append(staticFn).append(" \r\n")
				.append("		  }\r\n"));
		renderJsFunctions.append("]\r\n");
		return renderJsFunctions.toString();
	}

	private static final Gson GSON = new GsonBuilder().create();

	private static <R> R callRestWS(final String wsUrl, final String jsonPayload, final Type returnType) {
		Assertion.check().isNotBlank(wsUrl);
		// ---
		try {
			final URL url = new URL(wsUrl);
			final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(500);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			httpURLConnection.setRequestProperty("Accept", "application/json");
			httpURLConnection.setDoOutput(true);

			try (OutputStream os = httpURLConnection.getOutputStream()) {
				final byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			final ByteArrayOutputStream result = new ByteArrayOutputStream();
			final byte[] buffer = new byte[1024];
			try (InputStream inputStream = httpURLConnection.getInputStream()) {
				int length;
				while ((length = inputStream.read(buffer)) != -1) {
					result.write(buffer, 0, length);
				}
			}
			return GSON.fromJson(result.toString(StandardCharsets.UTF_8), returnType);
		} catch (final IOException e) {
			throw WrappedException.wrap(e);
		}

	}

	private static class VueJsPageSplit {
		private final String before;
		private final String templateToCompile;
		private final String after;

		public VueJsPageSplit(final String before, final String templateToCompile, final String after) {
			this.before = before;
			this.templateToCompile = templateToCompile;
			this.after = after;
		}

		public String getBefore() {
			return before;
		}

		public String getTemplateToCompile() {
			return templateToCompile;
		}

		public String getAfter() {
			return after;
		}
	}
}
