package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkRestorationByIdsPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkRestorationByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkRestorationByIdsForProductUseCase;

@Component
@RequiredArgsConstructor
public class ProductToVariantRestorationSyncAdapter
        implements ProductVariantBulkRestorationByIdsPort {

    private final VariantBulkRestorationByIdsForProductUseCase variantBulkRestorationByIdsForProductUseCase;

    @Override
    public void restoreByVariantIds(
            final Collection<ProductVariantId> variantIdCollection) {
        final var rawVariantIdSet = variantIdCollection.stream()
                .map(ProductVariantId::value)
                .collect(Collectors.toUnmodifiableSet());

        final var command = new VariantBulkRestorationByIdsForProductCommand(rawVariantIdSet);
        this.variantBulkRestorationByIdsForProductUseCase.restoreByIds(command);
    }
}
