package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.NewProduct;
import vn.edu.uit.msshop.product.product.domain.model.Product;

public interface CreateProductPort {
    Product create(
            final NewProduct newProduct);
}
