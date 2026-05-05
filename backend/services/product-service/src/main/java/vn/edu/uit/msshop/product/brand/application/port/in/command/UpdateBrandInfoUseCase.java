package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandInfoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public interface UpdateBrandInfoUseCase {
  BrandView updateInfo(final UpdateBrandInfoCommand command);
}
