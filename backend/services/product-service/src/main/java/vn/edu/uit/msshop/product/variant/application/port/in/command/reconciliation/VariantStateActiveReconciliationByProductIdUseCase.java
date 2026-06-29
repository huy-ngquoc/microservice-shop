package vn.edu.uit.msshop.product.variant.application.port.in.command.reconciliation;

import vn.edu.uit.msshop.product.variant.application.dto.command.reconciliation.VariantStateActiveReconciliationByProductIdCommand;

public interface VariantStateActiveReconciliationByProductIdUseCase {
    void reconcile(
            VariantStateActiveReconciliationByProductIdCommand cmd);
}
