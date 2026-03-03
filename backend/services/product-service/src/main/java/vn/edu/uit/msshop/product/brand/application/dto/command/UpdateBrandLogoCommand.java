package vn.edu.uit.msshop.product.brand.application.dto.command;

import vn.edu.uit.msshop.product.brand.domain.model.BrandId;

public record UpdateBrandLogoCommand(
        BrandId id,
        byte[] bytes,
        String originalFilename,
        String contentType) {
}
