package vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.out.persistence.ProductStockCountDocument;
import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductStockCountValue;

@Component
public class ProductStockCountPersistenceMapper {
    public ProductStockCount toDomain(
            final ProductStockCountDocument doc) {
        final var id = new ProductId(doc.getId());
        final var value = new ProductStockCountValue(doc.getValue());

        return new ProductStockCount(
                id,
                value);
    }
}
