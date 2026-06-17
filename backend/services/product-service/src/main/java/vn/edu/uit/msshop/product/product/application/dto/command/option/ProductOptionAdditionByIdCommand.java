package vn.edu.uit.msshop.product.product.application.dto.command.option;

import java.util.UUID;

public record ProductOptionAdditionByIdCommand(
        UUID productId,
        String newOption,
        String defaultTrait,
        long productVersion) {
}
