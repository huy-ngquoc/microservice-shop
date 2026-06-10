package vn.edu.uit.msshop.product.product.application.dto.command.option;

import java.util.UUID;

public record ProductOptionAdditionCommand(
        UUID productId,
        String newOption,
        String defaultTrait,
        long productVersion) {
}
