package vn.edu.uit.msshop.product.variant.adapter.in.web.request;

import java.util.List;
import java.util.UUID;

public record CreateVariantRequest(
        UUID productId,
        String imageKey,
        long price,
        int sold,
        List<String> traits) {
    public CreateVariantRequest {
        traits = List.copyOf(traits);
    }
}
