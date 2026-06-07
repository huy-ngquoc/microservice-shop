package vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle;

import java.util.UUID;

import vn.edu.uit.msshop.shared.application.dto.Change;

public record BrandInfoUpdateByIdCommand(
        UUID brandId,
        Change<String> brandNameChange,
        long brandVersion) {
}
