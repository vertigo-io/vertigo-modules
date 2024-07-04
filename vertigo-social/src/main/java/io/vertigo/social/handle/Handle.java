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
package io.vertigo.social.handle;

import io.vertigo.core.lang.Assertion;
import io.vertigo.datamodel.data.model.UID;

public final class Handle {

	private final UID uid;
	private final String code;

	public Handle(
			final UID uid,
			final String code) {
		Assertion.check()
				.isNotNull(uid)
				.isNotBlank(code);
		//---
		this.uid = uid;
		this.code = code;
	}

	public UID getUid() {
		return uid;
	}

	public String getCode() {
		return code;
	}

}
