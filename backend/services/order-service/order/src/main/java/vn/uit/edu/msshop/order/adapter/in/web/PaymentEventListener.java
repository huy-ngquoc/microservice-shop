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
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.msshop.order.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.OrderDetailEvent;
import vn.uit.edu.msshop.order.domain.event.PaymentCreatedFail;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.PaymentStatus;

@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = "payment-topic",
        groupId = "order-group")
public class PaymentEventListener {
    private final LoadOrderPort loadPort;
    private final SaveOrderPort savePort;
    private final EventDocumentRepository eventDocumentRepo;
    private final OrderUpdatedRepository orderUpdatedRepo;
    private final PublishOrderEventPort publishPort;

    @KafkaHandler
    public void onPaymentCreatedFail(
            PaymentCreatedFail event) {
        if (eventDocumentRepo.existsById(event.getEventId()))
            return;
        Order order = loadPort.loadById(new OrderId(event.getOrderId())).orElse(null);
        if (order != null) {
            if (order.getPaymentMethod().value().equals("ONLINE")) {
                order = order.updatePaymentStatus(new PaymentStatus("CANCELLED"));
                order = order.updateStatus(new OrderStatus("PAYMENT_ERROR"));
                saveAndSendEvent(order, event.getEventId());
                return;
            }
        }
        eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
    }

    @Transactional
    public void saveAndSendEvent(
            Order order,
            UUID eventId) {
        savePort.save(order);
        final var savedEvent = orderUpdatedRepo.save(getOrderUpdatedEvent(order));
        eventDocumentRepo.save(EventDocument.builder().eventId(eventId).receiveAt(Instant.now()).build());
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishPort.publishOrderUpdatedEvent(savedEvent);
            }
        });
    }

    private OrderUpdatedEventDocument getOrderUpdatedEvent(
            Order order) {
        List<OrderDetailEvent> detailEvents = order.getDetails().stream()
                .map(item -> new OrderDetailEvent(item.variantId(), item.productId(), item.amount())).toList();
        return OrderUpdatedEventDocument.builder()
                .eventId(UUIDs.newId())
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
