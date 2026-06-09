package vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command;

import vn.edu.uit.msshop.product.category.domain.model.Category;

public interface CategoryUpdatePort {
    Category update(
            final Category category);
}
