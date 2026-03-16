package vn.uit.edu.msshop.cart.application.service;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import vn.uit.edu.msshop.cart.application.port.in.CheckPermissionUseCase;

@Service
public class CheckPermissionService implements CheckPermissionUseCase {
    private static final String ROLE_USER = "ROLE_client_user";
    private static final String ROLE_ADMIN= "ADMIN_client_user";
    @Override
    public boolean isSameUser(String userIdHeader, String userIdFromCart) {
        return userIdHeader.equals(userIdFromCart);
    }

    @Override
    public boolean isUser(String userRole) {
        String[] roles = userRole.split(",");
        return Arrays.asList(roles).contains(ROLE_USER);
        
    }

    @Override
    public boolean isAdmin(String userRole) {
        String[] roles = userRole.split(",");
        return Arrays.asList(roles).contains(ROLE_ADMIN);
    }

}
