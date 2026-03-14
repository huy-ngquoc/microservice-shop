package vn.uit.edu.msshop.cart.adapter.in.web.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.cart.adapter.in.web.request.common.ChangeRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartAmountReuest {
    private UUID userId;
    private UUID variantId;
    private ChangeRequest<Integer> amount;
}
