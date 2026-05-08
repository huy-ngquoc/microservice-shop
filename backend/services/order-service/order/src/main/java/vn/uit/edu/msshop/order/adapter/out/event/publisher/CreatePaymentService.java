package vn.uit.edu.msshop.order.adapter.out.event.publisher;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;
import vn.uit.edu.msshop.order.adapter.remote.PaymentChecker;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.OrderDetailEvent;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentStatus;

@Service
@RequiredArgsConstructor
public class CreatePaymentService {
    private final OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private final PaymentChecker paymentChecker;
    private final LoadOrderPort loadPort;
    private final SaveOrderPort savePort;
    private final OrderUpdatedRepository orderUpdatedRepo;
    @Transactional
    public void createPayment(OrderCreatedDocument event) {
        OrderCreated orderCreated = new OrderCreated(event.getEventId(),event.getCurrency(), event.getOrderId(),event.getPaymentMethod(), event.getPaymentValue(), event.getUserId(), event.getUserEmail());
                
        Order order=null;
        try {
            order = loadPort.loadById(new OrderId(event.getOrderId())).orElse(null);
            paymentChecker.createPayment(orderCreated);
        
                
                if(order==null) {
                    handleFailure(event, "Order not found with id "+event.getEventId(),order);
                    return;
                }
                if(!order.getStatus().value().equals("PENDING_PAYMENT")) return;
                order = order.updateStatus(new OrderStatus("WAITING_PAYMENT"));
                savePort.save(order);
                markAsSent(event);

            }
             catch (Exception e) {
                handleFailure(event, e.getMessage(), order);
            }
            
    }
    @Transactional
    public void handleFailure(OrderCreatedDocument event, String error,Order order) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 20) {
            
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        if(order==null) return;
        final var toSave = order.updateStatus(new OrderStatus("PAYMENT_ERROR")).updatePaymentStatus(new PaymentStatus("FAILED"));
        
        savePort.save(toSave);
        OrderUpdatedEventDocument orderUpdatedEventDocument = getOrderUpdatedEvent(toSave);
        orderUpdatedRepo.save(orderUpdatedEventDocument);

        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    
    public void markAsSent(OrderCreatedDocument event) {
        updateStatus(event, "SENT", null);
    }
     private void updateStatus(OrderCreatedDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        orderCreatedDocumentRepo.save(event);
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
            .oldStatus("PENDING_PAYMENT")
            .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
    }
}
