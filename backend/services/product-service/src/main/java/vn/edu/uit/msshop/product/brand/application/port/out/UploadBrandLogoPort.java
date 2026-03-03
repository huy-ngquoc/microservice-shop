package vn.edu.uit.msshop.product.brand.application.port.out;

import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogo;

public interface UploadBrandLogoPort {
    BrandLogo upload(
            final BrandId id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType);
}
