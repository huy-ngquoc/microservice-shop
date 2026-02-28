package vn.edu.uit.msshop.product.application.dto.command;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

public record CreateBrandCommand(
        BrandName name,

        @Nullable
        BrandLogo logo) {
}
