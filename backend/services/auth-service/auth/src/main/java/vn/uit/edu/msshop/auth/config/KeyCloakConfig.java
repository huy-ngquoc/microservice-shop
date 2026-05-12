package vn.uit.edu.msshop.auth.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class KeyCloakConfig {
    @Value("${app.keycloak.admin.server-url}") private String serverUrl;
    @Value("${app.keycloak.admin.realm}") private String realm;
    @Value("${app.keycloak.admin.client-id}") private String clientId;
    @Value("${app.keycloak.admin.client-secret}") private String clientSecret;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") private String issuerUri;

    @Bean
    public Keycloak keycloak() {
        System.out.println("Issuer uri" +issuerUri);
        System.out.println("Realmmmm========================" +realm);
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("ms_shop") 
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
