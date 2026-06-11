package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantNameBulkUpdateForProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantTraitBulkUpdatePort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantProductNameForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.UpdateAllVariantTraitsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.UpdateVariantProductNameForProductUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Component
@RequiredArgsConstructor
public class ProductToVariantUpdateSyncAdapter
        implements
        ProductVariantTraitBulkUpdatePort,
        ProductVariantNameBulkUpdateForProductPort {

    private final UpdateAllVariantTraitsForProductUseCase updateAllTraitsForProductUseCase;
    private final UpdateVariantProductNameForProductUseCase updateVariantProductNameForProductUseCase;

    @Override
    public void updateTraitsByIds(
            final Map<ProductVariantId, ProductVariantTraits> newTraitsMap) {
        final var map = HashMap.<VariantId, VariantTraits>newHashMap(newTraitsMap.size());

        for (final var entry : newTraitsMap.entrySet()) {
            final var productVariantId = entry.getKey();
            final var productVariantTraits = entry.getValue();

            final var variantId = new VariantId(productVariantId.value());
            final var variantTraits = VariantTraits.of(productVariantTraits.unwrap());

            map.put(variantId, variantTraits);
        }

        this.updateAllTraitsForProductUseCase.updateTraitsByIds(map);
    }

    @Override
    public void updateProductNameByProductId(
            final ProductId id,
            final ProductName name) {
        final var variantProductId = new VariantProductId(id.value());
        final var variantProductName = new VariantProductName(name.value());

        final var command = new UpdateVariantProductNameForProductCommand(
                variantProductId,
                variantProductName);
        this.updateVariantProductNameForProductUseCase.execute(command);
    }
}
