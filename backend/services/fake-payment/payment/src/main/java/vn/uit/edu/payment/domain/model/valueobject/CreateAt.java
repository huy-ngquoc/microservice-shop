package vn.uit.edu.payment.domain.model.valueobject;

import java.time.Instant;

public record CreateAt(
        Instant value) {
    public CreateAt {
        if (value == null) {
            throw new IllegalArgumentException("Invalid creation time");
        }
    }

}
