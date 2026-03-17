package vn.uit.edu.msshop.inventory.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.OrderQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailCommand {
    private VariantId variantId;
    private OrderQuantity quantity;
}
