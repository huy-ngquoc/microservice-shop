package vn.edu.uit.msshop.product.variant.adapter.in.event;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.domain.event.ProductEvent;
import vn.edu.uit.msshop.product.product.domain.event.ProductHardDeletedEvent;
import vn.edu.uit.msshop.product.product.domain.event.ProductNameChangedEvent;
import vn.edu.uit.msshop.product.product.domain.event.ProductOptionAddedEvent;
import vn.edu.uit.msshop.product.product.domain.event.ProductOptionRemovedEvent;
import vn.edu.uit.msshop.product.product.domain.event.ProductRestoredEvent;
import vn.edu.uit.msshop.product.product.domain.event.ProductSoftDeletedEvent;
import vn.edu.uit.msshop.product.product.domain.event.ProductVariantBulkRemovedEvent;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkHardDeletionByProductIdForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkRestorationByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByProductIdForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantProductNameBulkUpdateForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantTraitBulkUpdateByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkHardDeletionByProductIdForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkRestorationByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkSoftDeletionByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkSoftDeletionByProductIdForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantProductNameBulkUpdateForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantTraitBulkUpdateByIdsForProductUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantProductEventListener {

    private final VariantProductNameBulkUpdateForProductUseCase productNameBulkUpdateUseCase;
    private final VariantTraitBulkUpdateByIdsForProductUseCase traitBulkUpdateByIdsUseCase;
    private final VariantBulkSoftDeletionByIdsForProductUseCase bulkSoftDeleteByIdsUseCase;
    private final VariantBulkSoftDeletionByProductIdForProductUseCase bulkSoftDeleteByProductIdUseCase;
    private final VariantBulkHardDeletionByProductIdForProductUseCase bulkHardDeleteByProductIdUseCase;
    private final VariantBulkRestorationByIdsForProductUseCase bulkRestorationByIdsUseCase;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onProductNameChanged(
            final ProductNameChangedEvent event) {
        final var cmd = new VariantProductNameBulkUpdateForProductCommand(
                event.getProductId().value(),
                event.getNewName().value());
        try {
            this.productNameBulkUpdateUseCase.updateAll(cmd);
        } catch (final RuntimeException exception) {
            this.logCascadeFailure(event, exception);
        }
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onProductOptionAdded(
            final ProductOptionAddedEvent event) {
        final var traitsById = event.getTraitsByVariantId();

        final var traitListById = HashMap.<UUID, List<String>>newHashMap(traitsById.size());
        for (final var entry : traitsById.entrySet()) {
            final var variantId = entry.getKey();
            final var traits = entry.getValue();

            traitListById.put(variantId.value(), traits.unwrap());
        }

        final var cmd = new VariantTraitBulkUpdateByIdsForProductCommand(
                traitListById,
                event.getProductId().value());

        try {
            this.traitBulkUpdateByIdsUseCase.updateAll(cmd);
        } catch (final RuntimeException exception) {
            this.logCascadeFailure(event, exception);
        }
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onProductOptionRemoved(
            final ProductOptionRemovedEvent event) {
        final var traitsById = event.getTraitsByVariantId();

        final var traitListById = HashMap.<UUID, List<String>>newHashMap(traitsById.size());
        for (final var entry : traitsById.entrySet()) {
            final var variantId = entry.getKey();
            final var traits = entry.getValue();

            traitListById.put(variantId.value(), traits.unwrap());
        }

        final var cmd = new VariantTraitBulkUpdateByIdsForProductCommand(
                traitListById,
                event.getProductId().value());

        try {
            this.traitBulkUpdateByIdsUseCase.updateAll(cmd);
        } catch (final RuntimeException exception) {
            this.logCascadeFailure(event, exception);
        }
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onProductVariantBulkRemoved(
            final ProductVariantBulkRemovedEvent event) {
        final var variantIdSet = event.getRemovedVariantIdSet().stream()
                .map(ProductVariantId::value)
                .collect(Collectors.toUnmodifiableSet());
        final var cmd = new VariantBulkSoftDeletionByIdsForProductCommand(
                variantIdSet,
                event.getProductId().value());
        try {
            this.bulkSoftDeleteByIdsUseCase.softDeleteAll(cmd);
        } catch (final RuntimeException exception) {
            this.logCascadeFailure(event, exception);
        }
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onProductSoftDeleted(
            final ProductSoftDeletedEvent event) {
        final var cmd = new VariantBulkSoftDeletionByProductIdForProductCommand(
                event.getProductId().value());

        try {
            this.bulkSoftDeleteByProductIdUseCase.softDeleteAll(cmd);
        } catch (final RuntimeException exception) {
            this.logCascadeFailure(event, exception);
        }
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onProductHardDeleted(
            final ProductHardDeletedEvent event) {
        final var cmd = new VariantBulkHardDeletionByProductIdForProductCommand(
                event.getProductId().value());

        try {
            this.bulkHardDeleteByProductIdUseCase.hardDeleteAll(cmd);
        } catch (final RuntimeException ex) {
            this.logCascadeFailure(event, ex);
        }
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onProductRestored(
            final ProductRestoredEvent event) {
        final var variantIdSet = event.getActiveVariantIdSet().stream()
                .map(ProductVariantId::value)
                .collect(Collectors.toUnmodifiableSet());
        final var cmd = new VariantBulkRestorationByIdsForProductCommand(
                variantIdSet,
                event.getProductId().value());

        try {
            this.bulkRestorationByIdsUseCase.restoreAll(cmd);
        } catch (final RuntimeException ex) {
            this.logCascadeFailure(event, ex);
        }
    }

    private void logCascadeFailure(
            final ProductEvent event,
            final RuntimeException exception) {
        log.error("Product-to-variant cascade failed (product committed, awaiting reconcile job to converge):"
                + " event={} product={}",
                event.getEventId(),
                event.getProductId().value(),
                exception);
    }
}
