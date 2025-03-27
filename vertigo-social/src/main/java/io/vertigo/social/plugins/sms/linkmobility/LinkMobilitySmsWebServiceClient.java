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

import java.util.Map;

import io.vertigo.core.node.component.Amplifier;
import io.vertigo.vega.impl.webservice.client.WebServiceProxyAnnotation;
import io.vertigo.vega.webservice.stereotype.AnonymousAccessAllowed;
import io.vertigo.vega.webservice.stereotype.GET;
import io.vertigo.vega.webservice.stereotype.PathPrefix;
import io.vertigo.vega.webservice.stereotype.QueryParam;
import io.vertigo.vega.webservice.stereotype.SessionLess;

@WebServiceProxyAnnotation(connectorName = "linkMobilitySms", requestSpecializer = "linkMobilityRequestSpecializer")
@PathPrefix("/v1")
public interface LinkMobilitySmsWebServiceClient extends Amplifier {

	@AnonymousAccessAllowed
	@SessionLess
	@GET("/sms/send")
	Map sendSms(
			@QueryParam("destinationAddress") final String destinationAddress,
			@QueryParam("originatorTON") final int originatorTON,
			@QueryParam("originatingAddress") final String originatingAddress,
			@QueryParam("messageText") final String message);
}
