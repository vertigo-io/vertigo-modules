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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.core.param.ParamValue;
import io.vertigo.social.impl.sms.SmsSendPlugin;
import io.vertigo.social.sms.Sms;
import io.vertigo.social.sms.SmsSendingReport;

public class LinkMobilitySmsSendPlugin implements SmsSendPlugin {

	private static final int MAX_LOGGED_PREFIX_NUM = 8; //maximum numbers keep in tracer tag for matched whitelistPrefix
	private final AnalyticsManager analyticsManager;
	private final LinkMobilitySmsWebServiceClient linkMobilitySmsWebServiceClient;

	private final boolean acceptAll;
	private final List<String> whitelistPrefixes;
	private long sendDelayMs = -1;
	private long lastSentTimeMs = 0;

	private static final int ALPHA_NUMERIC_ORIGINATOR_TON = 1;

	/**
	 * Constructor.
	 * @param whitelistPrefixesOpt prefixes of phone numbers to accept
	 * @param maxSmsPerMinute maximum number of SMS to send in a minute (LinkMobility reject if too fast)
	 * @param linkMobilitySmsWebServiceClient SMS web service client
	 * @param analyticsManager analytics manager
	 */
	@Inject
	public LinkMobilitySmsSendPlugin(
			final @ParamValue("whitelistPrefixes") Optional<String> whitelistPrefixesOpt,
			final @ParamValue("maxSmsPerMinute") Optional<Long> maxSmsPerMinute,
			final LinkMobilitySmsWebServiceClient linkMobilitySmsWebServiceClient,
			final AnalyticsManager analyticsManager) {
		Assertion.check()
				.isNotNull(whitelistPrefixesOpt)
				.isNotNull(linkMobilitySmsWebServiceClient)
				.isNotNull(analyticsManager)
				.when(maxSmsPerMinute.isPresent(),
						() -> Assertion.check().isTrue(maxSmsPerMinute.get() > 0, "maxSmsPerMinute must be greater than 0"));
		//---
		this.analyticsManager = analyticsManager;
		this.linkMobilitySmsWebServiceClient = linkMobilitySmsWebServiceClient;
		sendDelayMs = maxSmsPerMinute.map(v -> 60 * 1000L / v).orElse(0L); //convert max SMS per minute to delay between sending in ms
		if (whitelistPrefixesOpt.isPresent() && !whitelistPrefixesOpt.get().isBlank()) {
			acceptAll = false;
			whitelistPrefixes = Arrays.asList(whitelistPrefixesOpt.get().split(";"))
					.stream()
					.map(prefix -> prefix.replaceAll("[\\(\\)\\s\\.\\-]+", "")) //we accept ( ) . - and spaces as separators
					.toList();
		} else {
			acceptAll = true;
			whitelistPrefixes = Collections.emptyList();
		}

	}

	@Override
	public boolean acceptSms(final Sms sms) {
		return acceptAll ||
				sms.receivers().stream()
						.map(receiver -> receiver.replaceAll("[\\(\\)\\s\\.\\-]+", ""))
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
		double cost = 0.0;
		boolean sent = false;

		for (final var receiver : sms.receivers()) {
			final long sleepMs = sendDelayMs - (System.currentTimeMillis() - lastSentTimeMs);
			sleep(sleepMs);
			lastSentTimeMs = System.currentTimeMillis();

			final var linkMobilitySendingReportMap = linkMobilitySmsWebServiceClient.sendSms(
					receiver,
					ALPHA_NUMERIC_ORIGINATOR_TON,
					sms.sender(),
					sms.textContent());

			final Double responseCode = (Double) linkMobilitySendingReportMap.getOrDefault("responseCode", 99.0);
			if (responseCode == 0) {
				cost++;
				sent = true;
			} else {
				final String responseMessage = "[" + responseCode.intValue() + "] " + linkMobilitySendingReportMap.getOrDefault("responseMessage", "No message");
				throw WrappedException.wrap(new IOException(responseMessage));
			}
		}

		return new SmsSendingReport(cost, sent);
	}

	private void sleep(final long sleepMs) {
		if (sleepMs > 0) {
			try {
				Thread.sleep(sleepMs);
			} catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
