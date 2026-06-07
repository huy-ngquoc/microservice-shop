package vn.edu.uit.msshop.product.brand.application.port.in.command.logo;

import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoUpdateByIdCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;

public interface BrandLogoUpdateUseCase {
    BrandLogoView update(
            final BrandLogoUpdateByIdCommand cmd);
}
