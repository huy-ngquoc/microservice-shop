package vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public interface BrandInfoUpdateUseCase {
    BrandView updateInfo(
            final BrandInfoUpdateByIdCommand cmd);
}
