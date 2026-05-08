package vn.uit.edu.msshop.account.adapter.out.keycloak;

import java.util.Collections;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.application.port.out.CreateKeyCloakAccountPort;

@Component
@RequiredArgsConstructor
public class KeyCloakAdapter implements CreateKeyCloakAccountPort {

     private final Keycloak keycloak;
    private static final String REALM="ms_shop";
    @Value("${app.keycloak.admin.client-uuid}")
    private String clientUuid;
    @Override
    public Response createAccount(UserRepresentation user, String role) {
        System.out.println("--- START: " + role + " ---");
    Response response = null;

    try {
        // 1. Tạo User
        response = keycloak.realm(REALM).users().create(user);
        
        // 2. Lấy ID (Nếu bạn không set ID trước, bạn phải lấy từ Header của Response)
        String actualUserId = CreatedResponseUtil.getCreatedId(response);
        if (actualUserId == null) {
            // Cách lấy ID từ Keycloak Response nếu bạn không tự generate
            String path = response.getLocation().getPath();
            actualUserId = path.substring(path.lastIndexOf('/') + 1);
        }

        // Dòng DEBUG bây giờ đã an toàn
        System.out.println("DEBUG: Đang gán Role cho User ID: " + actualUserId + " tại Client: " + clientUuid);

        // 3. Lấy Role và Gán
        RoleRepresentation roleToAssign = keycloak.realm(REALM)
                .clients()
                .get(clientUuid)
                .roles()
                .get(role)
                .toRepresentation();

        keycloak.realm(REALM)
                .users()
                .get(actualUserId)
                .roles()
                .clientLevel(clientUuid)
                .add(Collections.singletonList(roleToAssign));

        System.out.println("SUCCESS: Hoàn tất luồng Keycloak cho " + user.getUsername());

    } catch (Exception e) {
        System.err.println("!!! LỖI TỔNG THỂ !!!");
        throw new RuntimeException(e.getMessage());
    }

    return response;
    }

}
