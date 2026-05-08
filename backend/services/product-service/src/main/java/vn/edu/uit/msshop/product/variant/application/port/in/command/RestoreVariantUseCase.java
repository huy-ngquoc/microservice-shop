package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.RestoreVariantCommand;

public interface RestoreVariantUseCase {
    void restore(
            final RestoreVariantCommand command);
}
