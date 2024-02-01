package io.vertigo.easyforms.easyformsrunner.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EasyFormsParameterData extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	private static EasyFormsParameterDataAdapter adapter = new EasyFormsParameterDataAdapter();

	/**
	 * Merge 2 lists of parameters. If both define same parameter, the second parameter 'win'.
	 *
	 * @param a nullable, first set of parameters
	 * @param b nullable, second set of parameters
	 * @return a new set of parameters combining both definitions.
	 */
	public static EasyFormsParameterData combine(final EasyFormsParameterData a, final EasyFormsParameterData b) {
		final EasyFormsParameterData out = new EasyFormsParameterData();
		if (a != null) {
			out.putAll(a);
		}
		if (b != null) {
			out.putAll(b);
		}
		return out;
	}

	public EasyFormsParameterData() {
		super();
	}

	public EasyFormsParameterData(final Map<String, Serializable> m) {
		super(m);
	}

	@Override
	public String toString() {
		return adapter.toBasic(this);
	}

}
