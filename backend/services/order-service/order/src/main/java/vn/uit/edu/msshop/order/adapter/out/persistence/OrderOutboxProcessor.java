package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedSuccessDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;
import vn.uit.edu.msshop.order.adapter.remote.InventoryChecker;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.OrderDetailEvent;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;

@Component
@RequiredArgsConstructor
public class OrderOutboxProcessor {
    private final OrderOutboxRepository orderOutboxRepo;
    private final LoadOrderPort loadPort;
    private final SaveOrderPort savePort;
    private final InventoryChecker inventoryChecker;
    private final OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private final OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo;
    
    private final OrderUpdatedRepository orderUpdatedRepo;
    @Transactional
    public void confirmOrderAndCreateEvent(OrderOutbox outbox, Order order) {
        final var toSave=order.updateStatus(new OrderStatus("CONFIRMED"));
        outbox.setOutboxStatus("COMPLETED");
            savePort.save(toSave);
            orderOutboxRepo.save(outbox);
            orderCreatedDocumentRepo.save(createOrderCreatedEvent(order));
             orderCreatedSuccessDocumentRepo.save(createOrderCreatedSuccessDocument(order));
    }
    
    
    public void updateStatus(OrderOutbox outbox) {
        Order order= loadPort.loadById(new OrderId(outbox.getOrderId())).orElse(null);
            
            if(order==null) {
                outbox.setOutboxStatus("COMPLETED");
                orderOutboxRepo.save(outbox);
                return;
            }
        try {
            ResponseEntity<String> processResult = inventoryChecker.process(outbox);
            
            confirmOrderAndCreateEvent(outbox, order);
            
                
                
            
            
        }
        catch(FeignException e) {
            
           handleError(e, order, outbox);
        }
            
    }
    @Transactional
    public void handleError(FeignException e, Order order, OrderOutbox outbox) {
        outbox.setRetryCount(outbox.getRetryCount()+1);
            
            if(outbox.getRetryCount()>=5) {
                outbox.setOutboxStatus("COMPLETED");
                final var isInsufficient = e.status()==400;
                String newOrderStatus = isInsufficient?"INSUFFICIENT_STOCK":"CANCELLED_BEFORE_PROCESS";
                final var toSave= order.updateStatus(new OrderStatus(newOrderStatus));
                savePort.save(toSave);
                final var savedEvent = orderUpdatedRepo.save(getOrderUpdatedEvent(order));

            }
            orderOutboxRepo.save(outbox);
    }
    private OrderCreatedDocument createOrderCreatedEvent(Order order) {
        return OrderCreatedDocument.builder().currency(order.getCurrency().value())
        .orderId(order.getId().value())
        .paymentMethod(order.getPaymentMethod().value())
        .paymentValue(order.getTotalPrice().value())
        .userEmail(order.getShippingInfo().email())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null).eventId(UUID.randomUUID())
        .lastError(null).userId(order.getUserId().value()).build();
        
    }
    private OrderCreatedSuccessDocument createOrderCreatedSuccessDocument(Order order) {
        return OrderCreatedSuccessDocument.builder().eventId(UUID.randomUUID())
        .userId(order.getUserId().value())
        .variantIds(order.getDetails().stream().map(item->item.variantId()).toList())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
    }
    private OrderUpdatedEventDocument getOrderUpdatedEvent(Order order) {
        List<OrderDetailEvent> detailEvents = order.getDetails().stream().map(item->new OrderDetailEvent(item.variantId(), item.productId(), item.amount())).toList();
        return OrderUpdatedEventDocument.builder()
            .eventId(UUID.randomUUID())
            .orderId(order.getId().value())
            .details(detailEvents)
            .status(order.getStatus().value())
            .userId(order.getUserId().value())
            .originPrice(order.getOriginPrice().value())
            .shippingFee(order.getShippingFee().value())
            .discount(order.getDiscount().value())
            .totalPrice(order.getTotalPrice().value())
            .currency(order.getCurrency().value())
            .paymentMethod(order.getPaymentMethod().value())
            .paymentStatus(order.getPaymentStatus().value())
            .email(order.getShippingInfo().email())
            .oldStatus("PENDING")
            .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
    }
}
