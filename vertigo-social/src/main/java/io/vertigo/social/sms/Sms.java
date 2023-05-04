/**
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
package io.vertigo.social.sms;

import java.util.List;

import io.vertigo.core.lang.Assertion;

public final class Sms {

	private final String sender;
	private final List<String> receivers;
	private final String textContent;
	private final boolean nonCommercialMessage;

	public Sms(final String sender, final List<String> receivers, final String textContent, final boolean nonCommercialMessage) {
		Assertion.check()
				.isNotNull(receivers)
				.isFalse(receivers.isEmpty(), "At least one receiver is needed for an sms")
				.isNotBlank(textContent);
		//---
		this.sender = sender;
		this.receivers = receivers;
		this.textContent = textContent;
		this.nonCommercialMessage = nonCommercialMessage;
	}

	public String getSender() {
		return sender;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public String getTextContent() {
		return textContent;
	}

	public boolean isNonCommercialMessage() {
		return nonCommercialMessage;
	}

}
