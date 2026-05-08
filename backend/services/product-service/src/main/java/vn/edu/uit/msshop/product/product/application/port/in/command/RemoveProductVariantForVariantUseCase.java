package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductVariantForVariantCommand;

public interface RemoveProductVariantForVariantUseCase {
    void removeVariant(
            final RemoveProductVariantForVariantCommand command);
}
