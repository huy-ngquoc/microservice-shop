package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import java.util.List;
import java.util.UUID;

public record ProductSimpleCreationCommand(
        String productName,
        UUID categoryId,
        UUID brandId,
        long productPrice,
        List<String> productTargetList) {
}
