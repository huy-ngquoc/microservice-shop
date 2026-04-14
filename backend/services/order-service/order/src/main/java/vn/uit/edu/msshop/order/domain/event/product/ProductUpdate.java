package vn.uit.edu.msshop.order.domain.event.product;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdate {
    private UUID eventId;
    private UUID productId;
    private String name;
}
