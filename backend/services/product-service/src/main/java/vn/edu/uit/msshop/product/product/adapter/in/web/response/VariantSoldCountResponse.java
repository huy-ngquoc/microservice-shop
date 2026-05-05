package vn.edu.uit.msshop.product.product.adapter.in.web.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VariantSoldCountResponse {
  private UUID variantId;
  private int soldCount;
}
