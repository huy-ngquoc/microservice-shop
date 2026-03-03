package vn.edu.uit.msshop.product.category.application.port.out;

import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;

public interface DeleteCategoryImagePort {
    void deleteByKey(
            final CategoryImageKey key);
}
