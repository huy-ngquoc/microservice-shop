package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

public record RemoveProductVariantsCommand(
        ProductId id,
        List<ProductVariantId> variantIds,
        ProductVersion expectedVersion) {
    public RemoveProductVariantsCommand {
        variantIds = List.copyOf(variantIds);
    }
}
