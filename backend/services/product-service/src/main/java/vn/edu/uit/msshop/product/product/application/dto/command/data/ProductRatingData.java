package vn.edu.uit.msshop.product.product.application.dto.command.data;

import java.util.UUID;

public record ProductRatingData(
        UUID productId,
        long total,
        long amount) {
}
