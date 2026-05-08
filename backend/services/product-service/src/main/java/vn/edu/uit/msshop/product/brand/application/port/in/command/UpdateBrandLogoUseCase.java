package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;

public interface UpdateBrandLogoUseCase {
    BrandLogoView updateLogo(
            final UpdateBrandLogoCommand command);
}
