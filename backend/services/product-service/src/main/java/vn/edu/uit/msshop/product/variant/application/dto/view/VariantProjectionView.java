package vn.edu.uit.msshop.product.variant.application.dto.view;

import java.util.List;
import java.util.UUID;

public record VariantProjectionView(
        UUID variantId,

        long price,

        List<String> traitList) {

    public VariantProjectionView {
        traitList = List.copyOf(traitList);
    }
}
