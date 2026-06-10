package vn.edu.uit.msshop.product.category.application.dto.command.lifecycle;

import java.util.UUID;

public record CategoryRestorationByIdCommand(
        UUID categoryId,
        long categoryVersion) {
}
