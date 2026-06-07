package vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandCreationCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public interface BrandCreationUseCase {
    BrandView create(
            final BrandCreationCommand cmd);
}
