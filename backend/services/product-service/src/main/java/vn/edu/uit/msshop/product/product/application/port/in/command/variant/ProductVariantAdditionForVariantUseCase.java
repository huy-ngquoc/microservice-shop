package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantAdditionForVariantCommand;

public interface ProductVariantAdditionForVariantUseCase {
    void add(
            final ProductVariantAdditionForVariantCommand command);
}
