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
package io.vertigo.easyforms.impl.designer;

import javax.inject.Inject;

import io.vertigo.core.locale.LocaleManager;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.easyforms.designer.EasyFormsDesignerManager;

public final class EasyFormsDesignerManagerImpl implements EasyFormsDesignerManager, Activeable {

	private final LocaleManager localeManager;

	@Inject
	public EasyFormsDesignerManagerImpl(final LocaleManager localeManager) {
		this.localeManager = localeManager;
	}

	@Override
	public void start() {
		localeManager.add("io.vertigo.easyforms.designer.Resources", Resources.values());
	}

	@Override
	public void stop() {
		// Nothing
	}

}
