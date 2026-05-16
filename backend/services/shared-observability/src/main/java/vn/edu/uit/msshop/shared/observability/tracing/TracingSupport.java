package vn.edu.uit.msshop.shared.observability.tracing;

import org.springframework.data.mongodb.observability.ContextProviderFactory;
import org.springframework.data.mongodb.observability.MongoObservationCommandListener;

import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import vn.edu.uit.msshop.shared.observability.tracing.properties.TracingProperties;
import zipkin2.reporter.BytesMessageSender;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

public final class TracingSupport {
    private TracingSupport() {
    }

    public static Tracing braveTracing(
            final TracingProperties props) {
        final BytesMessageSender sender = OkHttpSender.create(props.zipkinEndpoint());
        final var sampler = Sampler.create((float) props.samplingProbability());
        final var spanHandler = AsyncZipkinSpanHandler.create(sender);

        // MDCScopeDecorator populates SLF4J MDC with traceId/spanId on every
        // scope enter so log appender pattern can render them.
        final var currentTraceContext = ThreadLocalCurrentTraceContext.newBuilder()
                .addScopeDecorator(MDCScopeDecorator.get())
                .build();

        return Tracing.newBuilder()
                .localServiceName(props.serviceName())
                .sampler(sampler)
                .currentTraceContext(currentTraceContext)
                .addSpanHandler(spanHandler)
                .build();
    }

    public static Tracer bridgeTracer(
            final Tracing tracing) {
        final var bridgeContext = new BraveCurrentTraceContext(tracing.currentTraceContext());
        final var baggageManager = new BraveBaggageManager();
        return new BraveTracer(
                tracing.tracer(),
                bridgeContext,
                baggageManager);
    }

    // Bridges Micrometer Observation events into Tracer spans. Spring Boot 4
    // does not ship a tracing autoconfigure module that wires this, so each
    // service must register it explicitly.
    public static ObservationHandler<Observation.Context> tracingObservationHandler(
            final Tracer tracer) {
        return new DefaultTracingObservationHandler(tracer);
    }

    public static MongoClientSettingsBuilderCustomizer mongoObservationCustomizer(
            final ObservationRegistry registry) {
        final var commandListener = new MongoObservationCommandListener(registry);
        // contextProvider propagates the current Observation across MongoClient's
        // command pipeline so each Mongo command becomes a child span of the
        // active request span. Without it the listener fires but spans are
        // detached from the parent trace (root-only HTTP span in Zipkin).
        final var contextProvider = ContextProviderFactory.create(registry);

        return clientSettingsBuilder -> clientSettingsBuilder
                .contextProvider(contextProvider)
                .addCommandListener(commandListener);
    }
}
