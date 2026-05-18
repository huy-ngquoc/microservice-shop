package vn.edu.uit.msshop.shared.observability.tracing;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.observability.ContextProviderFactory;
import org.springframework.data.mongodb.observability.MongoObservationCommandListener;

import io.micrometer.observation.ObservationRegistry;

// Spring Boot's MicrometerTracingAutoConfiguration wires HTTP server spans,
// Feign client spans, Lettuce/Redis spans, Brave Tracing with MDC decorator
// and the AsyncZipkinSpanHandler when spring-boot-micrometer-tracing +
// micrometer-tracing-bridge-brave + zipkin-reporter-brave are on the
// classpath. MongoDB driver observation is the only piece Boot does NOT
// auto-wire — we plug a CommandListener into MongoClientSettings and pass an
// observation ContextProvider so each Mongo command becomes a child span of
// the active request span.
//
// Conditional on the Mongo support classes so non-Mongo services that pull
// shared-observability never trigger class loading of MongoClient* types.
@AutoConfiguration
@ConditionalOnClass({
        MongoClientSettingsBuilderCustomizer.class,
        MongoObservationCommandListener.class
})
public class MongoTracingAutoConfiguration {

    @Bean
    MongoClientSettingsBuilderCustomizer mongoObservationCustomizer(
            final ObservationRegistry registry) {
        return clientSettingsBuilder -> clientSettingsBuilder
                .contextProvider(ContextProviderFactory.create(registry))
                .addCommandListener(new MongoObservationCommandListener(registry));
    }
}
