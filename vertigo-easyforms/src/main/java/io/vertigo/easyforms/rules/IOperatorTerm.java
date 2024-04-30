package io.vertigo.easyforms.rules;

/**
 * Interface for all operator terms.
 *
 * @param <T> the type of operands used by this operator
 * @author skerdudou
 */
public interface IOperatorTerm<T> extends ITermRule {

	/**
	 * Get the priority of the operator. Higher is the priority, earlier the operator is executed.
	 *
	 * @return the priority of the operator
	 */
	int getPriority();

	T apply(T left, T right);

}
