package vn.edu.uit.msshop.product.variant.application.port.out.validation;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface CheckVariantRestorablePort {
  void validateRestorable(final Variant variant);
}
