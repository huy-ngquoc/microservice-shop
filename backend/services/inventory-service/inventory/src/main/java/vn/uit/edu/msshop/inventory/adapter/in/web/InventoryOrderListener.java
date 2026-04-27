package vn.uit.edu.msshop.inventory.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.in.web.mapper.InventoryWebMapper;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.inventory.application.port.in.UpdateInventoryUseCase;
import vn.uit.edu.msshop.inventory.domain.event.OrderUpdatedEvent;

@Component
@KafkaListener(topics="order-topic", groupId="inventory-group")
@RequiredArgsConstructor
public class InventoryOrderListener {
    private final UpdateInventoryUseCase updateUseCase;
    private final EventDocumentRepository eventDocumentRepo;
    private final InventoryWebMapper webMapper;

    @KafkaHandler
    @Transactional
    public void onOrderUpdated(OrderUpdatedEvent event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        if(event.getStatus().equals("SHIPPING")) {
            updateUseCase.updateWhenOrderShipped(webMapper.toShippedCommand(event));
        }
        else if(event.getStatus().equals("CANCELLED")) {
            updateUseCase.updateWhenOrderCancelled(webMapper.toCancelledCommand(event));
        }
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }
    @KafkaHandler(isDefault=true)
    public void onObjectReceived(Object event){
        
    }
}
