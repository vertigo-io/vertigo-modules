package io.vertigo.easyforms.rules.term;

import java.util.List;

import io.vertigo.easyforms.rules.ITermRule;

public enum BracketsTerm implements ITermRule {
	OPEN("("),
	CLOSE(")");

	private final String str;

	BracketsTerm(final String str) {
		this.str = str;
	}

	@Override
	public List<String> getStrValues() {
		return List.of(str);
	}

}
