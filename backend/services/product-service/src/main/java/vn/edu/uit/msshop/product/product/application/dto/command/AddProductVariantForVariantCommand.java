package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record AddProductVariantForVariantCommand(ProductId id,ProductVariant variant,int soldIncrement,int stockIncrement){}
