package vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantSoftDeletionByIdCommand;

public interface VariantSoftDeletionByIdUseCase {
    void delete(
            final VariantSoftDeletionByIdCommand cmd);
}
