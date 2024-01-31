package io.vertigo.easyforms.impl.easyformsrunner;

import javax.inject.Inject;

import io.vertigo.core.locale.LocaleManager;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.easyforms.easyformsrunner.EasyFormsRunnerManager;
import io.vertigo.easyforms.impl.easyformsrunner.library.EfLibraryResources;

public class EasyFormsRunnerManagerImpl implements EasyFormsRunnerManager, Activeable {

	private final LocaleManager localeManager;

	@Inject
	public EasyFormsRunnerManagerImpl(final LocaleManager localeManager) {
		this.localeManager = localeManager;
	}

	@Override
	public void start() {
		localeManager.add("io.vertigo.easyforms.impl.easyformsrunner.library.EfLibraryResources", EfLibraryResources.values());
	}

	@Override
	public void stop() {
		// Nothing
	}

}
