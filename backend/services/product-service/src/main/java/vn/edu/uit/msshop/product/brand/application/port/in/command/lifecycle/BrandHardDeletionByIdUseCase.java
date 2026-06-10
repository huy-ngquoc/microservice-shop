package vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandHardDeletionByIdCommand;

public interface BrandHardDeletionByIdUseCase {
    void hardDeleteById(
            final BrandHardDeletionByIdCommand cmd);
}
