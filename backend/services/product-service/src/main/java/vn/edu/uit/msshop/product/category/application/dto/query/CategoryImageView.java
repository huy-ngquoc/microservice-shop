package vn.edu.uit.msshop.product.category.application.dto.query;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record CategoryImageView(
        UUID id,
        String imageKey,

        @Nullable
        Long version) {
}
