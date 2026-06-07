package vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandHardDeletionByIdCommand;

public interface BrandHardDeletionUseCase {
    void hardDelete(
            final BrandHardDeletionByIdCommand cmd);
}
