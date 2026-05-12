package vn.uit.edu.msshop.order.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;

import feign.Request;
import feign.Retryer;

public class PaymentFeignConfig {
    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
    @Bean
    public Request.Options options() {
        // connectTimeout: 2s, readTimeout: 2s
        return new Request.Options(
            5000, TimeUnit.MILLISECONDS, 
            5000, TimeUnit.MILLISECONDS, 
            true
        );
    }

    @Bean
    public Retryer feignRetryer() {
        // Tắt retry tuyệt đối ở tầng Feign
        return Retryer.NEVER_RETRY;
    }
}
