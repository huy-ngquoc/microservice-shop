package vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.existence;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CategoryActiveExistenceCheckByIdPort {
    boolean existsActiveById(
            final CategoryId id);
}
