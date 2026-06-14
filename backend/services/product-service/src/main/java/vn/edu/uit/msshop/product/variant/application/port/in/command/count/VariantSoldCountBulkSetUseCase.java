package vn.edu.uit.msshop.product.variant.application.port.in.command.count;

import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantSoldCountBulkSetCommand;

public interface VariantSoldCountBulkSetUseCase {
    void setAll(
            final VariantSoldCountBulkSetCommand cmd);
}
