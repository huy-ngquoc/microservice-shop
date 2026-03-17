package vn.uit.edu.msshop.inventory.domain.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateManyInventoriesEvent {
    private List<InventoryUpdated> listInventories;
}
