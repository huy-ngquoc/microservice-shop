package vn.uit.edu.msshop.recommendation.domain.model.valueobject;

import java.util.UUID;

public record VariantId(UUID value) {
    public VariantId {
        if(value==null) throw new IllegalArgumentException("Invalid variant id");
    }
}
