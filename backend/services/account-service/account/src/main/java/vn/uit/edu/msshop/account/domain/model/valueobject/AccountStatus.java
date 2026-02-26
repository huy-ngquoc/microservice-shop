package vn.edu.uit.msshop.product.domain.model.valueobject;

import java.util.Set;

public record AccountStatus(String value) {
    private static Set<String> validStatus=Set.of("ACTIVE","BANNED");
    public AccountStatus {
        if(value==null) {
            throw new IllegalArgumentException("Invalid account status");
        } 
        if(!validStatus.contains(value)) {
            throw new IllegalArgumentException("Invalid account status");
        }
    }
}
