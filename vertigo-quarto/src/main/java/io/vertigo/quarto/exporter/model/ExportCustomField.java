package io.vertigo.quarto.exporter.model;

import java.util.function.Function;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.datamodel.data.definitions.DataField;
import io.vertigo.datamodel.data.model.DataObject;

public class ExportCustomField extends ExportField {

	private Function<DataObject, String> getter;

	/**
	 * 
	 * @param dtField is mandatory for super class ExportField, provide any field as
	 *                it won't be used
	 * @param getter
	 * @param label
	 */
	public ExportCustomField(final DataField dtField, final Function<DataObject, String> getter,
			final LocaleMessageText label) {
		super(dtField, label);
		Assertion.check().isNotNull(getter);
		this.getter = getter;
	}

	/**
	 * Get value from DataObject
	 * 
	 * @param dto
	 * @return
	 */
	public String apply(DataObject dto) {
		return getter.apply(dto);
	}

}
