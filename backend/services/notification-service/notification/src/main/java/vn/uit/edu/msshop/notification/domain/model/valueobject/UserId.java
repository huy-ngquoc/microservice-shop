package vn.uit.edu.msshop.notification.domain.model.valueobject;

import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        if(value==null) throw new IllegalArgumentException("Invalid user id");
    }

}
