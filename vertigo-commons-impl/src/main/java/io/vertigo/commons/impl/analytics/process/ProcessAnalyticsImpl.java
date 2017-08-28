package io.vertigo.commons.impl.analytics.process;

import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

import io.vertigo.commons.analytics.process.ProcessAnalyticsTracer;
import io.vertigo.lang.Assertion;

public class ProcessAnalyticsImpl {

	/**
	 * Processus binde sur le thread courant. Le processus , recoit les notifications des sondes placees dans le code de
	 * l'application pendant le traitement d'une requete (thread).
	 */
	private static final ThreadLocal<Stack<ProcessAnalyticsTracerImpl>> THREAD_LOCAL_PROCESS = new ThreadLocal<>();

	public void trace(final String category, final String name, final Consumer<ProcessAnalyticsTracer> consumer, final Consumer<AProcess> onCloseConsumer) {
		try (ProcessAnalyticsTracerImpl tracer = createTracer(category, name, onCloseConsumer)) {
			try {
				consumer.accept(tracer);
				tracer.markAsSucceeded();
			} catch (final Exception e) {
				tracer.markAsFailed(e);
				throw e;
			}
		}
	}

	public <O> O traceWithReturn(final String category, final String name, final Function<ProcessAnalyticsTracer, O> function, final Consumer<AProcess> onCloseConsumer) {
		try (ProcessAnalyticsTracerImpl tracer = createTracer(category, name, onCloseConsumer)) {
			try {
				final O result = function.apply(tracer);
				tracer.markAsSucceeded();
				return result;
			} catch (final Exception e) {
				tracer.markAsFailed(e);
				throw e;
			}
		}
	}

	public Optional<ProcessAnalyticsTracer> getCurrentTracer() {
		// When collect feature is enabled
		return doGetCurrentTracer().map(a -> a); // convert impl to api
	}

	public void removeTracer() {
		THREAD_LOCAL_PROCESS.get().pop();
	}

	private static Optional<ProcessAnalyticsTracerImpl> doGetCurrentTracer() {
		if (THREAD_LOCAL_PROCESS.get() == null || THREAD_LOCAL_PROCESS.get().isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(THREAD_LOCAL_PROCESS.get().peek());
	}

	private static void push(final ProcessAnalyticsTracerImpl analyticstracer) {
		Assertion.checkNotNull(analyticstracer);
		//---
		if (THREAD_LOCAL_PROCESS.get() == null) {
			THREAD_LOCAL_PROCESS.set(new Stack<>());
		}
		Assertion.checkState(THREAD_LOCAL_PROCESS.get().size() < 100, "More than 100 process deep. All processes must be closed.");
		THREAD_LOCAL_PROCESS.get().push(analyticstracer);
	}

	private static ProcessAnalyticsTracerImpl createTracer(final String category, final String name, final Consumer<AProcess> onCloseConsumer) {
		final Optional<ProcessAnalyticsTracerImpl> parent = doGetCurrentTracer();
		final ProcessAnalyticsTracerImpl analyticsTracer = new ProcessAnalyticsTracerImpl(parent, category, name, onCloseConsumer);
		push(analyticsTracer);
		return analyticsTracer;
	}

}
