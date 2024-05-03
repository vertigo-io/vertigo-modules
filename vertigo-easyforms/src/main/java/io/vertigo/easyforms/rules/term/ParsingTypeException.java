package io.vertigo.easyforms.rules.term;

public class ParsingTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParsingTypeException(final String message) {
		super(message);
	}

	public ParsingTypeException(final String message, final Object o1, final Object o2, final String operator) {
		super(message + " : " + buildMessageDetail(o1, o2, operator));
	}

	private static String buildMessageDetail(final Object o1, final Object o2, final String operator) {
		return "'" + o1 + "' (" + o1.getClass().getSimpleName() + ") " + operator + " '" + o2 + "' (" + o2.getClass().getSimpleName() + ")";
	}

}
