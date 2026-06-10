package vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandSoftDeletionByIdCommand;

public interface BrandSoftDeletionByIdUseCase {
    void softDeleteById(
            final BrandSoftDeletionByIdCommand cmd);
}
