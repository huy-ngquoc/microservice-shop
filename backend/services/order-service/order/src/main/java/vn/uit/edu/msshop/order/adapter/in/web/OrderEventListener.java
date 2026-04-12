package vn.uit.edu.msshop.order.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.order.application.exception.OrderNotFoundException;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.OnlinePaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.OnlinePaymentExpired;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics="payment-online-topic",groupId="order-payment-group")
public class OrderEventListener {
    private final LoadOrderPort loadPort;
    private final SaveOrderPort savePort;
    private final EventDocumentRepository eventDocumentRepo;

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
        final var saved = order.applyUpdateInfo(updateInfo);
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
        Order.UpdateInfo updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        final var saved = order.applyUpdateInfo(updateInfo);
        savePort.save(saved);
        eventDocumentRepo.save(new EventDocument(event.eventId(), Instant.now()));
        }
    }
    
}
