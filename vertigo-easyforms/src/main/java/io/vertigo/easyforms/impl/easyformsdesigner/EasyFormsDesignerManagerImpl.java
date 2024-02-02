package io.vertigo.easyforms.impl.easyformsdesigner;

import javax.inject.Inject;

import io.vertigo.core.locale.LocaleManager;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.easyforms.easyformsdesigner.EasyFormsDesignerManager;

public class EasyFormsDesignerManagerImpl implements EasyFormsDesignerManager, Activeable {

	private final LocaleManager localeManager;

	@Inject
	public EasyFormsDesignerManagerImpl(final LocaleManager localeManager) {
		this.localeManager = localeManager;
	}

	@Override
	public void start() {
		localeManager.add("io.vertigo.easyforms.easyformsdesigner.Resources", Resources.values());
	}

	@Override
	public void stop() {
		// Nothing
	}

}
