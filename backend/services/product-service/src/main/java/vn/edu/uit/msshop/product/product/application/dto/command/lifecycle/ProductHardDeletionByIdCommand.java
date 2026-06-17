package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import java.util.UUID;

public record ProductHardDeletionByIdCommand(
        UUID productId,
        long productVersion) {
}
