package vn.edu.uit.msshop.product.category.application.port.out.persistence;

import vn.edu.uit.msshop.product.category.domain.model.Category;

public interface UpdateCategoryPort {
  Category update(final Category category);
}
