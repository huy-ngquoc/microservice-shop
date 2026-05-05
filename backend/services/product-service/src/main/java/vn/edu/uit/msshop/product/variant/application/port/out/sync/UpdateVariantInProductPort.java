package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface UpdateVariantInProductPort {
  void updateInProduct(final Variant variant);
}
