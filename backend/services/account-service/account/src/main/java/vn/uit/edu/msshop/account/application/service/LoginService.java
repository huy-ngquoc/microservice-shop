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
import vn.uit.edu.msshop.account.adapter.in.web.request.LoginRequest;
import vn.uit.edu.msshop.account.adapter.in.web.response.LoginResponse;
import vn.uit.edu.msshop.account.application.port.in.LoginUseCase;
import vn.uit.edu.msshop.account.application.port.out.LoadAccountPort;
import vn.uit.edu.msshop.account.domain.model.Account;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {
    @Value("${app.keycloak.admin.token-uri}")
    private String tokenUri;

    @Value("${app.keycloak.admin.client-id}")
    private String clientId;

    @Value("${app.keycloak.admin.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final LoadAccountPort loadPort;
    

    @Override
    public LoginResponse login(LoginRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", request.getUsername());
        map.add("password", request.getPassword());
        map.add("scope", "openid");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        try {
            
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(tokenUri,httpEntity, LoginResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            Account account = loadPort.loadByUsername(new AccountName(request.getUsername()));
            if(account==null) {
                throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không chính xác");
            }
            
            if(account.getStatus().value().equals("PENDING")){
                throw new RuntimeException("Tài khoản của bạn đang được xử lý, vui lòng đợi một lúc và thử lại");
            }

            throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không chính xác");
            
        }

    }
    
}
