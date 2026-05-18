package vn.edu.uit.msshop.shared.observability.tracing;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.micrometer.tracing.autoconfigure.NoopTracerAutoConfiguration;
import org.springframework.context.annotation.Bean;

import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import vn.edu.uit.msshop.shared.observability.tracing.properties.TracingProperties;
import zipkin2.reporter.BytesMessageSender;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

// Spring Boot 4 modular ships spring-boot-micrometer-tracing (with
// MicrometerTracingAutoConfiguration + NoopTracerAutoConfiguration) but does
// NOT provide a BraveAutoConfiguration. Without a real brave.Tracing +
// Micrometer Tracer bean, NoopTracer wins, Zipkin gets nothing, and the
// SLF4J MDC keys traceId/spanId stay empty.
//
// This auto-config fills the gap:
//   - braveTracing: real Brave Tracing with Zipkin sender + MDC decorator
//   - tracer: Micrometer Tracer bridged over braveTracing (so Boot's
//     NoopTracerAutoConfiguration's @ConditionalOnMissingBean(Tracer.class)
//     skips itself)
//
// `beforeName` orders this before noop so our Tracer always wins.
@AutoConfiguration(
        before = NoopTracerAutoConfiguration.class)
@EnableConfigurationProperties(TracingProperties.class)
@ConditionalOnClass({ Tracing.class, BraveTracer.class })
public class TracingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    Tracing braveTracing(
            final TracingProperties props) {
        final BytesMessageSender sender = OkHttpSender.create(props.zipkinEndpoint());
        final var spanHandler = AsyncZipkinSpanHandler.create(sender);

        // MDCScopeDecorator pushes traceId/spanId into SLF4J MDC on every scope
        // enter — without it the log pattern "%X{traceId:-}" renders empty.
        final var currentTraceContext = ThreadLocalCurrentTraceContext.newBuilder()
                .addScopeDecorator(MDCScopeDecorator.get())
                .build();

        return Tracing.newBuilder()
                .localServiceName(props.serviceName())
                .sampler(Sampler.create((float) props.samplingProbability()))
                .currentTraceContext(currentTraceContext)
                .addSpanHandler(spanHandler)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    Tracer tracer(
            final Tracing tracing) {
        return new BraveTracer(
                tracing.tracer(),
                new BraveCurrentTraceContext(tracing.currentTraceContext()),
                new BraveBaggageManager());
    }
}
