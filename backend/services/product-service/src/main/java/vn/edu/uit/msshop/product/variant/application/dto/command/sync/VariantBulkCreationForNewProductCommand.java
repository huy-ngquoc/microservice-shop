package vn.edu.uit.msshop.product.variant.application.dto.command.sync;

import java.util.List;
import java.util.UUID;

import vn.edu.uit.msshop.product.variant.application.dto.command.data.NewVariantForNewProductData;

public record VariantBulkCreationForNewProductCommand(
        UUID productId,
        String productName,
        List<NewVariantForNewProductData> newVariantList) {

    public VariantBulkCreationForNewProductCommand {
        newVariantList = List.copyOf(newVariantList);
    }
}
