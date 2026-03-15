package vn.edu.uit.msshop.product.brand.application.port.in;

import vn.edu.uit.msshop.product.brand.application.dto.command.DeleteBrandLogoCommand;

public interface DeleteBrandLogoUseCase {
    void deleteLogo(
            final DeleteBrandLogoCommand command);
}
