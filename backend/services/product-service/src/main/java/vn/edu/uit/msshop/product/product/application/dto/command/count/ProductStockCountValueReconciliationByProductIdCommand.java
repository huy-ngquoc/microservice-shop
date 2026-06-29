package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.UUID;

public record ProductStockCountValueReconciliationByProductIdCommand(
        UUID productId) {
}
