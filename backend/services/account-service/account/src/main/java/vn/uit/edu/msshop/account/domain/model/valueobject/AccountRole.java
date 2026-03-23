package vn.uit.edu.msshop.account.domain.model.valueobject;

import java.util.Set;

public record AccountRole(String value) {
    private static final Set<String> validRole= Set.of("Client_admin","Client_user");
    public AccountRole {
        if(value==null) {
            throw new IllegalArgumentException("Invalid role");
        } 
        if(!validRole.contains(value)) {
            throw new IllegalArgumentException("Invalid role");
        }
    }
}
