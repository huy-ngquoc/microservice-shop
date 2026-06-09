package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.shared.application.dto.Change;

public record ProductInfoUpdateCommand(
        ProductId id,
        Change<ProductName> name,
        Change<ProductCategoryId> categoryId,
        Change<ProductBrandId> brandId,
        ProductVersion expectedVersion) {
}
