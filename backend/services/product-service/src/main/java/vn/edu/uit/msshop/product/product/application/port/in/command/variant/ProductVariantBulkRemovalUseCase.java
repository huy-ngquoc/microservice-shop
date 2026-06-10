package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantBulkRemovalCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductVariantBulkRemovalUseCase {
    ProductView removeAll(
            final ProductVariantBulkRemovalCommand cmd);
}
