package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.Product;

public interface ProductBulkUpdatePort {
    List<Product> updateAll(
            final Collection<Product> products);
}
