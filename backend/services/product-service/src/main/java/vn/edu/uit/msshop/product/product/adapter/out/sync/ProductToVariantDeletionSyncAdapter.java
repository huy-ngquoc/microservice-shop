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
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkHardDeletionByProductIdForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByProductIdForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkHardDeletionByProductIdForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkSoftDeletionByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkSoftDeletionByProductIdForProductUseCase;

@Component
@RequiredArgsConstructor
public class ProductToVariantDeletionSyncAdapter
        implements
        ProductVariantBulkSoftDeletionByIdsPort,
        ProductVariantBulkSoftDeletionForProductPort,
        ProductVariantBulkHardDeletionForProductPort {

    private final VariantBulkSoftDeletionByIdsForProductUseCase variantBulkSoftDeletionByIdsForProductUseCase;
    private final VariantBulkSoftDeletionByProductIdForProductUseCase variantBulkSoftDeletionByProductIdForProductUseCase;
    private final VariantBulkHardDeletionByProductIdForProductUseCase variantBulkHardDeletionByProductIdForProductUseCase;

    @Override
    public void deleteByProductId(
            final ProductId productId) {
        final var rawProductId = productId.value();

        final var command = new VariantBulkSoftDeletionByProductIdForProductCommand(rawProductId);
        this.variantBulkSoftDeletionByProductIdForProductUseCase.deleteByProductId(command);
    }

    @Override
    public void deleteByIds(
            final Collection<ProductVariantId> variantIdCollection) {
        final var rawVariantIdSet = variantIdCollection.stream()
                .map(ProductVariantId::value)
                .collect(Collectors.toUnmodifiableSet());

        final var command = new VariantBulkSoftDeletionByIdsForProductCommand(rawVariantIdSet);
        this.variantBulkSoftDeletionByIdsForProductUseCase.deleteByIds(command);
    }

    @Override
    public void purgeByProductId(
            final ProductId productId) {
        final var rawProductId = productId.value();

        final var command = new VariantBulkHardDeletionByProductIdForProductCommand(rawProductId);
        this.variantBulkHardDeletionByProductIdForProductUseCase.purgeByProductId(command);
    }
}
