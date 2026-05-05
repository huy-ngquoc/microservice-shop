package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantsForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;

public record CreateVariantsForNewProductCommand(VariantProductId productId,VariantProductName productName,NewVariantsForNewProduct newVariantsForNewProduct){}
