package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantProductNameBulkUpdateForProductCommand;

public interface VariantProductNameBulkUpdateForProductUseCase {
    void execute(
            final VariantProductNameBulkUpdateForProductCommand cmd);
}
