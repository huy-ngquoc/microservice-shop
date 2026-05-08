package vn.uit.edu.msshop.notification.domain.model.valueobject;

import java.util.UUID;

public record EmailId(UUID value) {
    public EmailId {
        if(value==null) throw new IllegalArgumentException("Invalid email");
    }

}
