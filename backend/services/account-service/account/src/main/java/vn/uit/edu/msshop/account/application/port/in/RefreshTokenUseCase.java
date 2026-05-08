package vn.uit.edu.msshop.account.application.port.in;

import vn.uit.edu.msshop.account.adapter.in.web.response.LoginResponse;

public interface RefreshTokenUseCase {
    public LoginResponse refreshToken(String refreshToken);
}
