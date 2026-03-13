package vn.edu.uit.msshop.product.category.application.dto.query;

import java.util.UUID;

public record CategoryView(
        UUID id,
        String name,
        String imageKey) {
}
