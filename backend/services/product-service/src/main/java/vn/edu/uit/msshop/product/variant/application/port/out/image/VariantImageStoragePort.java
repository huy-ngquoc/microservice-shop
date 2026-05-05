package vn.edu.uit.msshop.product.variant.application.port.out.image;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;

public interface VariantImageStoragePort {
  boolean existsAsTemp(final VariantImageKey key);

  void publishImage(final VariantImageKey key);

  void unpublishImage(final VariantImageKey key);

  void deleteImage(final VariantImageKey key);
}
