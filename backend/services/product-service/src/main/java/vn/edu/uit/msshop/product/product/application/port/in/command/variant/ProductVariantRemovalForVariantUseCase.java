package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.variant.RemoveProductVariantForVariantCommand;

public interface ProductVariantRemovalForVariantUseCase {
    void remove(
            final RemoveProductVariantForVariantCommand command);
}
