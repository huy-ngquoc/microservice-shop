package vn.edu.uit.msshop.product.brand.application.dto.query;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record BrandView(
        UUID id,

        String name,

        @Nullable
        String logoKey,

        @Nullable
        Long version) {
}
