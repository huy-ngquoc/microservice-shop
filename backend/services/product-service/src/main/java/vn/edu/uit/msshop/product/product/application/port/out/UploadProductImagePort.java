package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductImage;

public interface UploadProductImagePort {
    ProductImage upload(
            ProductId id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType);
}
