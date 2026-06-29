package vn.edu.uit.msshop.product.variant.application.service.command.reconciliation;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.reconciliation.VariantStateSoftDeletedReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByProductIdForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.reconciliation.VariantStateSoftDeletedReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkSoftDeletionByProductIdForProductUseCase;

@Service
@RequiredArgsConstructor
class VariantStateSoftDeletedReconciliationByProductIdService
        implements VariantStateSoftDeletedReconciliationByProductIdUseCase {

    private final VariantBulkSoftDeletionByProductIdForProductUseCase bulkSoftDeletionByProductIdUseCase;

    @Override
    public void reconcile(
            VariantStateSoftDeletedReconciliationByProductIdCommand cmd) {
        final var newCommand = new VariantBulkSoftDeletionByProductIdForProductCommand(cmd.productId());
        this.bulkSoftDeletionByProductIdUseCase.softDeleteAll(newCommand);
    }

}
