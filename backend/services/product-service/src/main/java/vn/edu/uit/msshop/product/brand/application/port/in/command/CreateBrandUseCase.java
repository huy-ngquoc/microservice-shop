package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.CreateBrandCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public interface CreateBrandUseCase {
  BrandView create(final CreateBrandCommand command);
}
