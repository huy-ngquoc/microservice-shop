package vn.edu.uit.msshop.product.category.application.port.out;

import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;

public interface VerifyCategoryImageKeyPort {
    boolean existsInTemp(
            final CategoryImageKey key);
}
