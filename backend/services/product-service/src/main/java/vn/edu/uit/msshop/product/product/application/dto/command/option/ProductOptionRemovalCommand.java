package vn.edu.uit.msshop.product.product.application.dto.command.option;

import java.util.UUID;

public record ProductOptionRemovalCommand(
        UUID productId,
        int optionIndex,
        long productVersion) {
}
