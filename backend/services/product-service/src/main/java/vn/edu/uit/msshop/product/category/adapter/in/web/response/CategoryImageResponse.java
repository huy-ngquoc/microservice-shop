package vn.edu.uit.msshop.product.category.adapter.in.web.response;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record CategoryImageResponse(
        UUID id,
        String imageUrl,

        @Nullable
        Long version) {
}
