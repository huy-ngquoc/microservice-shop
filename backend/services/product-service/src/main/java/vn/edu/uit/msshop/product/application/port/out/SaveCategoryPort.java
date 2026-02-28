package vn.edu.uit.msshop.product.application.port.out;

import vn.edu.uit.msshop.product.domain.model.category.Category;

public interface SaveCategoryPort {
    Category save(
            final Category category);
}
