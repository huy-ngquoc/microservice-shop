package vn.uit.edu.msshop.notification.domain.model.valueobject;

import java.time.Instant;

public record SendTime(Instant value) {
    public SendTime {
        if(value==null) throw new IllegalArgumentException("Invalid send time");
    }

}
