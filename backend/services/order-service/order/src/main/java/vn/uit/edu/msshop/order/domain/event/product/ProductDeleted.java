package vn.uit.edu.msshop.order.domain.event.product;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor

public class ProductDeleted {
    private UUID eventId;
    private UUID productId;
}
