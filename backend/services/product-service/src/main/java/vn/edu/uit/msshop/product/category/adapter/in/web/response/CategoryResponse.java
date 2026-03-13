package vn.edu.uit.msshop.product.category.adapter.in.web.response;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String imageKey) {
}
