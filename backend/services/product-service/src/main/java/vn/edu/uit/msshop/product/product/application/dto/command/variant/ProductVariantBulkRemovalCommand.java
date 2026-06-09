package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

public record ProductVariantBulkRemovalCommand(
        ProductId id,
        List<ProductVariantId> variantIds,
        ProductVersion expectedVersion) {
    public ProductVariantBulkRemovalCommand {
        variantIds = List.copyOf(variantIds);
    }
}
