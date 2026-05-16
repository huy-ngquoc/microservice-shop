package vn.edu.uit.msshop.product.bootstrap.config.observability;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.Tracing;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import vn.edu.uit.msshop.shared.observability.tracing.TracingSupport;
import vn.edu.uit.msshop.shared.observability.tracing.properties.TracingProperties;

@Configuration
@EnableConfigurationProperties(TracingProperties.class)
public class TracingConfig {
    @Bean
    Tracing braveTracing(
            final TracingProperties props) {
        return TracingSupport.braveTracing(props);
    }

    @Bean
    Tracer tracer(
            final Tracing tracing) {
        return TracingSupport.bridgeTracer(tracing);
    }

    @Bean
    ObservationHandler<Observation.Context> tracingObservationHandler(
            final Tracer tracer) {
        return TracingSupport.tracingObservationHandler(tracer);
    }

    @Bean
    MongoClientSettingsBuilderCustomizer mongoObservationCustomizer(
            final ObservationRegistry registry) {
        return TracingSupport.mongoObservationCustomizer(registry);
    }
}
