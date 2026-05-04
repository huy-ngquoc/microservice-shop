package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.HardDeleteVariantCommand;

public interface HardDeleteVariantUseCase {
    void purge(
            final HardDeleteVariantCommand command);
}
