package vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.lookup;

import java.util.Optional;

import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CategoryActiveLookupByIdPort {
    Optional<Category> loadActiveById(
            final CategoryId id);
}
