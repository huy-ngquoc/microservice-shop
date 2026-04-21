package vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.out.persistence.ProductSoldCountDocument;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductSoldCountValue;

@Component
public class ProductSoldCountPersistenceMapper {
    public ProductSoldCount toDomain(
            final ProductSoldCountDocument entity) {
        final var id = new ProductId(entity.getId());
        final var value = new ProductSoldCountValue(entity.getSoldCount());

        return new ProductSoldCount(
                id,
                value);
    }
}
