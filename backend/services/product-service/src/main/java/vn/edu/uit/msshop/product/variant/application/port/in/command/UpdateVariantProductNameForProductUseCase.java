package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantProductNameForProductCommand;

public interface UpdateVariantProductNameForProductUseCase {
    void execute(
            final UpdateVariantProductNameForProductCommand command);
}
