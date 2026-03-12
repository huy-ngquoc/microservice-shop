package vn.uit.edu.msshop.order.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.application.exception.OrderNotFoundException;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.event.CodPaymentCreated;
import vn.uit.edu.msshop.order.domain.event.OnlinePaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.OnlinePaymentExpired;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics="payment-topic",groupId="order-payment-group")
public class OrderEventListener {
    private final LoadOrderPort loadPort;
    private final SaveOrderPort savePort;

    @KafkaHandler
    public void onPaymentCancelled(OnlinePaymentCancelled event) {
        Order order = loadPort.loadById(new OrderId(event.orderId())).orElseThrow(()->new OrderNotFoundException(new OrderId(event.orderId())));
        Order.UpdateInfo updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        final var saved = order.applyUpdateInfo(updateInfo);
        savePort.save(saved);
    } 
    @KafkaHandler
    public void onPaymentExpired(OnlinePaymentExpired event) {
        Order order = loadPort.loadById(new OrderId(event.orderId())).orElseThrow(()->new OrderNotFoundException(new OrderId(event.orderId())));
        Order.UpdateInfo updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("CANCELLED")).build();
        final var saved = order.applyUpdateInfo(updateInfo);
        savePort.save(saved);
    }
    @KafkaHandler
    public void onCodPaymentCreated(CodPaymentCreated event) {
        Order order = loadPort.loadById(new OrderId(event.orderId())).orElseThrow(()->new OrderNotFoundException(new OrderId(event.orderId())));
        Order.UpdateInfo updateInfo = Order.UpdateInfo.builder().id(order.getId()).shippingInfo(order.getShippingInfo()).orderStatus(new OrderStatus("RECEIVED")).build();
        final var saved = order.applyUpdateInfo(updateInfo);
        savePort.save(saved);
    }
}
