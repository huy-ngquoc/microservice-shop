package vn.edu.uit.msshop.product.variant.adapter.in.web.response;

import java.util.UUID;

public record VariantResponse(
        UUID id,
        UUID productId,
        String imageKey,
        int price,
        int sold) {
}
