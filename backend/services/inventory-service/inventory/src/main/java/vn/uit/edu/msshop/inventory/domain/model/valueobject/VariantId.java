package vn.uit.edu.msshop.inventory.domain.model.valueobject;

import java.util.UUID;

public record VariantId(UUID value) {
    public VariantId {
        if(value==null) throw new IllegalArgumentException("Invalid inventory id");
    }

}
