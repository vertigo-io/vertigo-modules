package io.vertigo.orchestra.domain.node;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.dynamo.domain.stereotype.DataSpace("orchestra")
public final class ONode implements Entity {
	private static final long serialVersionUID = 1L;

	private String nodId;
	private java.time.ZonedDateTime lastHeartbeat;

	/** {@inheritDoc} */
	@Override
	public URI<ONode> getURI() {
		return DtObjectUtil.createURI(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id'.
	 * @return String nodId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_UUID", type = "ID", required = true, label = "Id")
	public String getNodId() {
		return nodId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id'.
	 * @param nodId String <b>Obligatoire</b>
	 */
	public void setNodId(final String nodId) {
		this.nodId = nodId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Last activity'.
	 * @return java.time.ZonedDateTime lastHeartbeat <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_TIMESTAMP", required = true, label = "Last activity")
	public java.time.ZonedDateTime getLastHeartbeat() {
		return lastHeartbeat;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Last activity'.
	 * @param lastHeartbeat java.time.ZonedDateTime <b>Obligatoire</b>
	 */
	public void setLastHeartbeat(final java.time.ZonedDateTime lastHeartbeat) {
		this.lastHeartbeat = lastHeartbeat;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
