package io.vertigo.easyforms.rules;

import java.util.List;

/**
 * Interface for all term based rules.
 *
 * @author skerdudou
 */
public interface ITermRule {

	/**
	 * Get all the possible string values.
	 *
	 * @return the list of string values
	 */
	List<String> getStrValues();

}
