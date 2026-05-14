package vn.uit.edu.msshop.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity 
public class SecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;
    @Value("${spring.mongodb.uri}")
	private String mongoDBUri;
    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
        System.out.println("Mongo db uri "+mongoDBUri);
    }

    @Bean
public ReactiveJwtDecoder jwtDecoder() {
    // 1. Tạo một WebClient Builder mới hoàn toàn, KHÔNG dùng cái Builder có sẵn của Spring
    WebClient webClient = WebClient.builder()
            .build(); 

    
    NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder
            .withJwkSetUri("http://ms-keycloak:8080/realms/ms_shop/protocol/openid-connect/certs")
            .webClient(webClient) 
            .build();

    return jwtDecoder;
}
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(auth -> auth
                .pathMatchers("/account/**")
                
                //.pathMatchers("/account/create")
                .permitAll()
                .pathMatchers("/products/**").permitAll()
                .pathMatchers("/variants/**").permitAll()
                .pathMatchers("/fake_payment/**").permitAll()
                //.pathMatchers("/order/**").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(it -> Mono.just(jwtAuthConverter.convert(it))))
            );

        return http.build();
    }
}