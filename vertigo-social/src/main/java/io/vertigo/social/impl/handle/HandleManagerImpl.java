/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.social.impl.handle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.commons.eventbus.EventBusSubscribed;
import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.commons.transaction.VTransactionWritable;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.VSystemException;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datafactory.criteria.Criteria;
import io.vertigo.datafactory.criteria.Criterions;
import io.vertigo.datamodel.data.definitions.DataAccessor;
import io.vertigo.datamodel.data.definitions.DataDefinition;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.model.DtListState;
import io.vertigo.datamodel.data.model.Entity;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.datastore.entitystore.StoreEvent;
import io.vertigo.social.handle.Handle;
import io.vertigo.social.handle.HandleManager;

public final class HandleManagerImpl implements HandleManager, Activeable {

	private static final int CHUNK_SIZE = 1000;

	private final EntityStoreManager entityStoreManager;
	private final VTransactionManager transactionManager;

	private List<DataDefinition> dtDefinitionsWithHandle;
	private final HandlePlugin handlePlugin;

	@Inject
	public HandleManagerImpl(
			final EntityStoreManager entityStoreManager,
			final VTransactionManager transactionManager,
			final HandlePlugin handlePlugin) {
		Assertion.check()
				.isNotNull(entityStoreManager)
				.isNotNull(transactionManager)
				.isNotNull(handlePlugin);
		//---
		this.entityStoreManager = entityStoreManager;
		this.transactionManager = transactionManager;
		this.handlePlugin = handlePlugin;

	}

	@Override
	public void start() {
		dtDefinitionsWithHandle = Node.getNode().getDefinitionSpace().getAll(DataDefinition.class)
				.stream()
				.filter(dtDefinition -> dtDefinition.getHandleField().isPresent())
				.toList();

	}

	@Override
	public void stop() {
		// nothing

	}

	/**
	 * Receive Store event.
	 * @param storeEvent Store event
	 */
	@EventBusSubscribed
	public void onEvent(final StoreEvent storeEvent) {
		final Map<DataDefinition, List<UID>> uidsByDefinition = storeEvent.getUIDs().stream()
				//On ne traite l'event que si il porte sur un KeyConcept
				.filter(uid -> uid.getDefinition().getHandleField().isPresent())
				.collect(Collectors.groupingBy(uid -> uid.getDefinition()));

		uidsByDefinition.entrySet().stream()
				.forEach(entry -> {
					final DataDefinition dataDefinition = entry.getKey();
					final List<UID> uids = entry.getValue();
					switch (storeEvent.getType()) {
						case UPDATE:
						case CREATE:

							final DtList<Entity> entities;
							try (VTransactionWritable transaction = transactionManager.createCurrentTransaction()) {
								//we need to make better than this...
								entities = entityStoreManager.find(dataDefinition, Criterions.in(() -> dataDefinition.getIdField().get().name(), uids.toArray()), DtListState.of(null));
							}
							// add the handle in the plugin
							handlePlugin.add(entities.stream().map(entity -> toHandle(dataDefinition, entity)).toList());
							break;
						case DELETE:
							handlePlugin.remove(uids);
							break;
						default:
							throw new VSystemException("Type of store Event {0} is not supported", storeEvent.getType());
					}

				});

	}

	@Override
	public List<String> getHandlePrefixes() {
		return dtDefinitionsWithHandle.stream()
				.map(dtDefinition -> dtDefinition.id().shortName())
				.map(StringUtil::first2LowerCase)
				.toList();
	}

	@Override
	public List<Handle> searchHandles(final String handlePrefix) {
		Assertion.check().isNotNull(handlePrefix);
		//---
		if (!StringUtil.isBlank(handlePrefix) && isStartingByDtDefinition(handlePrefix).isPresent()) {
			// search with the plugin
			return handlePlugin.search(handlePrefix);
		}
		return Collections.emptyList();
	}

	@Override
	public Handle getHandleByUid(final UID uid) {
		return handlePlugin.getByUid(uid);
	}

	@Override
	public Handle getHandleByCode(final String code) {
		return handlePlugin.getByCode(code);
	}

	private Optional<DataDefinition> isStartingByDtDefinition(final String handlePrefix) {
		return dtDefinitionsWithHandle
				.stream()
				.filter(dtDefinition -> handlePrefix.startsWith(StringUtil.first2LowerCase(dtDefinition.id().shortName()) + "/"))
				.findAny();
	}

	private static Handle toHandle(final DataDefinition dataDefinition, final Entity entity) {
		final DataAccessor dataAccessor = dataDefinition.getHandleField().get().getDataAccessor();
		return new Handle(entity.getUID(),
				StringUtil.first2LowerCase(dataDefinition.id().shortName()) + "/" +
						dataAccessor.getValue(entity));
	}

	@Override
	public void reindexAll() {
		handlePlugin.removeAll();
		dtDefinitionsWithHandle.forEach(this::indexDefinition);
	}

	private void indexDefinition(final DataDefinition dataDefinition) {
		final String idFieldName = dataDefinition.getIdField().get().name();
		final DataAccessor idFieldAccessor = dataDefinition.getIdField().get().getDataAccessor();
		int lastResultsSize;
		Serializable lastId = null;
		//we do it by chunks

		do {
			final List<Entity> entities;
			try (final VTransactionWritable transaction = transactionManager.createCurrentTransaction()) {
				final Criteria criteria = lastId != null ? Criterions.isGreaterThan(() -> idFieldName, lastId) : Criterions.alwaysTrue();
				final DtListState dtListState = DtListState.of(CHUNK_SIZE, 0, idFieldName, false);
				entities = entityStoreManager.find(dataDefinition, criteria, dtListState);
			}
			lastResultsSize = entities.size();
			lastId = (Serializable) idFieldAccessor.getValue(entities.get(entities.size() - 1));

			handlePlugin.add(entities.stream()
					.map(entity -> toHandle(dataDefinition, entity))
					.collect(Collectors.toList()));
		} while (lastResultsSize == CHUNK_SIZE);
	}

}
