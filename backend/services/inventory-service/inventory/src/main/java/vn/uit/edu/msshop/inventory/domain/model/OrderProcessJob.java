package vn.uit.edu.msshop.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProcessJob {
    private Inventory inventory;
    private Quantity quantity;
}
