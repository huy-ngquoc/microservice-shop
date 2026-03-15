package vn.edu.uit.msshop.product.brand.application.port.in;

import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;

public interface UpdateBrandLogoUseCase {
    BrandLogoView updateLogo(
            final UpdateBrandLogoCommand command);
}
