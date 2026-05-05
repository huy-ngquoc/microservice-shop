package vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.adapter.out.persistence.VariantStockCountDocument;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;

@Component
public class VariantStockCountPersistenceMapper {
  public VariantStockCount toDomain(final VariantStockCountDocument doc) {
    final var variantId = new VariantId(doc.getVariantId());
    final var productId = new VariantProductId(doc.getProductId());
    final var value = new VariantStockCountValue(doc.getValue());

    return new VariantStockCount(variantId, productId, value);
  }
}
