package vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandRestorationByIdCommand;

public interface BrandRestorationByIdUseCase {
    void restoreById(
            final BrandRestorationByIdCommand cmd);
}
