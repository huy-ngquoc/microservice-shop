package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;

public interface UpdateVariantInfoUseCase {
    void updateInfo(
            final UpdateVariantInfoCommand command);
}
