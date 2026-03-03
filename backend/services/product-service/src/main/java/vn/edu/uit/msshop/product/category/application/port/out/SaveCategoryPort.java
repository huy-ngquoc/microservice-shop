package vn.edu.uit.msshop.product.category.application.port.out;

import vn.edu.uit.msshop.product.category.domain.model.Category;

public interface SaveCategoryPort {
    Category save(
            final Category category);
}
