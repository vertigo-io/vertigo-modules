package io.vertigo.orchestra.dao.referential;

import javax.inject.Inject;

import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.orchestra.domain.referential.TriggerType;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
 @Generated
public final class TriggerTypeDAO extends DAO<TriggerType, java.lang.String> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public TriggerTypeDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(TriggerType.class, storeManager, taskManager);
	}

}