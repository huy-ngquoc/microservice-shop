package vn.uit.edu.msshop.cart.domain.event;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdated {
    private UUID eventId;
    private UUID variantId;
    private List<String> traits;
    private long unitPrice;
    private String name;
    private String imageKey;
}
