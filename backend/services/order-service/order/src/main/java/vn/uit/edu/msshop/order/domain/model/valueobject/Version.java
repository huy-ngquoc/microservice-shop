package vn.uit.edu.msshop.order.domain.model.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;

public record Version(Long value) {
    @JsonValue
    public Long getValue() {
        return this.value;
    }

}
