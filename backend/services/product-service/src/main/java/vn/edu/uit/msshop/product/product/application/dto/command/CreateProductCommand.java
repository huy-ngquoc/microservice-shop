package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;

// TODO: check images.size() <= MAX_SIZE
public record CreateProductCommand(
        ProductName name,
        ProductCategoryId categoryId,
        List<ProductImageCommand> images,
        ProductBrandId brandId) {
}
