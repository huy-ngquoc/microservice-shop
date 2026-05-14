package vn.uit.edu.msshop.order.bootstrap.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;

import feign.Request;
import feign.Retryer;

public class FeignConfig {
    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

    @Bean
    public Request.Options options() {
        // connectTimeout: 2s, readTimeout: 2s
        return new Request.Options(
                2000, TimeUnit.MILLISECONDS,
                2000, TimeUnit.MILLISECONDS,
                true);
    }

    @Bean
    public Retryer feignRetryer() {
        // Tắt retry tuyệt đối ở tầng Feign
        return Retryer.NEVER_RETRY;
    }
}
