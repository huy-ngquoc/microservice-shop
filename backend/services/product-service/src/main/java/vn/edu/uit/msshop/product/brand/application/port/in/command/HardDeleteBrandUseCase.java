package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.HardDeleteBrandCommand;

public interface HardDeleteBrandUseCase {
    void purge(
            final HardDeleteBrandCommand command);
}
