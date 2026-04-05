package vn.uit.edu.msshop.order.config;

import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.observability.MongoObservationCommandListener;

import brave.Tracing;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;




@Configuration
public class TracingConfig {
    /*@Bean
    ObservationRegistryPostProcessor observationRegistryPostProcessor(ObjectProvider<ObservationHandler<?>> handlers) {
        return (registry) -> handlers.forEach(registry.observationConfig()::observationHandler);
    }*/
   @Bean
    public io.micrometer.observation.ObservationHandler<io.micrometer.observation.Observation.Context> tracingObservationHandler(Tracer tracer) {
        return new DefaultTracingObservationHandler(tracer);
    }
    
   @Bean
    public Tracing tracing() {
        
   
        // Dùng URLConnectionSender thay cho OkHttpSender
        var sender = OkHttpSender.create("http://localhost:9411/api/v2/spans");

        // 2. Cấu hình Handler để xử lý Span trước khi gửi
        var spanHandler = AsyncZipkinSpanHandler.create(sender);

        return Tracing.newBuilder()
                .localServiceName("order-service")
                .addSpanHandler(spanHandler) // QUAN TRỌNG: Gắn Handler vào đây
                .build();
    
    }
    @Bean
public MongoClientSettingsBuilderCustomizer mongoObservationCustomizer(ObservationRegistry observationRegistry) {
    return clientSettingsBuilder -> clientSettingsBuilder
            .addCommandListener(new MongoObservationCommandListener(observationRegistry));
}
   
@Bean
public io.micrometer.observation.ObservationHandler<io.micrometer.observation.Observation.Context> tracingObservationHandlerOther(io.micrometer.tracing.Tracer tracer) {
    return new io.micrometer.tracing.handler.DefaultTracingObservationHandler(tracer);
}
   @Bean
    public Tracer tracer(Tracing tracing) {
        // Lấy CurrentTraceContext từ object tracing thay vì tracer
        BraveCurrentTraceContext bridgeContext = new BraveCurrentTraceContext(tracing.currentTraceContext());
        
        // Khởi tạo Manager quản lý dữ liệu đính kèm
        BraveBaggageManager baggageManager = new BraveBaggageManager();

        // Khởi tạo Micrometer Tracer bridge
        // Tham số 1: dùng tracing.tracer() để lấy bản tracer lõi của Brave
        return new BraveTracer(tracing.tracer(), bridgeContext, baggageManager);
    }
}
