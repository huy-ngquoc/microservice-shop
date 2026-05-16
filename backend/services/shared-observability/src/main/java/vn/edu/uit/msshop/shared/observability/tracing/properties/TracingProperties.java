package vn.edu.uit.msshop.shared.observability.tracing.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "app.observability.tracing")
public record TracingProperties(
        String serviceName,
        double samplingProbability,
        String zipkinEndpoint) {

    public TracingProperties {
        if ((samplingProbability < 0D)
                || (samplingProbability > 1D)) {
            throw new IllegalArgumentException(
                    "samplingProbability must be in [0,1], got " + samplingProbability);
        }
    }
}
