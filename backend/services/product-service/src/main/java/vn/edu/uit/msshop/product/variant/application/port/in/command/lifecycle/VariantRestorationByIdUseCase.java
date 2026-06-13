package vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantRestorationByIdCommand;

public interface VariantRestorationByIdUseCase {
    void restore(
            final VariantRestorationByIdCommand cmd);
}
