package vn.uit.edu.msshop.notification.domain.model.valueobject;

import java.util.Set;

public record EmailStatus(String value) {
    private static final Set<String> VALID_STATUS = Set.of("SENT","UNSENT");
    public EmailStatus {
        if(!VALID_STATUS.contains(value)) throw new IllegalArgumentException("Invalid email status");
    }
}
