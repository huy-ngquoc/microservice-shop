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
import vn.uit.edu.msshop.inventory.adapter.out.persistence.OrderCancelled;
import vn.uit.edu.msshop.inventory.adapter.out.persistence.OrderCancelledRepository;
import vn.uit.edu.msshop.inventory.adapter.out.persistence.OrderShipped;
import vn.uit.edu.msshop.inventory.adapter.out.persistence.OrderShippedRepository;
import vn.uit.edu.msshop.inventory.application.port.in.UpdateInventoryUseCase;
import vn.uit.edu.msshop.inventory.domain.event.OrderUpdatedEvent;

@Component
@KafkaListener(topics="order-topic", groupId="inventory-group")
@RequiredArgsConstructor
public class InventoryOrderListener {
    private final UpdateInventoryUseCase updateUseCase;
    private final EventDocumentRepository eventDocumentRepo;
    private final InventoryWebMapper webMapper;
    private final OrderShippedRepository orderShippedRepo;
    private final OrderCancelledRepository orderCancelledRepo;

    @KafkaHandler
    public void onOrderUpdated(OrderUpdatedEvent event) {
        processOrderUpdated(event);
    }
    @Transactional
    public void processOrderUpdated(OrderUpdatedEvent event) {
        if(eventDocumentRepo.existsById(event.getEventId())) return;
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
        if(event.getStatus().equals("SHIPPING")) {
            if(orderShippedRepo.existsById(event.getOrderId())) return;
            System.out.println("Shipping");
            updateUseCase.updateWhenOrderShipped(webMapper.toShippedCommand(event));
            orderShippedRepo.save(new OrderShipped(event.getOrderId(), Instant.now()));
        }
        else if(event.getStatus().equals("CANCELLED")&&!event.getOldStatus().equals("PENDING")) {
            if(orderCancelledRepo.existsById(event.getOrderId())) return;
            updateUseCase.updateWhenOrderCancelled(webMapper.toCancelledCommand(event));
            orderCancelledRepo.save(new OrderCancelled(event.getOrderId(),Instant.now()));
        }
        else if(event.getStatus().equals("PAYMENT_ERROR")||event.getStatus().equals("PAYMENT_EXPIRED")) {
            if(orderCancelledRepo.existsById(event.getOrderId())) return;
            System.out.println("Cancell order");
            updateUseCase.updateWhenOrderCancelled(webMapper.toCancelledCommand(event));
            orderCancelledRepo.save(new OrderCancelled(event.getOrderId(),Instant.now()));
        }
        
    }
    @KafkaHandler(isDefault=true)
    public void onObjectReceived(Object event){
        
    }
}
