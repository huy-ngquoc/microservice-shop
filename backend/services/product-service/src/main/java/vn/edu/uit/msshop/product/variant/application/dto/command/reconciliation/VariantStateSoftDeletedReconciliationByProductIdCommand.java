package vn.edu.uit.msshop.product.variant.application.dto.command.reconciliation;

import java.util.UUID;

public record VariantStateSoftDeletedReconciliationByProductIdCommand(
        UUID productId) {
}
