package vn.edu.uit.msshop.product.product.application.dto.command.option;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record ProductOptionRemovalCommand(
        UUID productId,
        int optionIndex,
        @Nullable
        Long defaultPrice,
        long productVersion) {
}
