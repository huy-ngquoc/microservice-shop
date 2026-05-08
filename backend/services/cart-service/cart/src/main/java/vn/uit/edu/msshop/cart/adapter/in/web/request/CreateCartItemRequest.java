package vn.uit.edu.msshop.cart.adapter.in.web.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartItemRequest {
    private UUID variantId;
    private int amount;
}
