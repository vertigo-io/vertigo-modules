package io.vertigo.core.component.proxy.data;

import io.vertigo.core.component.Component;

/**
 * Example of a component defined as a proxy.
 * each method is marked with a specific annotation that explains what to do.
 * @author pchretien
 *
 */
public interface Aggregate extends Component {

	@AggregatorAnnotation(operation = AggregatorOperation.count)
	long count(int a);

	@AggregatorAnnotation(operation = AggregatorOperation.count)
	long count(int a, int b);

	@AggregatorAnnotation(operation = AggregatorOperation.count)
	long count(int a, int b, int c);

	@AggregatorAnnotation(operation = AggregatorOperation.max)
	int max(int a);

	@AggregatorAnnotation(operation = AggregatorOperation.max)
	int max(int a, int b);

	@AggregatorAnnotation(operation = AggregatorOperation.max)
	int max(int a, int b, int c);

	@AggregatorAnnotation(operation = AggregatorOperation.min)
	int min(int a);

	@AggregatorAnnotation(operation = AggregatorOperation.min)
	int min(int a, int b);

	@AggregatorAnnotation(operation = AggregatorOperation.min)
	int min(int a, int b, int c);

}
