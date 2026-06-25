package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantProjectionRebuildByProductIdCommand;

public interface ProductVariantProjectionRebuildByProductIdUseCase {
    void rebuild(
            final ProductVariantProjectionRebuildByProductIdCommand cmd);
}
