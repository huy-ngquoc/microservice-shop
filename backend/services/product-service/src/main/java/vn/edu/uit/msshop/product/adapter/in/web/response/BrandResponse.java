package vn.edu.uit.msshop.product.adapter.in.web.response;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record BrandResponse(
        UUID id,

        String name,

        @Nullable
        String logoUrl) {
}
