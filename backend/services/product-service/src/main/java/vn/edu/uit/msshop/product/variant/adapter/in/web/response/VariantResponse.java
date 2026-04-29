package vn.edu.uit.msshop.product.variant.adapter.in.web.response;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

// TODO: add "Targets"
public record VariantResponse(
        UUID id,

        UUID productId,

        String productName,

        long price,

        int soldCount,

        int stockCount,

        List<String> traits,

        @Nullable
        String imageKey,

        long version) {
}
