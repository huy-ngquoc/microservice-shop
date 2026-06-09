package vn.edu.uit.msshop.product.category.application.dto.command.image;

import java.util.UUID;

public record CategoryImageUpdateByIdCommand(
        UUID categoryId,
        String categoryNewImageKey,
        long categoryVersion) {
}
