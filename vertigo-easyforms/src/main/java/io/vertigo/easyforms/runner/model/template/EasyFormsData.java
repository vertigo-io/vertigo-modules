package io.vertigo.easyforms.runner.model.template;

import java.util.HashMap;
import java.util.Map;

public class EasyFormsData extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	/**
	 * Merge 2 lists of parameters. If both define same parameter, the second parameter 'win'.
	 *
	 * @param a nullable, first set of parameters
	 * @param b nullable, second set of parameters
	 * @return a new set of parameters combining both definitions.
	 */
	public static EasyFormsData combine(final EasyFormsData a, final EasyFormsData b) {
		final EasyFormsData out = new EasyFormsData();
		if (a != null) {
			out.putAll(a);
		}
		if (b != null) {
			out.putAll(b);
		}
		return out;
	}

	public EasyFormsData() {
		super();
	}

	public EasyFormsData(final Map<String, Object> m) {
		super(m);
	}

}
