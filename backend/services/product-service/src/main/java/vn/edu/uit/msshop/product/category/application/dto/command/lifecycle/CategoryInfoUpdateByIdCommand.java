package vn.edu.uit.msshop.product.category.application.dto.command.lifecycle;

import java.util.UUID;

import vn.edu.uit.msshop.shared.application.dto.Change;

public record CategoryInfoUpdateByIdCommand(
        UUID categoryId,
        Change<String> categoryNameChange,
        long categoryVersion) {
}
