package vn.edu.uit.msshop.product.product.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductSoldCountValue;
import vn.edu.uit.msshop.shared.domain.Domains;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class ProductSoldCount {
  @EqualsAndHashCode.Include
  private final ProductId id;

  private final ProductSoldCountValue value;

  public ProductSoldCount(final ProductId id, final ProductSoldCountValue value) {
    this.id = Domains.requireNonNull(id, "Product ID must NOT be null");
    this.value = Domains.requireNonNull(value, "Product sold count value must NOT be null");
  }

  public static ProductSoldCount zero(final ProductId id) {
    final var value = new ProductSoldCountValue(0);
    return new ProductSoldCount(id, value);
  }
}
