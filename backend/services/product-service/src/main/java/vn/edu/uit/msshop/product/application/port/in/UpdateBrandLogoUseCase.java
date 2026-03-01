package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.command.UpdateBrandLogoCommand;
import vn.edu.uit.msshop.product.application.dto.query.BrandLogoView;

public interface UpdateBrandLogoUseCase {
    BrandLogoView updateImage(
            final UpdateBrandLogoCommand command);
}
