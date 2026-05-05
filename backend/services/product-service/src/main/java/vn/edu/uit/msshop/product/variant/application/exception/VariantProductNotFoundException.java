package vn.edu.uit.msshop.product.variant.application.exception;

import vn.edu.uit.msshop.shared.application.exception.NotFoundException;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public class VariantProductNotFoundException extends NotFoundException {
  public VariantProductNotFoundException(final VariantProductId productId, final Throwable cause) {
    super("Variant product", productId.value().toString(), cause);
  }

  public VariantProductNotFoundException(final VariantProductId productId) {
    super("Variant product", productId.value().toString());
  }
}
