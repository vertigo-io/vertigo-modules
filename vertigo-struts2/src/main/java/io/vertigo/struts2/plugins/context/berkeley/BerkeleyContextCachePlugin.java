package io.vertigo.struts2.plugins.context.berkeley;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.core.lang.Activeable;
import io.vertigo.core.lang.Assertion;
import io.vertigo.struts2.core.KActionContext;
import io.vertigo.struts2.impl.context.ContextCachePlugin;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

/**
 * Implémentation Berkeley du ContextCachePlugin.
 * La purge est assur�e par un Timer et passe toutes les Math.min(15 min, timeToLiveSeconds).
 *
 * @author pchretien, npiedeloup
 */
public final class BerkeleyContextCachePlugin implements Activeable, ContextCachePlugin {
	private static final String USER_HOME = "user.home";
	private static final String USER_DIR = "user.dir";
	private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

	private final Logger logger = Logger.getLogger(getClass());
	private final TupleBinding cacheValueBinding;
	private final TupleBinding keyBinding = TupleBinding.getPrimitiveBinding(String.class);

	private Timer purgeTimer;
	private final long timeToLiveSeconds;
	private Database cacheDatas;

	private final File myCacheEnvPath;
	private Environment myEnv;

	/**
	 * Constructeur.
	 * @param codecManager Manager des mécanismes de codage/décodage. 
	 * @param cachePath Chemin de stockage
	 * @param timeToLiveSeconds Durée de vie des éléments en seconde
	 */
	@Inject
	public BerkeleyContextCachePlugin(final CodecManager codecManager, final @Named("cachePath") String cachePath, final @Named("timeToLiveSeconds") int timeToLiveSeconds) {
		Assertion.checkNotNull(codecManager);
		//---------------------------------------------------------------------
		this.timeToLiveSeconds = timeToLiveSeconds;
		final String translatedCachePath = translatePath(cachePath);
		myCacheEnvPath = new File(translatedCachePath);
		myCacheEnvPath.mkdirs();
		Assertion.checkState(myCacheEnvPath.canWrite(), "L'espace de stockage du cache n'est pas accessible ({0})", myCacheEnvPath.getAbsolutePath());

		cacheValueBinding = new CacheValueBinding(new SerializableBinding(codecManager.getCompressedSerializationCodec()));
	}

	private static String translatePath(final String path) {
		String translatedPath = path.replaceAll(USER_HOME, System.getProperty(USER_HOME).replace('\\', '/'));
		translatedPath = translatedPath.replaceAll(USER_DIR, System.getProperty(USER_DIR).replace('\\', '/'));
		translatedPath = translatedPath.replaceAll(JAVA_IO_TMPDIR, System.getProperty(JAVA_IO_TMPDIR).replace('\\', '/'));
		return translatedPath;
	}

	/** {@inheritDoc} */
	@Override
	public void put(final KActionContext context) {
		Assertion.checkNotNull(context);
		//---------------------------------------------------------------------
		//totalPuts++;
		try {
			final Transaction transaction = createTransaction();
			boolean committed = false;
			try {
				final DatabaseEntry theKey = new DatabaseEntry();
				keyBinding.objectToEntry(context.getId(), theKey);
				final DatabaseEntry theData = new DatabaseEntry();
				cacheValueBinding.objectToEntry(new CacheValue(context), theData);

				final OperationStatus status = cacheDatas.put(transaction, theKey, theData);
				if (!OperationStatus.SUCCESS.equals(status)) {
					throw new DatabaseException("la sauvegarde a �chou�e") { //DatabaseException est abstract, mais aucun fils ne semble correct
						private static final long serialVersionUID = -2201117970033615366L;
					};
				}
				transaction.commit();
				committed = true;
			} finally {
				if (!committed) {
					transaction.abort();
				}
			}
		} catch (final DatabaseException e) {
			throw new RuntimeException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public KActionContext get(final String key) {
		//totalCalls++;
		try {
			final DatabaseEntry theKey = new DatabaseEntry();
			keyBinding.objectToEntry(key, theKey);
			final DatabaseEntry theData = new DatabaseEntry();
			final OperationStatus status = cacheDatas.get(null, theKey, theData, null);
			if (OperationStatus.SUCCESS.equals(status)) {
				final CacheValue cacheValue = readCacheValueSafely(theKey, theData);
				if (cacheValue != null && !isTooOld(cacheValue)) { //null si erreur de lecture
					//totalHits++;
					return (KActionContext) cacheValue.getValue();
				}
				cacheDatas.delete(null, theKey);
			}
		} catch (final DatabaseException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private CacheValue readCacheValueSafely(final DatabaseEntry theKey, final DatabaseEntry theData) {
		String key = "IdError";
		try {
			key = (String) keyBinding.entryToObject(theKey);
			final CacheValue cacheValue = (CacheValue) cacheValueBinding.entryToObject(theData);
			return cacheValue;
		} catch (final RuntimeException e) {
			logger.warn("Erreur de lecture du ContextCache : suppression de l'entrée incrimin�e : " + key, e);
			cacheDatas.delete(null, theKey);
		}
		return null;
	}

	private boolean isTooOld(final CacheValue cacheValue) {
		return System.currentTimeMillis() - cacheValue.getCreateTime() >= timeToLiveSeconds * 1000;
	}

	private Transaction createTransaction() throws DatabaseException {
		return cacheDatas.getEnvironment().beginTransaction(null, null);
	}

	/**
	 * Purge les elements trop vieux.
	 * @throws DatabaseException Si erreur
	 */
	void removeTooOldElements() throws DatabaseException {
		final DatabaseEntry foundKey = new DatabaseEntry();
		final DatabaseEntry foundData = new DatabaseEntry();
		Cursor cursor = null;
		try {
			cursor = cacheDatas.openCursor(null, null);
			final int maxChecked = 500;
			int checked = 0;
			//Les elements sont parcouru dans l'ordre d'insertion (sans lock)
			//dés qu'on en trouve un trop récent, on stop
			while (checked < maxChecked && cursor.getNext(foundKey, foundData, LockMode.READ_UNCOMMITTED) == OperationStatus.SUCCESS) {
				final CacheValue cacheValue = readCacheValueSafely(foundKey, foundData);
				if (cacheValue == null || isTooOld(cacheValue)) {//null si erreur de lecture
					cacheDatas.delete(null, foundKey);
					checked++;
				} else {
					break;
				}
			}
			logger.info("purge " + checked + " elements");
		} finally {
			if (cursor != null) {
				cursor.close();
			}

		}
	}

	/** {@inheritDoc} */
	@Override
	public void start() {
		try {
			myEnv = createDbEnv();
			cacheDatas = createDb();
		} catch (final DatabaseException e) {
			throw new RuntimeException(e);
		}

		final long purgePeriod = Math.min(15 * 60 * 1000, timeToLiveSeconds * 1000);
		purgeTimer = new Timer("PurgeContextCache", true);
		purgeTimer.schedule(new TimerTask() {
			private final Logger timerLogger = Logger.getLogger(getClass());

			@Override
			public void run() {
				try {
					removeTooOldElements();
				} catch (final DatabaseException dbe) {
					timerLogger.error("Error closing BerkeleyContextCachePlugin: " + dbe.toString(), dbe);
				}
			}
		}, purgePeriod, purgePeriod);
	}

	/** {@inheritDoc} */
	@Override
	public void stop() {
		purgeTimer.cancel();
		if (myEnv != null) {
			try {
				cacheDatas.close();
				// Finally, close the environment.
				myEnv.close();
			} catch (final DatabaseException dbe) {
				logger.error("Error closing BerkeleyContextCachePlugin: " + dbe.toString(), dbe);
			}
		}
	}

	private Environment createDbEnv() throws DatabaseException {
		final EnvironmentConfig myEnvConfig = new EnvironmentConfig();
		myEnvConfig.setReadOnly(false);
		myEnvConfig.setAllowCreate(true);
		myEnvConfig.setTransactional(true);
		//On limite l'utilisation du cache à 20% de la mémoire globale.
		myEnvConfig.setCachePercent(20);
		// Open the environment
		return new Environment(myCacheEnvPath, myEnvConfig);
	}

	private Database createDb() {
		final DatabaseConfig myDbConfig = new DatabaseConfig();
		myDbConfig.setReadOnly(false);
		myDbConfig.setAllowCreate(true);
		myDbConfig.setTransactional(true);
		final Database db;
		try {
			db = myEnv.openDatabase(null, "KActionContext", myDbConfig);
		} catch (final DatabaseException e) {
			throw new RuntimeException(e);
		}
		return db;
	}

}