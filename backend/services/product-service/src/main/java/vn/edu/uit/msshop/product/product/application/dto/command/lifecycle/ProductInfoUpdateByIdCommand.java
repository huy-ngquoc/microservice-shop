package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import java.util.UUID;

import vn.edu.uit.msshop.shared.application.dto.Change;

public record ProductInfoUpdateByIdCommand(
        UUID productId,
        Change<String> productNameChange,
        Change<UUID> categoryIdChange,
        Change<UUID> brandIdChange,
        long productVersion) {
}
