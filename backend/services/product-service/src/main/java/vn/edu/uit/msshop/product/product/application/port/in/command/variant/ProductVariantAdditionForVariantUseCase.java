package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.AddProductVariantForVariantCommand;

public interface ProductVariantAdditionForVariantUseCase {
    void addVariant(
            final AddProductVariantForVariantCommand command);
}
