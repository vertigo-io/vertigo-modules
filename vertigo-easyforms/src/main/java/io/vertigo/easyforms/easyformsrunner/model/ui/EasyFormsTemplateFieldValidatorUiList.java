package io.vertigo.easyforms.easyformsrunner.model.ui;

import java.util.ArrayList;
import java.util.Collection;

import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.util.VCollectors;
import io.vertigo.easyforms.domain.EasyFormsTemplateFieldValidatorUi;

public class EasyFormsTemplateFieldValidatorUiList extends ArrayList<EasyFormsTemplateFieldValidatorUi> {

	private static final long serialVersionUID = 1L;

	public EasyFormsTemplateFieldValidatorUiList() {
		super();
	}

	public EasyFormsTemplateFieldValidatorUiList(final Collection<? extends EasyFormsTemplateFieldValidatorUi> c) {
		super(c);
	}

	public EasyFormsTemplateFieldValidatorUiList(final int initialCapacity) {
		super(initialCapacity);
	}

	public DtList<EasyFormsTemplateFieldValidatorUi> toDtList() {
		return stream().collect(VCollectors.toDtList(EasyFormsTemplateFieldValidatorUi.class));
	}

}
