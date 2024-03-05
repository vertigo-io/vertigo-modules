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
