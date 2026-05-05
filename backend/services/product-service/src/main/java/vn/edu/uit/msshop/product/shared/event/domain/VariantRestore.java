package vn.edu.uit.msshop.product.shared.event.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VariantRestore {
  private UUID eventId;
  private UUID variantId;
}
