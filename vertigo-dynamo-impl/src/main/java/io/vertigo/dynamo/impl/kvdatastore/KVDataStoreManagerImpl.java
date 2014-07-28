/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
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
package io.vertigo.dynamo.impl.kvdatastore;

import io.vertigo.dynamo.kvdatastore.KVDataStoreManager;
import io.vertigo.kernel.lang.Assertion;
import io.vertigo.kernel.lang.Option;

import java.util.List;

import javax.inject.Inject;

public class KVDataStoreManagerImpl implements KVDataStoreManager {
	private final KVDataStorePlugin kvDataStorePlugin;

	@Inject
	public KVDataStoreManagerImpl(final KVDataStorePlugin kvDataStorePlugin) {
		Assertion.checkNotNull(kvDataStorePlugin);
		//---------------------------------------------------------------------
		this.kvDataStorePlugin = kvDataStorePlugin;
	}

	public void put(final String id, final Object objet) {
		kvDataStorePlugin.put(id, objet);
	}

	public <C> void delete(final String id) {
		kvDataStorePlugin.delete(id);
	}

	public <C> Option<C> find(final String id, final Class<C> clazz) {
		return kvDataStorePlugin.find(id, clazz);
	}

	public <C> List<C> findAll(final int skip, final Integer limit, final Class<C> clazz) {
		return kvDataStorePlugin.findAll(skip, limit, clazz);
	}

}
