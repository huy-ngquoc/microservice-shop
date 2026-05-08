package vn.uit.edu.msshop.inventory.domain.model.valueobject;

import java.util.Set;

public record InventoryStatus(String value) {
    private static final Set<String> VALID_STATUS=Set.of("ENABLE","DISABLE");
    public InventoryStatus {
        if(value==null) {
            throw new IllegalArgumentException("Invalid account status");
        } 
        if(!VALID_STATUS.contains(value)) {
            throw new IllegalArgumentException("Invalid account status");
        }
    }
}
