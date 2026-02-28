package vn.edu.uit.msshop.product.application.port.out;

import java.util.Optional;

import vn.edu.uit.msshop.product.domain.model.category.Category;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;

public interface LoadCategoryPort {
    Optional<Category> loadById(
            final CategoryId id);
}
