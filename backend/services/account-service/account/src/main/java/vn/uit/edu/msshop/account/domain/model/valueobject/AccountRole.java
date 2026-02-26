package vn.edu.uit.msshop.product.domain.model.valueobject;

import java.util.Set;

public record AccountRole(String value) {
    private static final Set<String> validRole= Set.of("ADMIN","USER");
    public AccountRole {
        if(value==null) {
            throw new IllegalArgumentException("Invalid role");
        } 
        if(!validRole.contains(value)) {
            throw new IllegalArgumentException("Invalid role");
        }
    }
}
