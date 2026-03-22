package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.application.dto.command.SoftDeleteVariantCommand;

public interface SoftDeleteVariantUseCase {
    void delete(
            final SoftDeleteVariantCommand command);
}
