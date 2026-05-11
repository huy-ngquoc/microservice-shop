package vn.uit.edu.msshop.inventory.bootstrap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.Tracing;
import brave.sampler.Sampler;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

@Configuration
public class TracingConfig {

    @Value("${management.zipkin.tracing.endpoint:http://localhost:9411/api/v2/spans}")
    private String zipkinEndpoint;

    @Value("${management.tracing.sampling.probability:0.1}")
    private double samplingProbability;

    @Bean
    public io.micrometer.observation.ObservationHandler<
            io.micrometer.observation.Observation.Context> tracingObservationHandler(
                    Tracer tracer) {
        return new DefaultTracingObservationHandler(tracer);
    }

    @Bean
    public Tracing tracing() {

        var sender = OkHttpSender.create(zipkinEndpoint);

        // 2. Cấu hình Handler để xử lý Span trước khi gửi
        var spanHandler = AsyncZipkinSpanHandler.create(sender);

        return Tracing.newBuilder()
                .localServiceName("order-service")
                .sampler(Sampler.create((float) samplingProbability))
                .addSpanHandler(spanHandler) // QUAN TRỌNG: Gắn Handler vào đây
                .build();

    }

    @Bean
    public io.micrometer.observation.ObservationHandler<
            io.micrometer.observation.Observation.Context> tracingObservationHandlerOther(
                    io.micrometer.tracing.Tracer tracer) {
        return new io.micrometer.tracing.handler.DefaultTracingObservationHandler(tracer);
    }

    @Bean
    public Tracer tracer(
            Tracing tracing) {
        // Lấy CurrentTraceContext từ object tracing thay vì tracer
        BraveCurrentTraceContext bridgeContext = new BraveCurrentTraceContext(tracing.currentTraceContext());

        // Khởi tạo Manager quản lý dữ liệu đính kèm
        BraveBaggageManager baggageManager = new BraveBaggageManager();

        // Khởi tạo Micrometer Tracer bridge
        // Tham số 1: dùng tracing.tracer() để lấy bản tracer lõi của Brave
        return new BraveTracer(tracing.tracer(), bridgeContext, baggageManager);
    }
}
