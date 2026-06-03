package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductVariantForVariantCommand;

public interface UpdateProductVariantForVariantUseCase {
    void updateVariant(
            final UpdateProductVariantForVariantCommand command);
}
