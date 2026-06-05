package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProduct;

public interface ProductCreationPort {
    Product create(
            final NewProduct newProduct);
}
