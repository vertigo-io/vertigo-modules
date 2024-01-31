package io.vertigo.easyforms.easyformsrunner.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.vertigo.core.lang.VSystemException;

public record EasyFormsListItem(
		String value,
		String label) implements Serializable {

	@Override
	public String toString() {
		if (value == null) {
			return "{value:null, label:'" + label + "'}";
		}
		return "{value:'" + value + "', label:'" + label + "'}";
	}

	public static List<EasyFormsListItem> ofCollection(final Object o) {
		if (o instanceof final Collection<?> list) {
			return list.stream()
					.map(EasyFormsListItem::of)
					.filter(Objects::nonNull)
					.toList();
		}
		throw new VSystemException("Argument is not a collection");
	}

	public static EasyFormsListItem of(final Object o) {
		if (o instanceof final EasyFormsListItem easyFormsListItem) {
			return easyFormsListItem;
		} else if (o instanceof final Map<?, ?> map) {
			if (map.isEmpty()) {
				return null;
			} else if (map.size() == 1) {
				final var entry = map.entrySet().iterator().next();
				return new EasyFormsListItem(entry.getKey().toString(), entry.getValue().toString());
			} else if (map.size() == 2 && map.containsKey("label") && map.containsKey("value")) {
				return new EasyFormsListItem(map.get("value").toString(), map.get("label").toString());
			}
		}
		throw new VSystemException("Argument is not a valid representation of EasyFormsListItem.");
	}

}
