package vn.uit.edu.msshop.inventory.application.port.out;

import vn.uit.edu.msshop.inventory.adapter.out.event.ForceCancellOrderDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.InventoryUpdatedDocument;
import vn.uit.edu.msshop.inventory.domain.event.UpdateManyInventoriesEvent;

public interface PublishInventoryEventPort {
    public void publishInventoryUpdateEvent(InventoryUpdatedDocument outboxEvent);
    public void publishForceCancellOrderEvent(ForceCancellOrderDocument outboxEvent);
    public void publicUpdateManyInventoriesEvent(UpdateManyInventoriesEvent event);
}
