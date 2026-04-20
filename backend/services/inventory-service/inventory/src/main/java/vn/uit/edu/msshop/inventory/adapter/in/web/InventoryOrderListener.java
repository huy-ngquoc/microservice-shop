package vn.uit.edu.msshop.inventory.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.in.web.mapper.InventoryWebMapper;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.inventory.application.port.in.UpdateInventoryUseCase;
import vn.uit.edu.msshop.inventory.domain.event.OrderCreated;
import vn.uit.edu.msshop.inventory.domain.event.OrderUpdatedEvent;

@Component
@RequiredArgsConstructor
@KafkaListener(topics="order-topic",groupId="order-group")
public class InventoryOrderListener {
    private final InventoryWebMapper mapper;
    private final UpdateInventoryUseCase updateInventoryUseCase;
    private final EventDocumentRepository eventDocumentRepo;

    @KafkaHandler
    public void onOrderCreated(OrderCreated event) {
        /*if(!eventDocumentRepo.existsById(event.getEventId())) {
        final var command = mapper.toCommand(event);
        updateInventoryUseCase.updateWhenOrderCreated(command);
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
        }*/
    }
    @KafkaHandler
    public void onOrderUpdated(OrderUpdatedEvent event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        if(event.getStatus().equals("SHIPPING")) {
            updateInventoryUseCase.updateWhenOrderShipped(mapper.toShippedCommand(event));
        }
        if(event.getStatus().equals("CANCELLED")) {
            updateInventoryUseCase.updateWhenOrderCancelled(mapper.toCancelledCommand(event));
        }
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }
}
