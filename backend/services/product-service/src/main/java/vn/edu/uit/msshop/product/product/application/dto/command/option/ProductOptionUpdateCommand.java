package vn.edu.uit.msshop.product.product.application.dto.command.option;

import java.util.UUID;

public record ProductOptionUpdateCommand(
        UUID productId,
        int optionIndex,
        String newOption,
        long productVersion) {
}
