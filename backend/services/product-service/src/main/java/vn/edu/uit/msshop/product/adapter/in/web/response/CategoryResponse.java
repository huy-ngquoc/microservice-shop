package vn.edu.uit.msshop.product.adapter.in.web.response;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record CategoryResponse(
        UUID id,

        String name,

        @Nullable
        String imageUrl) {
}
