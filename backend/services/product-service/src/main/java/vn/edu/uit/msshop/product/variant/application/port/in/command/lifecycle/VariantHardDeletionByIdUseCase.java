package vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantHardDeletionByIdCommand;

public interface VariantHardDeletionByIdUseCase {
    void hardDelete(
            final VariantHardDeletionByIdCommand cmd);
}
