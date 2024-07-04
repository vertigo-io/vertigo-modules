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
package io.vertigo.easyforms.runner.model.template;

import java.util.HashMap;
import java.util.Map;

public final class EasyFormsData extends HashMap<String, Object> {

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
