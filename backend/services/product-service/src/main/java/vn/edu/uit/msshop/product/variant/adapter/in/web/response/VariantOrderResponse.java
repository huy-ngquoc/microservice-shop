package vn.edu.uit.msshop.product.variant.adapter.in.web.response;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record VariantOrderResponse(
    UUID id,

        UUID productId,

        long price,

        int soldCount,

        List<String> traits,

        @Nullable
        String imageKey,
        String productName
) {

}
