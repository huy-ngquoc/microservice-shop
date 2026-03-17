package vn.uit.edu.msshop.cart.domain.event;

import java.util.List;
import java.util.UUID;

public record OrderCreatedSuccess(
    UUID userId,
    List<UUID> variantIds
) {

}
