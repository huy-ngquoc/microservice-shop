package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.Collection;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkRestorationByIdsPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkRestorationByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class ProductToVariantRestorationSyncAdapter
        implements ProductVariantBulkRestorationByIdsPort {

    private final VariantBulkRestorationByIdsForProductUseCase variantBulkRestorationByIdsForProductUseCase;

    @Override
    public void restoreByVariantIds(
            final Collection<ProductVariantId> variantIds) {
        final var ids = variantIds.stream()
                .map(ProductVariantId::value)
                .map(VariantId::new)
                .toList();

        this.variantBulkRestorationByIdsForProductUseCase.restoreByIds(ids);
    }
}
