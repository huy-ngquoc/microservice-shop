package vn.edu.uit.msshop.product.shared.event.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VariantDeleted {
    private UUID eventId;
    private UUID variantId;
}

