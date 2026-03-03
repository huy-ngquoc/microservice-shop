package vn.edu.uit.msshop.product.category.application.port.out;

import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImage;

public interface UploadCategoryImagePort {
    CategoryImage upload(
            final CategoryId id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType);
}
