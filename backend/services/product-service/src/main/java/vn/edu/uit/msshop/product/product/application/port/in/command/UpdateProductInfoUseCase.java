package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface UpdateProductInfoUseCase {
  ProductView updateInfo(final UpdateProductInfoCommand command);
}
