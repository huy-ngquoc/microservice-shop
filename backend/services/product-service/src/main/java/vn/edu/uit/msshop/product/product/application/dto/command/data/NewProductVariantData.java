package vn.edu.uit.msshop.product.product.application.dto.command.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;

public record NewProductVariantData(
        long price,
        List<String> traitList,
        List<String> targetList) {

    public NewProductVariantData {
        traitList = List.copyOf(traitList);
        targetList = List.copyOf(targetList);
    }

    public NewProductVariant toNewProductVariant() {
        return NewProductVariant.of(
                this.price,
                this.traitList,
                this.targetList);
    }

    public static NewProductVariants toNewProductVariants(
            final Collection<NewProductVariantData> dataList) {
        final var variantList = new ArrayList<NewProductVariant>(dataList.size());
        for (final var data : dataList) {
            variantList.add(data.toNewProductVariant());
        }
        return new NewProductVariants(variantList);
    }
}
