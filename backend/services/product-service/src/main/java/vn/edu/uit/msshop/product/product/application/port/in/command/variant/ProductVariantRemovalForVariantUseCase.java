package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductVariantForVariantCommand;

public interface ProductVariantRemovalForVariantUseCase {
    void removeVariant(
            final RemoveProductVariantForVariantCommand command);
}
