package vn.edu.uit.msshop.product.application.port.out;

import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;

public interface UploadBrandLogoPort {
    BrandLogo upload(
            final BrandId id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType);
}
