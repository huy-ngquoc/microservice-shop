package vn.edu.uit.msshop.product.category.application.port.out;

import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;

public interface MoveCategoryImagePort {
    void moveToCategory(
            final CategoryImageKey key);

    void moveBackToTemp(
            final CategoryImageKey key);

    void deleteFromCategory(
            final CategoryImageKey key);
}
