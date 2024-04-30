package io.vertigo.easyforms.rules;

import java.util.List;

public enum Brackets implements ITermRule {
	OPEN("("),
	CLOSE(")");

	private final String str;

	Brackets(final String str) {
		this.str = str;
	}

	@Override
	public List<String> getStrValues() {
		return List.of(str);
	}

}
