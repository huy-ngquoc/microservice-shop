package vn.edu.uit.msshop.product.category.application.port.out;

import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;

public interface CategoryImageStoragePort {
    boolean existsAsTemp(
            final CategoryImageKey key);

    void publishImage(
            final CategoryImageKey key);

    void unpublishImage(
            final CategoryImageKey key);

    void deleteImage(
            final CategoryImageKey key);
}
