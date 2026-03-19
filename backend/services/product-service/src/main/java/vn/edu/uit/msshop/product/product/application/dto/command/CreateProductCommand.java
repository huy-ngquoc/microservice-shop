package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;

public record CreateProductCommand(
        ProductName name,
        ProductCategoryId categoryId,
        ProductBrandId brandId,
        ProductOptions options,
        List<CreateProductVariantCommand> variants) {
}
