package vn.uit.edu.msshop.image.domain.model.valueobject;

import java.util.UUID;

public record ObjectId(UUID value) {
    public ObjectId {
        if(value==null) {
            throw new IllegalArgumentException("Invalid object id");
        }
    }
}
