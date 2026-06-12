package vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.variant.application.dto.command.HardDeleteVariantCommand;

public interface VariantHardDeletionByIdUseCase {
    void purge(
            final HardDeleteVariantCommand command);
}
