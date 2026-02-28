package vn.edu.uit.msshop.product.application.dto.command;

import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

public record UpdateBrandImageCommand(
        BrandId id,
        byte[] bytes,
        String originalFilename,
        String contentType) {
}
