package vn.edu.uit.msshop.product.application.port.out;

import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImageKey;

public interface DeleteCategoryImagePort {
    void deleteByKey(
            final CategoryImageKey key);
}
