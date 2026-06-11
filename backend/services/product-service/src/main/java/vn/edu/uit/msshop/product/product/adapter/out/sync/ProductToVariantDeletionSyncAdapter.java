package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkHardDeletionForProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkSoftDeletionByIdsPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkSoftDeletionForProductPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.variant.application.port.in.command.HardDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteAllVariantsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Component
@RequiredArgsConstructor
public class ProductToVariantDeletionSyncAdapter
        implements
        ProductVariantBulkSoftDeletionByIdsPort,
        ProductVariantBulkSoftDeletionForProductPort,
        ProductVariantBulkHardDeletionForProductPort {

    private final SoftDeleteAllVariantsUseCase softDeleteAllUseCase;
    private final SoftDeleteVariantsForProductUseCase softDeleteForProductUseCase;
    private final HardDeleteVariantsForProductUseCase hardDeleteVariantsForProductUseCase;

    @Override
    public void deleteByProductId(
            final ProductId id) {
        final var productId = new VariantProductId(id.value());
        this.softDeleteForProductUseCase.deleteByProductId(productId);
    }

    @Override
    public void deleteByIds(
            final Collection<ProductVariantId> variantIds) {
        final var ids = variantIds.stream()
                .map(ProductVariantId::value)
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());
        this.softDeleteAllUseCase.deleteByIds(ids);
    }

    @Override
    public void purgeByProductId(
            final ProductId productId) {
        final var variantProductId = new VariantProductId(productId.value());
        this.hardDeleteVariantsForProductUseCase.purgeByProductId(variantProductId);
    }
}
