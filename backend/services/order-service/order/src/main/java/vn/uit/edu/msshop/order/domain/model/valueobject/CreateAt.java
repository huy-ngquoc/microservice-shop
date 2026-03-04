package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.time.Instant;

public record CreateAt(Instant value) {
    public CreateAt {
        if(value==null) {
            throw new IllegalArgumentException("Invalid creation time");
        }
    }

}
