package vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.adapter.out.persistence.VariantSoldCountDocument;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Component
public class VariantSoldCountPersistenceMapper {
    public VariantSoldCount toDomain(
            final VariantSoldCountDocument doc) {
        final var variantId = new VariantId(doc.getVariantId());
        final var productId = new VariantProductId(doc.getProductId());
        final var value = new VariantSoldCountValue(doc.getValue());

        return new VariantSoldCount(
                variantId,
                productId,
                value);
    }
}
