package vn.uit.edu.msshop.notification.domain.model.valueobject;

import java.time.Instant;

public record CreationTime(Instant value) {
    public CreationTime {
        if(value==null) throw new IllegalArgumentException("Invalid creation time");
    }
}
