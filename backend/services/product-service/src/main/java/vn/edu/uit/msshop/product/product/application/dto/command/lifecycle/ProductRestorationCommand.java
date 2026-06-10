package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import java.util.UUID;

public record ProductRestorationCommand(
        UUID productId,
        long productVersion) {
}
