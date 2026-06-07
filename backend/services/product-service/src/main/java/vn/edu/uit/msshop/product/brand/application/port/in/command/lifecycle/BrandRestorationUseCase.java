package vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandRestorationByIdCommand;

public interface BrandRestorationUseCase {
    void restore(
            final BrandRestorationByIdCommand cmd);
}
