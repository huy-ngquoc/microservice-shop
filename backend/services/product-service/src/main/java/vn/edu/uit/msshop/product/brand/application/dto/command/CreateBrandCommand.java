package vn.edu.uit.msshop.product.brand.application.dto.command;

import vn.edu.uit.msshop.product.brand.domain.model.BrandName;

public record CreateBrandCommand(
        BrandName name) {
}
