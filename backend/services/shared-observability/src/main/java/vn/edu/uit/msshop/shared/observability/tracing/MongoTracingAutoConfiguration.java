package vn.edu.uit.msshop.shared.observability.tracing;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.observability.ContextProviderFactory;
import org.springframework.data.mongodb.observability.MongoObservationCommandListener;

import io.micrometer.observation.ObservationRegistry;

// Responsibility split inside this module:
//   - TracingAutoConfiguration: real brave.Tracing, Micrometer Tracer bridge,
//     Propagator (B3 extract/inject), MDC scope decorator, AsyncZipkinSpan
//     Handler — none of which Spring Boot 4 modular auto-wires by itself.
//   - Spring Boot's MicrometerTracingAutoConfiguration on top consumes those
//     beans to register the DefaultTracingObservationHandler + propagating
//     receiver/sender handlers that turn HTTP / Feign / Lettuce observation
//     events into Brave spans.
//   - This class: plug a Mongo CommandListener into MongoClientSettings and
//     pass an observation ContextProvider so each Mongo command emits a
//     child span of the active request span. Boot has no equivalent for
//     Mongo driver observation, so we add it here.
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
