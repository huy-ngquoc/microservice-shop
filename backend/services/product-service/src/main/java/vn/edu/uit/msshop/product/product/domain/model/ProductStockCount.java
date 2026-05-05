package vn.edu.uit.msshop.product.product.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductStockCountValue;
import vn.edu.uit.msshop.shared.domain.Domains;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class ProductStockCount {
  @EqualsAndHashCode.Include
  private final ProductId id;

  private final ProductStockCountValue value;

  public ProductStockCount(final ProductId id, final ProductStockCountValue value) {
    this.id = Domains.requireNonNull(id, "Product ID must NOT be null");
    this.value = Domains.requireNonNull(value, "Product stock count value must NOT be null");
  }

  public static ProductStockCount zero(final ProductId id) {
    final var value = new ProductStockCountValue(0);
    return new ProductStockCount(id, value);
  }
}
