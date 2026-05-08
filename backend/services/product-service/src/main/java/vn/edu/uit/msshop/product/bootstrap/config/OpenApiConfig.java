package vn.edu.uit.msshop.product.bootstrap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    OpenAPI customOpenAPI() {
        final var info = new Info()
                .title("Microservice shop - Product service")
                .version("v1");
        final var securityRequirement = new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME);
        final var securityScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        final var components = new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
