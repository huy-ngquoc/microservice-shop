package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;

public record ProductCreationCommand(
        ProductName name,

        ProductCategoryId categoryId,

        ProductBrandId brandId,

        NewProductConfiguration newConfiguration) {
}
