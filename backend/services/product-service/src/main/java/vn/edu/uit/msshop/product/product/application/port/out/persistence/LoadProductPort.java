package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.List;
import java.util.Optional;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface LoadProductPort {
    Optional<Product> loadById(
            final ProductId id);
    public List<Product> loadByListId(List<ProductId> ids);
    public List<Product> loadByVariants(List<Variant> variants);
    
}
