package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.DeleteBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;

public interface DeleteBrandLogoUseCase {
  BrandLogoView deleteLogo(final DeleteBrandLogoCommand command);
}
