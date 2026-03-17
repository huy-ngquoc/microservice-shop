package vn.edu.uit.msshop.product.variant.application.dto.query;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record VariantView(
        UUID id,

        UUID productId,

        int price,

        int soldCount,

        List<String> traits,

        @Nullable
        String imageKey,

        long version) {
    public VariantView {
        traits = List.copyOf(traits);
    }
}
