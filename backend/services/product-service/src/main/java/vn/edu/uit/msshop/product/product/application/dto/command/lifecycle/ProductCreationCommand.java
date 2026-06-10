package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import java.util.List;
import java.util.UUID;

import vn.edu.uit.msshop.product.product.application.dto.command.data.NewProductVariantData;

public record ProductCreationCommand(
        String productName,
        UUID categoryId,
        UUID brandId,
        List<String> optionList,
        List<NewProductVariantData> variantList) {
}
