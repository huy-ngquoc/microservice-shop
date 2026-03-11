package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantCommand;

public interface CreateVariantUseCase {
    void create(
            final CreateVariantCommand command);
}
