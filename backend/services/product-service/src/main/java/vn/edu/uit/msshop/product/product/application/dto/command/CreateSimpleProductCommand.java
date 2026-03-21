package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.ProductPrice;

public record CreateSimpleProductCommand(
        ProductName name,
        ProductCategoryId categoryId,
        ProductBrandId brandId,
        ProductPrice price) {
}
