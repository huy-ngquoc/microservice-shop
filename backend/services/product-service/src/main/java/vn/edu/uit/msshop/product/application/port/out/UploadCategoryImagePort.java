package vn.edu.uit.msshop.product.application.port.out;

import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;

public interface UploadCategoryImagePort {
    CategoryImage upload(
            final CategoryId id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType);
}
