package vn.edu.uit.msshop.product.brand.application.dto.query;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record BrandLogoView(
        UUID id,

        @Nullable
        String logoKey,

        @Nullable
        Long version) {
}
