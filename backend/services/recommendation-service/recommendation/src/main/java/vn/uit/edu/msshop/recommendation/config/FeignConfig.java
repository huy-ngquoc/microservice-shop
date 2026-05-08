package vn.uit.edu.msshop.recommendation.config;

import java.lang.System.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.ALL; // Log toàn bộ header và body
    }
}