package vn.uit.edu.msshop.inventory.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VariantDeleted {
    private UUID variantId;
    private UUID eventId;
}