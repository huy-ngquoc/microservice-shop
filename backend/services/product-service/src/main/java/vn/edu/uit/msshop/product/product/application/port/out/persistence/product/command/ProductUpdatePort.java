package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command;

import vn.edu.uit.msshop.product.product.domain.model.Product;

public interface ProductUpdatePort {
    Product update(
            final Product product);
}
