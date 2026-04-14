package vn.edu.uit.msshop.product.product.domain.event;

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
public class VariantChange {
    private UUID eventId;
    private UUID productId;
    private long price;

    private int soldCount;

    private List<String> traits;

        
    private String imageKey;
}
