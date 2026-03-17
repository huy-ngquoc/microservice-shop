package vn.edu.uit.msshop.product.variant.adapter.in.web.response;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record VariantImageResponse(
        UUID id,

        @Nullable
        String imageKey,

        long version) {
}
