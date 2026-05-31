package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;

public interface HardDeleteBrandUseCase {
    void purge(
            final BrandLifecycleCommands.HardDelete command);
}
