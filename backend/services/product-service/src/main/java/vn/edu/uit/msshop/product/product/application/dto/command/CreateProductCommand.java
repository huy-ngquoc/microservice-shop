package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.NewProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;

public record CreateProductCommand(
        ProductName name,

        ProductCategoryId categoryId,

        ProductBrandId brandId,

        NewProductConfiguration newConfiguration) {
}
