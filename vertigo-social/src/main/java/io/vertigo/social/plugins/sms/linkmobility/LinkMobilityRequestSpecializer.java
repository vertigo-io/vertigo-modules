/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
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
package io.vertigo.social.plugins.sms.linkmobility;

import java.util.Base64;
import java.util.Map;

import javax.inject.Inject;

import io.vertigo.connectors.httpclient.HttpClientConnector;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.param.ParamValue;
import io.vertigo.vega.impl.webservice.client.HttpRequestBuilder;
import io.vertigo.vega.impl.webservice.client.RequestSpecializer;
import io.vertigo.vega.webservice.definitions.WebServiceDefinition;

public class LinkMobilityRequestSpecializer implements RequestSpecializer, Component {

	private final String username;
	private final String password;

	@Inject
	public LinkMobilityRequestSpecializer(
			final @ParamValue("username") String username,
			final @ParamValue("password") String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void specialize(final HttpRequestBuilder httpRequestBuilder, final WebServiceDefinition webServiceDefinition,
			final Map<String, Object> namedArgs, final HttpClientConnector httpClientConnector) {
		httpRequestBuilder.header("Authorization", getBasicAuthenticationHeader(username, password));
	}

	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

}
