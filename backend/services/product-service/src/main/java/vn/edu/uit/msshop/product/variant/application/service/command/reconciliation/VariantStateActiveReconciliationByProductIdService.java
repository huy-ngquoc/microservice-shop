package vn.edu.uit.msshop.product.variant.application.service.command.reconciliation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.reconciliation.VariantStateActiveReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkRestorationByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantProductNameBulkUpdateForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.reconciliation.VariantStateActiveReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkRestorationByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantProductNameBulkUpdateForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.reconciliation.VariantProductReconciliationActiveLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.reconciliation.VariantProductReconciliationSnapshot;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
class VariantStateActiveReconciliationByProductIdService
        implements VariantStateActiveReconciliationByProductIdUseCase {

    private final VariantProductReconciliationActiveLookupByProductIdPort productReconciliationActiveLookupByProductIdPort;
    private final VariantActiveBulkLookupByProductIdPort activeBulkLookupByProductIdPort;
    private final VariantBulkRestorationByIdsForProductUseCase bulkRestorationByIdsForProductUseCase;
    private final VariantProductNameBulkUpdateForProductUseCase productNameBulkUpdateForProductUseCase;

    @Override
    public void reconcile(
            final VariantStateActiveReconciliationByProductIdCommand command) {
        final var productId = new VariantProductId(command.productId());

        final var productReconciliationSnapshot = this.productReconciliationActiveLookupByProductIdPort
                .findActiveByProductId(productId)
                .orElse(null);
        if (productReconciliationSnapshot == null) {
            return;
        }

        final var activeVariantList = this.activeBulkLookupByProductIdPort
                .loadAllActiveByProductId(productId);
        if (this.restoreLostVariants(
                productId,
                productReconciliationSnapshot,
                activeVariantList)) {
            return;
        }

        this.realignProductName(
                productId,
                productReconciliationSnapshot,
                activeVariantList);
    }

    private boolean restoreLostVariants(
            final VariantProductId productId,
            final VariantProductReconciliationSnapshot snapshot,
            final List<Variant> activeVariantList) {
        if (!activeVariantList.isEmpty() || snapshot.variantIdSet().isEmpty()) {
            return false;
        }

        final var variantIdSet = snapshot.variantIdSet().stream()
                .map(VariantId::value)
                .collect(Collectors.toUnmodifiableSet());
        final var newCommand = new VariantBulkRestorationByIdsForProductCommand(
                variantIdSet,
                productId.value());
        this.bulkRestorationByIdsForProductUseCase.restoreAll(newCommand);

        return true;
    }

    private void realignProductName(
            final VariantProductId productId,
            final VariantProductReconciliationSnapshot snapshot,
            final List<Variant> activeVariantList) {
        final var nameDrift = activeVariantList.stream()
                .anyMatch(variant -> !variant.getProductName().equals(snapshot.name()));
        if (!nameDrift) {
            return;
        }

        final var newCommand = new VariantProductNameBulkUpdateForProductCommand(
                productId.value(),
                snapshot.name().value());
        this.productNameBulkUpdateForProductUseCase.updateAll(newCommand);
    }
}
