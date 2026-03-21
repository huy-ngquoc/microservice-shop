package vn.uit.edu.msshop.inventory.adapter.out.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.port.out.PublishInventoryEventPort;
import vn.uit.edu.msshop.inventory.domain.event.ForceCancellOrder;
import vn.uit.edu.msshop.inventory.domain.event.InventoryUpdated;
import vn.uit.edu.msshop.inventory.domain.event.UpdateManyInventoriesEvent;

@Component
@RequiredArgsConstructor
public class InventoryEventPublisher implements PublishInventoryEventPort {
    private final KafkaTemplate<String, InventoryUpdated> inventoryUpdateTemplate;
    private final KafkaTemplate<String, ForceCancellOrder> forceCancellOrderTemplate;
    private final KafkaTemplate<String, UpdateManyInventoriesEvent> updateManyTemplate;
    private static final String INVENTORY_TOPIC="inventory-product";
    private static final String ORDER_TOPIC="inventory-order";

    @Override
    public void publishInventoryUpdateEvent(InventoryUpdated event) {
        Message<InventoryUpdated> message= MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, INVENTORY_TOPIC).build();
        inventoryUpdateTemplate.send(message);
    }

    @Override
    public void publishForceCancellOrderEvent(ForceCancellOrder event) {
        Message<ForceCancellOrder> message= MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, ORDER_TOPIC).build();
        forceCancellOrderTemplate.send(message);
    }

    @Override
    public void publicUpdateManyInventoriesEvent(UpdateManyInventoriesEvent event) {
        Message<UpdateManyInventoriesEvent> message= MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, INVENTORY_TOPIC).build();
        updateManyTemplate.send(message);
    }

}
