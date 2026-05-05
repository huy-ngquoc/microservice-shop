package vn.edu.uit.msshop.product.variant.domain.event;

import java.util.UUID;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InventoryUpdated {
  @NonNull
  private UUID variantId;
  private int newQuantity;
  private int newReservedQuantity;
  @NonNull
  private UUID eventId;

  public InventoryUpdated() {
    variantId = UUID.randomUUID();
    eventId = UUID.randomUUID();
  }
}
