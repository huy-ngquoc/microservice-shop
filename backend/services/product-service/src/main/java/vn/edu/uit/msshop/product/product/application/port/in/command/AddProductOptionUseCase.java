package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.AddProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface AddProductOptionUseCase {
  ProductView addOption(final AddProductOptionCommand command);
}
