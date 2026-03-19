package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.ProductVersion;
import vn.edu.uit.msshop.product.shared.application.dto.Change;

public record UpdateProductInfoCommand(
        ProductId id,
        Change<ProductName> name,
        Change<ProductCategoryId> categoryId,
        Change<ProductBrandId> brandId,
        ProductVersion expectedVersion) {
}
