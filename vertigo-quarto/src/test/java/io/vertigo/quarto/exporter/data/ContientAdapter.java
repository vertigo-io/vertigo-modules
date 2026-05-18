package io.vertigo.quarto.exporter.data;

import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;
import io.vertigo.quarto.exporter.data.domain.Continent;

public final class ContientAdapter implements BasicTypeAdapter<Continent, String> {

	@Override
	public String toBasic(final Continent data) {
		return data.getName();
	}

	@Override
	public Continent toJava(final String basicValue, final Class<Continent> dtClass) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public BasicType getBasicType() {
		return BasicType.String;
	}
}
