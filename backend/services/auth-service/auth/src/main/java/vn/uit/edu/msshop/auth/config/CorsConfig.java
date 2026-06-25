package vn.uit.edu.msshop.auth.config; // Thay đổi package cho đúng với dự án của bạn

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // Đảm bảo bộ lọc CORS này chạy ĐẦU TIÊN, trước cả Spring Security
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Cho phép các Origin của bạn
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:5500"));
        
        // Cho phép đầy đủ các phương thức bao gồm cả OPTIONS (Preflight)
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Cho phép tất cả các Header từ Client gửi lên
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        
        // Cho phép gửi kèm Credentials (Cookie, Token,...)
        corsConfig.setAllowCredentials(true);
        
        // Thời gian cache kết quả Preflight
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}