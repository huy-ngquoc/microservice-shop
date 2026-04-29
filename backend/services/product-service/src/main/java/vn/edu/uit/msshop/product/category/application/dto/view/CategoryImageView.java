package vn.edu.uit.msshop.product.category.application.dto.view;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record CategoryImageView(
        UUID id,

        @Nullable
        String imageKey,

        long version) {
}
