package vn.uit.edu.msshop.order.adapter.in.web.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VariantSoldCountResponse {
    private UUID variantId;
    private int soldCount;
}
