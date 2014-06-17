package io.vertigo.dynamo.plugins.environment.loaders.poweramc.core;

import io.vertigo.kernel.lang.Assertion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe ou entité.
 * @author pchretien
 */
public final class OOMClass {
	private final String code;
	private final String packageName;
	private final List<OOMAttribute> keyAttributes;
	private final List<OOMAttribute> fieldAttributes;

	OOMClass(final String code, final String packageName, final List<OOMAttribute> keyAttributes, final List<OOMAttribute> fieldAttributes) {
		Assertion.checkArgNotEmpty(code);
		//Assertion.notEmpty(packageName);
		Assertion.checkNotNull(keyAttributes);
		Assertion.checkNotNull(fieldAttributes);
		//---------------------------------------------------------------------
		this.code = code;
		this.packageName = packageName;
		this.keyAttributes = Collections.unmodifiableList(new ArrayList<>(keyAttributes));
		this.fieldAttributes = Collections.unmodifiableList(new ArrayList<>(fieldAttributes));
	}

	/**
	 * @return Code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return Nom du package.
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return Listes des champs identifiants (PK).
	 */
	public List<OOMAttribute> getKeyAttributes() {
		return keyAttributes;
	}

	/***
	 * @return Liste des champs non PK.
	 */
	public List<OOMAttribute> getFieldAttributes() {
		return fieldAttributes;
	}
}
