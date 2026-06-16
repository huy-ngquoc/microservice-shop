package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;

public record ProductOptionRemovalRequest(
        @NotNull
        Long version) {
}
