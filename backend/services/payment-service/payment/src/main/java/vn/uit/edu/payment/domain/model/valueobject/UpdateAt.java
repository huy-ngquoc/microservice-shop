package vn.uit.edu.payment.domain.model.valueobject;

import java.time.Instant;

public record UpdateAt(Instant value) {
    public UpdateAt {
        if(value==null) {
            throw new IllegalArgumentException("Invalid update time");
        }
    }

}
