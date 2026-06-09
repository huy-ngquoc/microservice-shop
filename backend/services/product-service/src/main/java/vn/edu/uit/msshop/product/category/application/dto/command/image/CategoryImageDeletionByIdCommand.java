package vn.edu.uit.msshop.product.category.application.dto.command.image;

import java.util.UUID;

public record CategoryImageDeletionByIdCommand(
        UUID categoryId,
        long categoryVersion) {
}
