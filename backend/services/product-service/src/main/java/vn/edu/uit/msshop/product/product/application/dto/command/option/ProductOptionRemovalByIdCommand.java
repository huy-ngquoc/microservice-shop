package vn.edu.uit.msshop.product.product.application.dto.command.option;

import java.util.UUID;

public record ProductOptionRemovalByIdCommand(
        UUID productId,
        int optionIndex,
        long productVersion) {
}
