package vn.edu.uit.msshop.product.product.application.port.in;

import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;

public interface CreateProductUseCase {
    ProductView create(
            final CreateProductCommand command);
}
