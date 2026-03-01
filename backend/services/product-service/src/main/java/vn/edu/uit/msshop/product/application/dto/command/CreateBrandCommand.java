package vn.edu.uit.msshop.product.application.dto.command;

import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

public record CreateBrandCommand(
        BrandName name) {
}
