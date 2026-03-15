package vn.edu.uit.msshop.product.brand.application.dto.command;

import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.BrandVersion;

public record DeleteBrandLogoCommand(
        BrandId id,
        BrandVersion expectedVersion) {
}
