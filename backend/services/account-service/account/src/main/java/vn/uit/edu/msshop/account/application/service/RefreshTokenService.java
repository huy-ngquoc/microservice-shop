package vn.uit.edu.msshop.account.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.adapter.in.web.response.LoginResponse;
import vn.uit.edu.msshop.account.application.port.in.RefreshTokenUseCase;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {
    @Value("${app.keycloak.admin.token-uri}")
    private String tokenUri;

    @Value("${app.keycloak.admin.client-id}")
    private String clientId;

    @Value("${app.keycloak.admin.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("grant_type", "refresh_token");
    map.add("refresh_token", refreshToken);
    map.add("client_id", clientId);
    map.add("client_secret", clientSecret);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    try {
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(tokenUri, request, LoginResponse.class);
        return response.getBody();
    } catch (HttpClientErrorException e) {
        throw new RuntimeException("Refresh token không hợp lệ hoặc đã hết hạn");
    }
    }
    

}
