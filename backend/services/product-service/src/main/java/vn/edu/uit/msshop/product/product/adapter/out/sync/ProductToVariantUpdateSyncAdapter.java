package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantNameBulkUpdateForProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantTraitBulkUpdatePort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantTraitBulkUpdateByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantProductNameBulkUpdateForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantTraitBulkUpdateByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantProductNameBulkUpdateForProductUseCase;

@Component
@RequiredArgsConstructor
public class ProductToVariantUpdateSyncAdapter
        implements
        ProductVariantTraitBulkUpdatePort,
        ProductVariantNameBulkUpdateForProductPort {

    private final VariantTraitBulkUpdateByIdsForProductUseCase variantTraitBulkUpdateByIdsForProductUseCase;
    private final VariantProductNameBulkUpdateForProductUseCase variantProductNameBulkUpdateForProductUseCase;

    @Override
    public void updateTraitsByIds(
            final Map<ProductVariantId, ProductVariantTraits> newTraitsByVariantId) {
        final var amountVariants = newTraitsByVariantId.size();
        final var traitListById = HashMap.<UUID, List<String>>newHashMap(amountVariants);

        for (final var entry : newTraitsByVariantId.entrySet()) {
            final var productVariantId = entry.getKey();
            final var productVariantTraits = entry.getValue();

            final var rawVariantId = productVariantId.value();
            final var rawVariantTraitList = productVariantTraits.unwrap();

            traitListById.put(rawVariantId, rawVariantTraitList);
        }

        final var command = new VariantTraitBulkUpdateByIdsForProductCommand(traitListById);
        this.variantTraitBulkUpdateByIdsForProductUseCase.updateTraitsByIds(command);
    }

    @Override
    public void updateProductNameByProductId(
            final ProductId id,
            final ProductName name) {
        final var rawVariantId = id.value();
        final var rawProductName = name.value();

        final var command = new VariantProductNameBulkUpdateForProductCommand(
                rawVariantId,
                rawProductName);
        this.variantProductNameBulkUpdateForProductUseCase.execute(command);
    }
}
