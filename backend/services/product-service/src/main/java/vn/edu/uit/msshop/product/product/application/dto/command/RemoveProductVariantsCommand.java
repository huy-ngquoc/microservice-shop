package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVersion;

public record RemoveProductVariantsCommand(
        ProductId id,
        List<ProductVariantId> variantIds,
        ProductVersion expectedVersion) {
    public RemoveProductVariantsCommand {
        variantIds = List.copyOf(variantIds);
    }
}
