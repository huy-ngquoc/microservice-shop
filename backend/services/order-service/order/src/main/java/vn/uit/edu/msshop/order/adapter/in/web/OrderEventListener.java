package vn.uit.edu.msshop.order.adapter.in.web;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;
import vn.uit.edu.msshop.order.application.exception.OrderNotFoundException;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.OnlinePaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.OnlinePaymentExpired;
import vn.uit.edu.msshop.order.domain.event.OrderDetailEvent;
import vn.uit.edu.msshop.order.domain.event.PaymentSuccess;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentStatus;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics="payment-online-topic",groupId="order-payment-group")
public class OrderEventListener {
    private final LoadOrderPort loadPort;
    private final SaveOrderPort savePort;
    private final EventDocumentRepository eventDocumentRepo;
    private final OrderUpdatedRepository orderUpdatedRepo;
    private final PublishOrderEventPort publishEventPort;
    @KafkaHandler
    @Transactional
    public void onPaymentCancelled(OnlinePaymentCancelled event) {
        if(event.eventId()==null||event.orderId()==null) {
            System.out.println("Con di me may");
            System.out.println(event.orderId());
            return;
        }
        if(!eventDocumentRepo.existsById(event.eventId())) {
        Order order = loadPort.loadById(new OrderId(event.orderId())).orElseThrow(()->new OrderNotFoundException(new OrderId(event.orderId())));
        Order.UpdateInfo updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        final var saved = order.applyUpdateInfo(updateInfo).updatePaymentStatus(new PaymentStatus("CANCELLED"));
        savePort.save(saved);
        eventDocumentRepo.save(new EventDocument(event.eventId(), Instant.now()));
        }
    } 
    @KafkaHandler
    @Transactional
    public void onPaymentExpired(OnlinePaymentExpired event) {
        if(event.eventId()==null||event.orderId()==null) {
            System.out.println("Con di me may");
            System.out.println(event.orderId());
            return;
        }
        if(!eventDocumentRepo.existsById(event.eventId())) {
        Order order = loadPort.loadById(new OrderId(event.orderId())).orElseThrow(()->new OrderNotFoundException(new OrderId(event.orderId())));
        String oldStatus = order.getStatus().value();
        Order.UpdateInfo updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        final var saved = order.applyUpdateInfo(updateInfo).updatePaymentStatus(new PaymentStatus("EXPIRED"));
        List<OrderDetailEvent> detailEvents = saved.getDetails().stream().map(item->new OrderDetailEvent(item.variantId(), item.productId(), item.amount())).toList();
            OrderUpdatedEventDocument eventDocument = OrderUpdatedEventDocument.builder()
            .eventId(UUID.randomUUID())
            .orderId(saved.getId().value())
            .details(detailEvents)
            .status(saved.getStatus().value())
            .userId(saved.getUserId().value())
            .originPrice(saved.getOriginPrice().value())
            .shippingFee(saved.getShippingFee().value())
            .discount(saved.getDiscount().value())
            .totalPrice(saved.getTotalPrice().value())
            .currency(saved.getCurrency().value())
            .paymentMethod(saved.getPaymentMethod().value())
            .paymentStatus(saved.getPaymentStatus().value())
            .email(saved.getShippingInfo().email())
            .oldStatus(oldStatus)
            .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        final var savedEvent = orderUpdatedRepo.save(eventDocument);

        savePort.save(saved);
        eventDocumentRepo.save(new EventDocument(event.eventId(), Instant.now()));
         TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishEventPort.publishOrderUpdatedEvent(savedEvent);
            }
        });
        }
    }
    @KafkaHandler
    public void onOnlinePaymentSuccess(PaymentSuccess event) {
        System.out.println("Nhan event");
        if(event.getEventId()==null||event.getOrderId()==null) {
            System.out.println("Con di me may");
            System.out.println(event.getOrderId().toString());
            return;
        }
        if(!eventDocumentRepo.existsById(event.getEventId())) {
        Order order = loadPort.loadById(new OrderId(event.getOrderId())).orElseThrow(()->new OrderNotFoundException(new OrderId(event.getOrderId())));
        Order.UpdateInfo updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        final var saved = order.applyUpdateInfo(updateInfo).updatePaymentStatus(new PaymentStatus("SUCCESS"));
        
        savePort.save(saved);
        eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
        }

    }
    @KafkaHandler(isDefault=true)
    public void onObjectReceived(Object event) {
        
    }
    
}
