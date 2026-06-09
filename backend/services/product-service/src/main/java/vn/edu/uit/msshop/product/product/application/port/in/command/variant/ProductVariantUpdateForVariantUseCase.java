package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.ProductVariantUpdateForVariantCommand;

public interface ProductVariantUpdateForVariantUseCase {
    void update(
            final ProductVariantUpdateForVariantCommand command);
}
