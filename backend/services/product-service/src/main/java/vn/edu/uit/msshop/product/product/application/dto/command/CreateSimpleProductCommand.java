package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPrice;

public record CreateSimpleProductCommand(
        ProductName name,
        ProductCategoryId categoryId,
        ProductBrandId brandId,
        ProductPrice price) {
}
