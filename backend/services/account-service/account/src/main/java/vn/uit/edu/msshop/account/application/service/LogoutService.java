package vn.uit.edu.msshop.account.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.application.port.in.LogoutUseCase;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {
    @Value("${app.keycloak.admin.token-uri}")
    private String tokenUri;

    @Value("${app.keycloak.admin.client-id}")
    private String clientId;

    @Value("${app.keycloak.admin.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void logout(String refreshToken) {
        String logoutUri = tokenUri.replace("/token", "/logout"); 

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", clientId);
    map.add("client_secret", clientSecret);
    map.add("refresh_token", refreshToken);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    try {
        restTemplate.postForEntity(logoutUri, request, String.class);
        
    } catch (Exception e) {
        throw new RuntimeException("Đăng xuất thất bại");
    }
    }
}
