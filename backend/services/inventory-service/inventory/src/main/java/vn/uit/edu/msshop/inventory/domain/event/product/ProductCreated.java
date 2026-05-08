package vn.uit.edu.msshop.inventory.domain.event.product;
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
public class ProductCreated {
    private UUID eventId;
    private UUID productId;
    private String productName;
    private List<VariantCreated> variantCreateds;

    
}
