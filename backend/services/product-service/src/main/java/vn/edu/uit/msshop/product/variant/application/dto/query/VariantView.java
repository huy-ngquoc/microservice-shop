package vn.edu.uit.msshop.product.variant.application.dto.query;

import java.util.List;
import java.util.UUID;

public record VariantView(
        UUID id,
        UUID productId,
        String imageKey,
        int price,
        int sold,
        List<String> traits) {
    public VariantView {
        traits = List.copyOf(traits);
    }
}
