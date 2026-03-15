package vn.edu.uit.msshop.product.brand.adapter.in.web.response;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record BrandLogoResponse(
        UUID id,

        @Nullable
        String logoUrl,

        @Nullable
        Long version) {
}
