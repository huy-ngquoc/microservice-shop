package vn.edu.uit.msshop.product.brand.application.port.in;

import vn.edu.uit.msshop.product.brand.application.dto.command.DeleteBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;

public interface DeleteBrandLogoUseCase {
    BrandLogoView deleteLogo(
            final DeleteBrandLogoCommand command);
}
