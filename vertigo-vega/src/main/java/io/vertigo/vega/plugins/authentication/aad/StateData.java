package io.vertigo.vega.plugins.authentication.aad;

import java.util.Date;

class StateData {
	private final String nonce;
	private final String requestedUri;
	private final Date expirationDate;

	StateData(final String nonce, final Date expirationDate, final String requestedUri) {
		this.nonce = nonce;
		this.expirationDate = expirationDate;
		this.requestedUri = requestedUri;
	}

	String getNonce() {
		return nonce;
	}

	Date getExpirationDate() {
		return expirationDate;
	}

	String getRequestedUri() {
		return requestedUri;
	}
}