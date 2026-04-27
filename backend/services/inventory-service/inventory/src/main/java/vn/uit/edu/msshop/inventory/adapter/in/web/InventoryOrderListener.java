package vn.uit.edu.msshop.inventory.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.inventory.application.service.UpdateInventoryService;
import vn.uit.edu.msshop.inventory.domain.event.OrderUpdatedEvent;

@Component
@KafkaListener(topics="order-topic", groupId="inventory-group")
@RequiredArgsConstructor
public class InventoryOrderListener {
    private final UpdateInventoryService updateService;
    private final EventDocumentRepository eventDocumentRepo;

    @KafkaHandler
    public void onOrderUpdated(OrderUpdatedEvent event) {

    }
    @KafkaHandler(isDefault=true)
    public void onObjectReceived(Object event){
        
    }
}
