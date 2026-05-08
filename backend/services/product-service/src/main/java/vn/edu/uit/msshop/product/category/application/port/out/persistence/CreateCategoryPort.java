package vn.edu.uit.msshop.product.category.application.port.out.persistence;

import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.creation.NewCategory;

public interface CreateCategoryPort {
    Category create(
            final NewCategory newCategory);
}
