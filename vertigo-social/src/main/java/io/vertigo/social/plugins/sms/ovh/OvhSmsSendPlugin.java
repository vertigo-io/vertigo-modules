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
package io.vertigo.social.plugins.sms.ovh;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.param.ParamValue;
import io.vertigo.social.impl.sms.SmsSendPlugin;
import io.vertigo.social.sms.Sms;
import io.vertigo.social.sms.SmsSendingReport;

public class OvhSmsSendPlugin implements SmsSendPlugin {

	private static final String PATTERN_NORMALIZE_PREFIX = "[\\(\\)\\s\\.\\-]+";
	private static final int MAX_LOGGED_PREFIX_NUM = 8; //maximum numbers keep in tracer tag for matched whitelistPrefix
	private final AnalyticsManager analyticsManager;
	private final OvhSmsWebServiceClient ovhSmsWebServiceClient;
	private final String serviceName;

	private final boolean acceptAll;
	private final List<String> whitelistPrefixes;

	private static final long REFRESH_INTERVAL_MS = 5 * 60 * 1000;
	private static final String ENABLED_STATUS = "enable";

	private boolean enabled = false;
	private double creditsLeft = 0;
	private double creditLeftThreshold = 0;
	private long lastRefreshTimeMs = 0;

	@Inject
	public OvhSmsSendPlugin(
			final @ParamValue("whitelistPrefixes") Optional<String> whitelistPrefixesOpt,
			final @ParamValue("serviceName") String serviceName,
			final @ParamValue("creditLeftThreshold") Optional<Double> creditLeftThreshold,
			final OvhSmsWebServiceClient ovhSmsWebServiceClient,
			final AnalyticsManager analyticsManager) {
		Assertion.check()
				.isNotNull(whitelistPrefixesOpt)
				.isNotNull(ovhSmsWebServiceClient)
				.isNotNull(analyticsManager);
		//---
		this.analyticsManager = analyticsManager;
		this.ovhSmsWebServiceClient = ovhSmsWebServiceClient;
		this.serviceName = serviceName;
		this.creditLeftThreshold = creditLeftThreshold.orElse(-1D);
		if (whitelistPrefixesOpt.isPresent() && !whitelistPrefixesOpt.get().isBlank()) {
			acceptAll = false;
			whitelistPrefixes = Arrays.asList(whitelistPrefixesOpt.get().split(";"))
					.stream()
					.map(prefix -> prefix.replaceAll(PATTERN_NORMALIZE_PREFIX, "")) //we accept ( ) . - and spaces as separators
					.toList();
		} else {
			acceptAll = true;
			whitelistPrefixes = Collections.emptyList();
		}

	}

	@Override
	public boolean acceptSms(final Sms sms) {
		if (!acceptSms()) {
			return false;
		}

		return acceptAll ||
				sms.receivers().stream()
						.map(receiver -> receiver.replaceAll(PATTERN_NORMALIZE_PREFIX, ""))
						.allMatch(receiver -> whitelistPrefixes.stream()
								.anyMatch(prefix -> {
									if (receiver.startsWith(prefix)) {
										analyticsManager.getCurrentTracer().ifPresent(
												tracer -> tracer.setTag("whitelistPrefix", prefix.substring(0, Math.min(prefix.length(), MAX_LOGGED_PREFIX_NUM))));
										return true;
									}
									//We can't tag whitelistPrefix, because it could be accepted by another plugin
									return false;
								}));
	}

	@Override
	public SmsSendingReport sendSms(final Sms sms) {
		final var ovhSendingReportMap = ovhSmsWebServiceClient.sendSms(
				serviceName,
				sms.sender(),
				sms.receivers(),
				sms.textContent(),
				sms.isNonCommercialMessage());

		analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer.setTag("ovh-serviceName", serviceName));

		final List<String> validReceivers = (List<String>) ovhSendingReportMap.getOrDefault("validReceivers", Collections.emptyList());
		final Double totalCreditsRemoved = (Double) ovhSendingReportMap.getOrDefault("totalCreditsRemoved", 0.0);

		return new SmsSendingReport(totalCreditsRemoved, !validReceivers.isEmpty());
	}

	private boolean acceptSms() {
		if (creditLeftThreshold < 0) {
			return true;
		}

		if (System.currentTimeMillis() - lastRefreshTimeMs > REFRESH_INTERVAL_MS) {
			final var ovhServiceReportMap = ovhSmsWebServiceClient.smsService(serviceName);
			enabled = ENABLED_STATUS.equals(ovhServiceReportMap.get("status"));
			creditsLeft = (Double) ovhServiceReportMap.getOrDefault("creditsLeft", 0.0);
			lastRefreshTimeMs = System.currentTimeMillis();
		}

		return enabled && creditsLeft > creditLeftThreshold;
	}

}
