package vn.edu.uit.msshop.product.variant.application.dto.command.data;

import java.util.List;

public record NewVariantForNewProductData(
        long price,
        List<String> traitList,
        List<String> targetList) {

    public NewVariantForNewProductData {
        traitList = List.copyOf(traitList);
        targetList = List.copyOf(targetList);
    }
}
