package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantProductNameForProductCommand;

public interface VariantProductNameBulkUpdateForProductUseCase {
    void execute(
            final UpdateVariantProductNameForProductCommand command);
}
