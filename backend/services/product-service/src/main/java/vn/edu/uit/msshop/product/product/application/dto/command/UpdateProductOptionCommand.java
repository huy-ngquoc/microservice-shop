package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductOption;
import vn.edu.uit.msshop.product.product.domain.model.ProductVersion;

public record UpdateProductOptionCommand(
        ProductId id,
        int optionIndex,
        ProductOption newOption,
        ProductVersion expectedVersion) {
}
