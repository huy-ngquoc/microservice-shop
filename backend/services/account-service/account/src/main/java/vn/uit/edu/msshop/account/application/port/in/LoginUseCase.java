package vn.uit.edu.msshop.account.application.port.in;

import vn.uit.edu.msshop.account.adapter.in.web.request.LoginRequest;
import vn.uit.edu.msshop.account.adapter.in.web.response.LoginResponse;

public interface LoginUseCase {
    public LoginResponse login(LoginRequest request);
}
