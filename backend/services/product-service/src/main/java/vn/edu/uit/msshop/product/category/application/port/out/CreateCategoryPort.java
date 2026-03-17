package vn.edu.uit.msshop.product.category.application.port.out;

import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.NewCategory;

public interface CreateCategoryPort {
    Category create(
            final NewCategory newCategory);
}
