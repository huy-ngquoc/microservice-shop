package vn.uit.edu.msshop.cart.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ProductDeleted {
    private UUID variantId;
}
