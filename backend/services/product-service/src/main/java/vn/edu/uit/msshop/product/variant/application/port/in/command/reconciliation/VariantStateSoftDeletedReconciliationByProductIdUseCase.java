package vn.edu.uit.msshop.product.variant.application.port.in.command.reconciliation;

import vn.edu.uit.msshop.product.variant.application.dto.command.reconciliation.VariantStateSoftDeletedReconciliationByProductIdCommand;

public interface VariantStateSoftDeletedReconciliationByProductIdUseCase {
    void reconcile(
            VariantStateSoftDeletedReconciliationByProductIdCommand cmd);
}
