package vn.uit.edu.msshop.account.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String id_token;
    private String token_type;
}
