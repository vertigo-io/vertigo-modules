package io.vertigo.easyforms.easyformsrunner.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EasyFormsParameterData extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	private static EasyFormsParameterDataAdapter adapter = new EasyFormsParameterDataAdapter();

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
