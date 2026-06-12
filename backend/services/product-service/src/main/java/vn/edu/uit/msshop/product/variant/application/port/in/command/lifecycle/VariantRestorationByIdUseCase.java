package vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.variant.application.dto.command.RestoreVariantCommand;

public interface VariantRestorationByIdUseCase {
    void restore(
            final RestoreVariantCommand command);
}
