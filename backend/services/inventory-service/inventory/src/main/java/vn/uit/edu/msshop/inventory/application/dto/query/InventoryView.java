package vn.uit.edu.msshop.inventory.application.dto.query;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryView {
    private UUID id;
    private UUID variantId;
    private int quantity;
    private int reservedQuantity;
    private Instant lastUpdate;
}
