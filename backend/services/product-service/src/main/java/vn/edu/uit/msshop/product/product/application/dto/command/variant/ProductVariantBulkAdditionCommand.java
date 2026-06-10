package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.List;
import java.util.UUID;

import vn.edu.uit.msshop.product.product.application.dto.command.data.NewProductVariantData;

public record ProductVariantBulkAdditionCommand(
        UUID productId,
        List<NewProductVariantData> newVariantList,
        long productVersion) {
}
