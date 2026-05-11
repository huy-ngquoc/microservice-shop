package vn.uit.edu.msshop.inventory.application.service;

import org.springframework.stereotype.Service;

import vn.uit.edu.msshop.inventory.application.port.in.CheckPermissionUseCase;

@Service
public class CheckPermissionService implements CheckPermissionUseCase {
    private static final String ROLE_USER = "ROLE_Client_User";
    private static final String ROLE_ADMIN = "ROLE_Client_Admin";

    @Override
    public boolean isSameUser(
            String userIdHeader,
            String userIdFromOrder) {
        return true;
        /* return userIdHeader.equalsIgnoreCase(userIdFromOrder); */
    }

    @Override
    public boolean isUser(
            String userRole) {
        return true;
        /*
         * String[] roles = userRole.split(",");
         * return Arrays.asList(roles).contains(ROLE_USER);
         */

    }

    @Override
    public boolean isAdmin(
            String userRole) {
        return true;
        /*
         * String[] roles = userRole.split(",");
         * return Arrays.asList(roles).contains(ROLE_ADMIN);
         */
    }

}
