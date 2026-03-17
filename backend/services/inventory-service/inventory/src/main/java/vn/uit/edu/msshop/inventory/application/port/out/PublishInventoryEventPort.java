package vn.uit.edu.msshop.inventory.application.port.out;

import vn.uit.edu.msshop.inventory.domain.event.ForceCancellOrder;
import vn.uit.edu.msshop.inventory.domain.event.InventoryUpdated;
import vn.uit.edu.msshop.inventory.domain.event.UpdateManyInventoriesEvent;

public interface PublishInventoryEventPort {
    public void publishInventoryUpdateEvent(InventoryUpdated event);
    public void publishForceCancellOrderEvent(ForceCancellOrder event);
    public void publicUpdateManyInventoriesEvent(UpdateManyInventoriesEvent event);
}
