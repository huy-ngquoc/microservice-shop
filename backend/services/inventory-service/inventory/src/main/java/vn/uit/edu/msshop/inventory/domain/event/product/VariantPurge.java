package vn.uit.edu.msshop.inventory.domain.event.product;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VariantPurge {
    private UUID eventId;
    private UUID variantId;
}
