package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import java.util.UUID;

public record ProductHardDeletionCommand(
        UUID productId,
        long productVersion) {
}
