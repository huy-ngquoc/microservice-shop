package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.Product;

public interface SaveProductPort {
    Product save(
            final Product product);
}
