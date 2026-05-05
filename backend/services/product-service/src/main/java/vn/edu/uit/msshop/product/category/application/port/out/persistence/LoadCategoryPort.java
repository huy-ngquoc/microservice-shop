package vn.edu.uit.msshop.product.category.application.port.out.persistence;

import java.util.Optional;

import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface LoadCategoryPort {
  Optional<Category> loadById(final CategoryId id);
}
