package vn.edu.uit.msshop.product.category.application.port.out;

import java.util.Optional;

import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

public interface LoadCategoryPort {
    Optional<Category> loadById(
            final CategoryId id);
}
