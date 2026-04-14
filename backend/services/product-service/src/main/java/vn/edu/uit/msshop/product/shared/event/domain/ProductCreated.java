package vn.edu.uit.msshop.product.shared.event.domain;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor

@Builder
public class ProductCreated {
    private UUID eventId;
    private UUID productId;
    private String productName;
    private List<VariantCreated> variantCreateds;

    
}