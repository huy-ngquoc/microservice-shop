package vn.edu.uit.msshop.product.product.application.dto.view;

import java.util.List;
import java.util.UUID;

public record ProductVariantView(
        UUID id,

        long price,

        List<String> traits) {
    public ProductVariantView {
        traits = List.copyOf(traits);
    }
}
