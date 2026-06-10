package vn.edu.uit.msshop.product.brand.application.port.in.command.logo;

import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoDeletionByIdCommand;

public interface BrandLogoDeletionByIdUseCase {
    void deleteById(
            final BrandLogoDeletionByIdCommand cmd);
}
