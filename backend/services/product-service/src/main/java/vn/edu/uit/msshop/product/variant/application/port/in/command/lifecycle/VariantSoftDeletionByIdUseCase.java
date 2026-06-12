package vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.variant.application.dto.command.SoftDeleteVariantCommand;

public interface VariantSoftDeletionByIdUseCase {
    void delete(
            final SoftDeleteVariantCommand command);
}
