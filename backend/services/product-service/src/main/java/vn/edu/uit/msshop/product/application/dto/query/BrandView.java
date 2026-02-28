package vn.edu.uit.msshop.product.application.dto.query;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record BrandView(
        UUID id,
        String name,

        @Nullable
        String logoUrl) {
}
