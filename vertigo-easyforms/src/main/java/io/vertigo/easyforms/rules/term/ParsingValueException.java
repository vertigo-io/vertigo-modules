package io.vertigo.easyforms.rules.term;

public class ParsingValueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParsingValueException(final String message) {
		super(message);
	}

	public ParsingValueException(final String message, final Object o1, final Object o2, final String operator) {
		super(message + " : " + buildMessageDetail(o1, o2, operator));
	}

	private static String buildMessageDetail(final Object o1, final Object o2, final String operator) {
		return "'" + o1 + "' (" + o1.getClass().getSimpleName() + ") " + operator + " '" + o2 + "' (" + o2.getClass().getSimpleName() + ")";
	}

}
