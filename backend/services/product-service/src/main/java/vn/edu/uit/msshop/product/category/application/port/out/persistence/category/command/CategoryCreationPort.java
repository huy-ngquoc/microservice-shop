package vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command;

import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.creation.NewCategory;

public interface CategoryCreationPort {
    Category create(
            final NewCategory newCategory);
}
