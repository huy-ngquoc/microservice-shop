package vn.edu.uit.msshop.product.variant.application.service.command.reconciliation;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.dto.command.reconciliation.VariantStateActiveReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.reconciliation.VariantStateSoftDeletedReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.reconciliation.VariantStateActiveReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.reconciliation.VariantStateBulkReconciliationUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.reconciliation.VariantStateSoftDeletedReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.reconciliation.VariantProductReconciliationActiveIdListingPort;
import vn.edu.uit.msshop.product.variant.application.port.out.reconciliation.VariantProductReconciliationSoftDeletedIdListingPort;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
class VariantStateBulkReconciliationService
        implements VariantStateBulkReconciliationUseCase {

    private static final int PAGE_SIZE = 100;

    private final VariantProductReconciliationActiveIdListingPort activeIdListingPort;
    private final VariantProductReconciliationSoftDeletedIdListingPort softDeletedIdListingPort;
    private final VariantStateActiveReconciliationByProductIdUseCase activeReconciliationUseCase;
    private final VariantStateSoftDeletedReconciliationByProductIdUseCase softDeletedReconciliationUseCase;

    @Override
    @Transactional
    public void reconcileAll() {
        this.reconcileActiveProducts();
        this.reconcileSoftDeletedProducts();
    }

    private void reconcileActiveProducts() {
        int page = 0;
        while (true) {
            final var pageRequest = new PageRequestDto(page, PAGE_SIZE);
            final var response = this.activeIdListingPort.listActiveIds(pageRequest);
            for (final var productId : response.items()) {
                this.reconcileActiveOne(productId);
            }

            if (!response.hasNext()) {
                break;
            }
            page++;
        }
    }

    private void reconcileSoftDeletedProducts() {
        int page = 0;
        while (true) {
            final var pageRequest = new PageRequestDto(page, PAGE_SIZE);
            final var response = this.softDeletedIdListingPort.listSoftDeletedIds(pageRequest);
            for (final var productId : response.items()) {
                this.reconcileSoftDeletedOne(productId);
            }

            if (!response.hasNext()) {
                break;
            }
            page++;
        }
    }

    private void reconcileActiveOne(
            final UUID productId) {
        final var cmd = new VariantStateActiveReconciliationByProductIdCommand(productId);

        try {
            this.activeReconciliationUseCase.reconcile(cmd);
        } catch (final RuntimeException exception) {
            log.error("Variant-state reconciliation (active) failed for product {}",
                    productId,
                    exception);
        }
    }

    private void reconcileSoftDeletedOne(
            final UUID productId) {
        final var cmd = new VariantStateSoftDeletedReconciliationByProductIdCommand(productId);

        try {
            this.softDeletedReconciliationUseCase.reconcile(cmd);
        } catch (final RuntimeException exception) {
            log.error("Variant-state reconciliation (soft-deleted) failed for product {}",
                    productId,
                    exception);
        }
    }
}
